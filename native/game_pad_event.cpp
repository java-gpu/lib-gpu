#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

#include "jni_utils.h"
#include "tech_lib_ui_jni_EventManager.h"
#include "event_manager_jni.h"

#include <bgfx/bgfx.h>
#include <bgfx/platform.h>
#include <exception>
#include <cstdio>
#include <sstream>
#include <fstream>         // <-- required
#include <cstring>
#include <stdlib.h>
#include <stddef.h>   // for alignof

#ifdef _WIN32
#ifndef NOMINMAX
#define NOMINMAX
#endif
#include <windows.h>
// for _aligned_malloc on Windows
#include <malloc.h>
#endif

#ifdef __linux__
#include <pthread.h>
#include <dbus/dbus.h>
#endif

/**
* JNI Type Codes Summary (for quick reference):
  JNI Code ->	Java Type
  Z	boolean
  B	byte
  C	char
  S	short
  I	int
  J	long
  F	float
  D	double
  V	void
  L<classname>;	Object (class reference) which replace dot by slash
  [<type>	Array (e.g., [I is int[])
  (Ltech/lib/ui/event/KeyEnum;IZJ)V => input: (Object "tech/lib/ui/event/KeyEnum", int, boolean, long) and return void
**/

void sendAxisEvent(JNIEnv* env, int32_t gamepadId, int32_t axisId, float value) {
    jclass managerClass = env->FindClass("tech/lib/ui/input/GamepadEventManager");
    if (managerClass == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.jni.GamepadEventManager!");
        return;
    }

    jmethodID onAxisEventMethod = env->GetStaticMethodID(managerClass, "onAxisEvent", "(IIF)V");
    if (onAxisEventMethod == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot access method onAxisEvent for tech.lib.ui.event.GamepadEventManager!");
        return;
    }
    env->CallStaticObjectMethod(managerClass, onAxisEventMethod, static_cast<jint>(gamepadId), static_cast<jint>(axisId), static_cast<jfloat>(value));
}

void sendGamepadConnectedEvent(JNIEnv* env, int32_t gamepadId) {
    jclass managerClass = env->FindClass("tech/lib/ui/input/GamepadEventManager");
    if (managerClass == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.jni.GamepadEventManager!");
        return;
    }

    jmethodID onGamepadConnectedMethod = env->GetStaticMethodID(managerClass, "onGamepadConnected", "(I)V");
    if (onGamepadConnectedMethod == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot access method onGamepadConnected for tech.lib.ui.event.GamepadEventManager!");
        return;
    }
    env->CallStaticObjectMethod(managerClass, onGamepadConnectedMethod, static_cast<jint>(gamepadId));
}

void sendGamepadDisconnectedEvent(JNIEnv* env, int32_t gamepadId) {
    jclass managerClass = env->FindClass("tech/lib/ui/input/GamepadEventManager");
    if (managerClass == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.jni.GamepadEventManager!");
        return;
    }

    jmethodID onGamepadDisconnectedMethod = env->GetStaticMethodID(managerClass, "onGamepadDisconnected", "(I)V");
    if (onGamepadDisconnectedMethod == NULL) {
        env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot access method onGamepadDisconnected for tech.lib.ui.event.GamepadEventManager!");
        return;
    }
    env->CallStaticObjectMethod(managerClass, onGamepadDisconnectedMethod, static_cast<jint>(gamepadId));
}