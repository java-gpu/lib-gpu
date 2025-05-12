#include "event_manager_jni.h"
#include <vector>
#include <string>
#include <jni.h>
#include "jni_utils.h"
#include <dbus/dbus.h>
#include <pthread.h>
#include <iostream>

#include <libevdev/libevdev.h>
#include <fcntl.h>
#include <unistd.h>
#include <thread>

void* listen_for_suspend(void* arg) {
    DBusError err;
    dbus_error_init(&err);

    DBusConnection* conn = dbus_bus_get(DBUS_BUS_SYSTEM, &err);
    if (dbus_error_is_set(&err)) {
        std::cerr << "Connection Error: " << err.message << std::endl;
        dbus_error_free(&err);
        return nullptr;
    }

    // Match signal from login1 Manager
    const char* match_rule =
        "type='signal',interface='org.freedesktop.login1.Manager',member='PrepareForSleep'";
    dbus_bus_add_match(conn, match_rule, &err);
    dbus_connection_flush(conn);

    if (dbus_error_is_set(&err)) {
        std::cerr << "Match Error: " << err.message << std::endl;
        dbus_error_free(&err);
        return nullptr;
    }

    while (true) {
        dbus_connection_read_write(conn, 100);
        DBusMessage* msg = dbus_connection_pop_message(conn);

        if (!msg) continue;

        if (dbus_message_is_signal(msg, "org.freedesktop.login1.Manager", "PrepareForSleep")) {
            DBusMessageIter args;
            dbus_message_iter_init(msg, &args);

            if (DBUS_TYPE_BOOLEAN == dbus_message_iter_get_arg_type(&args)) {
                dbus_bool_t suspending;
                dbus_message_iter_get_basic(&args, &suspending);

                JNIEnv* env;
                jvm->AttachCurrentThread((void**)&env, NULL);

                if (suspending) {
                    // About to suspend
                    submitSuspendStateToJava(env, SuspendState::WillSuspend);
                } else {
                    // Resumed from suspend
                    submitSuspendStateToJava(env, SuspendState::WillResume);
                    // Optional: Add delay or logic to determine DidResume
                    //submitSuspendStateToJava(SuspendState::DidResume);
                }
            }
        }

        dbus_message_unref(msg);
    }

    dbus_connection_unref(conn);
    return nullptr;
}

void listenToDevice(const std::string& devicePath, int gamepadId) {
    int fd = open(devicePath.c_str(), O_RDONLY | O_NONBLOCK);
    if (fd < 0) return;

    struct libevdev* dev = nullptr;
    if (libevdev_new_from_fd(fd, &dev) < 0) {
        close(fd);
        return;
    }
    JNIEnv* env;
    jvm->AttachCurrentThread((void**)&env, NULL);
    sendGamepadConnectedEvent(env, static_cast<int32_t>(gamepadId));

    struct input_event ev;
    while (true) {
        int rc = libevdev_next_event(dev, LIBEVDEV_READ_FLAG_NORMAL, &ev);
        if (rc == 0) {
            if (ev.type == EV_ABS) {
                float value = ev.value / 32767.0f; // Normalize
                jvm->AttachCurrentThread((void**)&env, NULL);
                sendAxisEvent(env, static_cast<int32_t>(gamepadId), static_cast<int32_t>(ev.code), value);
            }
        }
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
    }

    libevdev_free(dev);
    close(fd);
    jvm->AttachCurrentThread((void**)&env, NULL);
    sendGamepadDisconnectedEvent(env, static_cast<int32_t>(gamepadId));
}

void pollGamepads(JNIEnv* env) {
    std::vector<std::string> devicePaths = {
        "/dev/input/event0",
        "/dev/input/event1",
        // Add more device paths as needed
    };

    int gamepadId = 0;
    for (const auto& path : devicePaths) {
        std::thread(listenToDevice, path, gamepadId++).detach();
    }
}
