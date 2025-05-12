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

JavaVM* jvm = nullptr;

extern "C" {

    jobject createJavaAxisEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
       jclass uiEventClass = env->FindClass("tech/lib/ui/event/AxisEvent");
       if (uiEventClass == NULL) {
           env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.AxisEvent!");
           return nullptr;
       }

       jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(Ltech/lib/ui/enu/GamepadAxis;FJJ)V");
       if (ctor == NULL) {
           env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.AxisEvent!");
           return nullptr;
       }

       // GamepadAxis must be resolved from int to Java enum object
       jclass axisEnumClass = env->FindClass("tech/lib/ui/enu/GamepadAxis");
       if (uiEventClass == NULL) {
          env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.GamepadAxis!");
          return nullptr;
       }

       jfieldID valField = env->GetStaticFieldID(axisEnumClass, nativeEvent.axis.name.c_str(), "Ltech/lib/ui/enu/GamepadAxis;");
       if (valField == NULL) {
         env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot get enum field name of tech.lib.ui.enu.GamepadAxis!");
         return nullptr;
       }
       // Get the static field (enum constant)
       jobject axisEnum = env->GetStaticObjectField(axisEnumClass, valField);
       if (valField == NULL) {
         env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot get enum instance of tech.lib.ui.enu.GamepadAxis!");
         return nullptr;
       }

       return env->NewObject(uiEventClass, ctor, axisEnum, static_cast<jfloat>(nativeEvent.axisValue), nativeEvent.gamepadHandler, nativeEvent.windowHandler);
    }

    jobject createJavaGamepadEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/GamepadEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.GamepadEvent!");
            return nullptr;
        }
 
        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(ZJJ)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.GamepadEvent!");
            return nullptr;
        }
 
        return env->NewObject(uiEventClass, ctor, nativeEvent.connected, nativeEvent.gamepadHandler, nativeEvent.windowHandler);
    }

    jobject createJavaSizeEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/SizeEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.SizeEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(IIJ)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.SizeEvent!");
            return nullptr;
        }

        return env->NewObject(uiEventClass, ctor, static_cast<jint> (nativeEvent.width), static_cast<jint> (nativeEvent.height), nativeEvent.windowHandler);
    }

    jobject createJavaSuspendEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/SuspendEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.SuspendEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(Ltech/lib/ui/enu/SuspendState;J)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.SuspendEvent!");
            return nullptr;
        }

        // Java SuspendState
        jclass suspendStateClass = env->FindClass("tech/lib/ui/enu/SuspendState");
        if (suspendStateClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.SuspendState!");
            return nullptr;
        }
        jmethodID valuesMethod = env->GetStaticMethodID(suspendStateClass, "values", "()[Ltech/lib/ui/enu/SuspendState;");
        if (valuesMethod == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class tech.lib.ui.enu.SuspendState does not have values method!");
            return nullptr;
        }
        jobjectArray enumArray = static_cast<jobjectArray>(env->CallStaticObjectMethod(suspendStateClass, valuesMethod));
        if (enumArray == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Call values method of class tech.lib.ui.enu.SuspendState fail!");
            return nullptr;
        }
        jsize ordinal = static_cast<jint>(nativeEvent.suspendState);
        jsize arrayLength = env->GetArrayLength(enumArray);
        if (ordinal >= 0 && ordinal < arrayLength) {
            jobject javaSuspenState = env->GetObjectArrayElement(enumArray, ordinal);
            return env->NewObject(uiEventClass, ctor, javaSuspenState, nativeEvent.windowHandler);
        } else {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "ordinal from java out of CPP enum list!");
            return nullptr;
        }
    }

    jobject createJavaDropFileEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/DropFileEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.DropFileEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(Ljava/lang/String;J)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.DropFileEvent!");
            return nullptr;
        }

        return env->NewObject(uiEventClass, ctor, env->NewStringUTF(nativeEvent.filePath), nativeEvent.windowHandler);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateAxisEvent
    * Signature: (Ltech/lib/ui/enu/GamepadAxis;IJJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateAxisEvent(JNIEnv* env, jclass clzz, jobject gamepadAxis, jfloat value, jlong gamepadHandler, jlong windowHandler) {
        // Initialize the struct fields
        // Get the class of the Java enum GamepadAxis
        jclass axisEnumClass = env->FindClass("tech/lib/ui/enu/GamepadAxis");
        if (axisEnumClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.GamepadAxis!");
            return nullptr;
        }

        // Get the ordinal of the Java enum constant (its position in the enum)
        jmethodID ordinalMethod = env->GetMethodID(axisEnumClass, "ordinal", "()I");
        if (ordinalMethod == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Enum tech.lib.ui.enu.GamepadAxis does not have method ordinal!");
            return nullptr;
        }

        jint ordinal = env->CallIntMethod(gamepadAxis, ordinalMethod);

        // Convert the Java enum ordinal to the corresponding C++ enum value
        GamepadAxis::GamepadAxisType type;
        std::string name;
        switch (ordinal) {
            case 0:
                type = GamepadAxis::GamepadAxisType::LeftX;
                name = "LeftX";
                break;
            case 1:
                type = GamepadAxis::GamepadAxisType::LeftY;
                name = "LeftY";
                break;
            case 2:
                type = GamepadAxis::GamepadAxisType::LeftZ;
                name = "LeftZ";
                break;
            case 3:
                type = GamepadAxis::GamepadAxisType::RightX;
                name = "RightX";
                break;
            case 4:
                type = GamepadAxis::GamepadAxisType::RightY;
                name = "RightY";
                break;
            case 5:
                type = GamepadAxis::GamepadAxisType::RightZ;
                name = "RightZ";
                break;
            default:
                type = GamepadAxis::GamepadAxisType::AxisNone;
                name = "AxisNone";
                break;
        }

        // Create the GamepadAxis object
        GamepadAxis axis(type, name);

        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Axis;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.axis = axis;
        nativeEvent.axisValue = static_cast<float> (value);
        nativeEvent.gamepadHandler = static_cast<int64_t> (gamepadHandler);

        return createJavaAxisEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateGamepadEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateGamepadEvent(JNIEnv* env, jclass clzz, jboolean connected, jlong gamepadHandler, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Gamepad;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.connected = static_cast<bool> (connected);
        nativeEvent.gamepadHandler = static_cast<int64_t> (gamepadHandler);

        return createJavaGamepadEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateDropFileEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateDropFileEvent(JNIEnv* env, jclass clzz, jstring filePath, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::DropFile;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.filePath = env->GetStringUTFChars(filePath, nullptr);

        return createJavaDropFileEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateCharEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
   JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateSizeEvent(JNIEnv* env, jclass clzz, jint width, jint height, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Size;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.width = static_cast<int32_t>(width);
        nativeEvent.height = static_cast<int32_t>(height);

        return createJavaSizeEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateSuspendEvent
    * Signature: (Ltech/lib/ui/enu/SuspendState;J)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateSuspendEvent(JNIEnv* env, jclass clzz, jobject state, jlong windowHandler) {
        // Initialize the struct fields
        // Get the class of the Java enum GamepadAxis
        jclass stateEnumClass = env->FindClass("tech/lib/ui/enu/SuspendState");
        if (stateEnumClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.SuspendState!");
            return nullptr;
        }

        // Get the ordinal of the Java enum constant (its position in the enum)
        jmethodID ordinalMethod = env->GetMethodID(stateEnumClass, "ordinal", "()I");
        if (ordinalMethod == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Enum tech.lib.ui.enu.SuspendState does not have method ordinal!");
            return nullptr;
        }

        jint ordinal = env->CallIntMethod(state, ordinalMethod);

        // Convert the Java enum ordinal to the corresponding C++ enum value
        SuspendState cppState;
        switch (ordinal) {
            case 0:
                cppState = SuspendState::WillSuspend;
                break;
            case 1:
                cppState = SuspendState::DidSuspend;
                break;
            case 2:
                cppState = SuspendState::WillResume;
                break;
            case 3:
                cppState = SuspendState::DidResume;
                break;
            default:
                env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown tech.lib.ui.enu.SuspendState!");
                break;
        }

        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Suspend;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.suspendState = cppState;

        return createJavaSuspendEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    registerForNativeSuspendEvents
    * Signature: (J)V
    */
    JNIEXPORT void JNICALL Java_tech_lib_ui_jni_EventManager_registerForNativeSuspendEvents(JNIEnv* env, jclass clzz, jlong windowHandler) {
        if (!jvm) env->GetJavaVM(&jvm);

        #ifdef _WIN32
            HWND hwnd = (HWND)(intptr_t)windowHandler;
            WNDPROC original = (WNDPROC) SetWindowLongPtr(hwnd, GWLP_WNDPROC, (LONG_PTR)CustomWndProc);
            wndProcMap[hwnd] = original;
        #elif __APPLE__
            registerForSuspendEventsOnMac(env, (void*)(intptr_t) windowHandler);
        #elif __linux__
            pthread_t thread;
            pthread_create(&thread, NULL, listen_for_suspend, NULL);
            pthread_detach(thread);
        #endif
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    unregisterForNativeSuspendEvents
    * Signature: (J)V
    */
    JNIEXPORT void JNICALL Java_tech_lib_ui_jni_EventManager_unregisterForNativeSuspendEvents(JNIEnv* env, jclass clzz, jlong windowHandler) {
        #ifdef _WIN32
            HWND hwnd = (HWND)(intptr_t)windowHandler;
            auto it = wndProcMap.find(hwnd);
            if (it != wndProcMap.end()) {
                SetWindowLongPtr(hwnd, GWLP_WNDPROC, (LONG_PTR)it->second);
                wndProcMap.erase(it);
            }
        #elif __APPLE__
            // Do nothing for now
            jniLog(env, "DEBUG", "event_manager_jni.cpp#unregisterForNativeSuspendEvents", "There's no unregister for specific window on MacOS! It will be handled in Java");
        #elif __linux__
            jniLog(env, "DEBUG", "event_manager_jni.cpp#unregisterForNativeSuspendEvents", "There's no unregister for specific window on Linux! It will be handled in Java");
        #endif
    }

    void submitSuspendStateToJava(JNIEnv* env, SuspendState suspendState) {
        // 1. Get the class of the Java EventManager
        jclass managerClass = env->FindClass("tech/lib/ui/jni/EventManager");
        if (managerClass == NULL) {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Class not found tech.lib.ui.jni.EventManager!");
            return;
        }

        // 2. Get the method ID -> Signature: long, UiEvent
        jmethodID methodID = env->GetStaticMethodID(managerClass, "pushSuspendState", "(Ltech/lib/ui/enu/tech.lib.ui.enu.SuspendState;)V");
        if (methodID == NULL) {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Class not found tech.lib.ui.jni.EventManager#pushSuspendState(SuspendState)!");
            return;
        }

        // 3. Create jobject SuspendState
        jclass suspendStateClass = env->FindClass("tech/lib/ui/enu/SuspendState");
        if (suspendStateClass == NULL) {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Class not found tech.lib.ui.enu.SuspendState!");
            return;
        }
        jmethodID valuesMethod = env->GetStaticMethodID(suspendStateClass, "values", "()[Ltech/lib/ui/enu/SuspendState;");
        if (valuesMethod == nullptr) {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Class tech.lib.ui.enu.SuspendState does not have values method!");
            return;
        }
        jobjectArray enumArray = static_cast<jobjectArray>(env->CallStaticObjectMethod(suspendStateClass, valuesMethod));
        if (enumArray == nullptr) {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Call values method of class tech.lib.ui.enu.SuspendState fail!");
            return;
        }
        jsize ordinal = static_cast<jint>(suspendState);
        jsize arrayLength = env->GetArrayLength(enumArray);
        if (ordinal >= 0 && ordinal < arrayLength) {
            jobject javaSuspenState = env->GetObjectArrayElement(enumArray, ordinal);
            // 4. Call the static method
            env->CallStaticVoidMethod(managerClass, methodID, javaSuspenState);
        } else {
            jniLog(env, "ERROR", "event_manager_jni.cpp#submitSuspendStateToJava", "❌ Ordinal from java out of CPP enum list!");
            return;
        }
    }
}