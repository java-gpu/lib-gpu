#ifndef EVENTMANAGER_H
#define EVENTMANAGER_H

#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

#include "jni_utils.h"
#include "tech_lib_bgfx_jni_Bgfx.h"

#include <bgfx/bgfx.h>
#include <bgfx/platform.h>
#include <exception>
#include <cstdio>
#include <sstream>

#include <unordered_map>
#include <map>

enum class UiEventType {
    Axis,
    Char,
    Exit,
    Gamepad,
    Key,
    Mouse,
    Size,
    Window,
    Suspend,
    DropFile
};

struct GamepadAxis {
    enum class GamepadAxisType {
        LeftX, LeftY, LeftZ, RightX, RightY, RightZ, AxisNone
    };

    GamepadAxisType type;
    std::string name;

    GamepadAxis(GamepadAxisType t, const std::string& n) : type(t), name(n) {}
    GamepadAxis() : type(GamepadAxisType::LeftX), name("LeftX") {}
};

enum class SuspendState {
    WillSuspend,
    DidSuspend,
    WillResume,
    DidResume
};

struct NativeUiEvent {
    UiEventType type;
    int64_t windowHandler;

    // Axis Event
    GamepadAxis axis;
    float axisValue;
    int64_t gamepadHandler;

    // Char Event
    int32_t length;
    uint16_t character;

    // Drop File Event
    const char* filePath;

    //Gamepad Event
    bool connected;
    // Reuse gamepadHandler in axis event

    // Key Event
    int32_t key;
    int32_t modifiers;
    bool down;

    // Mouse Event
    int32_t mx, my, mz;
    int32_t button;
    bool move;

    // Size Event
    int32_t width;
    int32_t height;

    // Suspend Event
    SuspendState suspendState;

    // Constructor to initialize empty event (using default values)
    NativeUiEvent()
        : type(UiEventType::Window), windowHandler(0),
          // Axis Event
          axis(GamepadAxis(GamepadAxis::GamepadAxisType::LeftX, "LeftX")), axisValue(0), gamepadHandler(0),
          // Char Event
          length(0), character(0),
          // Drop File Event
          filePath(""),
          //Gamepad Event
          connected(false),
          // KeyEvent
          key(0), modifiers(0), down(false),
          // Mouse Event
          mx(0), my(0), mz(0), button(0), move(false),
          // Size Event
          width(0), height(0),
          // Suspend Event
          suspendState(SuspendState::DidResume)
    {}
};


// Declare JavaVM pointer as extern
extern JavaVM* jvm; // Declaration only, no definition

#ifdef __cplusplus
extern "C" {
#endif

    jobject createJavaUiEvent(JNIEnv* env, const NativeUiEvent nativeEvent);

    #ifdef __APPLE__
        void registerForSuspendEventsOnMac(JNIEnv* env, void* windowHandler);
    #endif

    #ifdef _WIN32
        extern std::map<HWND, WNDPROC> wndProcMap;
        LRESULT CALLBACK CustomWndProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
    #endif

    #ifdef __linux__
        void* listen_for_suspend(void* arg);
    #endif

    jobject createJavaAxisEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaGamepadEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaSizeEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaSuspendEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaDropFileEvent(JNIEnv* env, const NativeUiEvent nativeEvent);

    void submitSuspendStateToJava(JNIEnv* env, SuspendState suspendState);

    void pollGamepads(JNIEnv* env);

    void sendAxisEvent(JNIEnv* env, int32_t gamepadId, int32_t axisId, float value);
    void sendGamepadConnectedEvent(JNIEnv* env, int32_t gamepadId);
    void sendGamepadDisconnectedEvent(JNIEnv* env, int32_t gamepadId);

#ifdef __cplusplus
}
#endif

#endif /* EVENTMANAGER_H */
