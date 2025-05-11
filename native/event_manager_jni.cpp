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
#include <malloc.h>  // for _aligned_malloc on Windows
#include <stddef.h>   // for alignof

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


extern "C" {

    /*
     * Class:     tech_lib_ui_jni_EventManager
     * Method:    pollUiEvent
     * Signature: (J)Ltech/lib/ui/event/UiEvent;
     */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_pollUiEvent(JNIEnv* env, jclass clazz, jlong windowHandler) {
        NativeUiEvent evt;
        bool gotEvent = pollEventFromSystem(reinterpret_cast<void*>(windowHandler), &evt);

        if (!gotEvent) {
          return nullptr;
        }

        return createJavaUiEvent(env, evt);
    }

    jobject createJavaUiEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        switch (nativeEvent.type) {
            case UiEventType::Axis: {
                return createJavaAxisEvent(env, nativeEvent);
            }
            case UiEventType::Char: {
                return createJavaCharEvent(env, nativeEvent);
            }
            case UiEventType::Exit: {
                return createJavaExitEvent(env, nativeEvent);
            }
            case UiEventType::Gamepad: {
                return createJavaGamepadEvent(env, nativeEvent);
            }
            case UiEventType::Key: {
                return createJavaKeyEvent(env, nativeEvent);
            }

            case UiEventType::Mouse: {
                return createJavaMouseEvent(env, nativeEvent);
            }

            case UiEventType::Size: {
                return createJavaSizeEvent(env, nativeEvent);
            }

            case UiEventType::Window: {
                return createJavaWindowEvent(env, nativeEvent);
            }

            case UiEventType::Suspend: {
                return createJavaSuspendEvent(env, nativeEvent);
            }

            case UiEventType::DropFile: {
                return createJavaDropFileEvent(env, nativeEvent);
            }

            default: {
                env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown event exception!");
                return nullptr;
            }
        }
    }

    jobject createJavaAxisEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
       jclass uiEventClass = env->FindClass("tech/lib/ui/event/AxisEvent");
       if (uiEventClass == NULL) {
           env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.AxisEvent!");
           return nullptr;
       }

       jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(Ltech/lib/ui/enu/GamepadAxis;IJJ)V");
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

       return env->NewObject(uiEventClass, ctor, axisEnum, nativeEvent.value, nativeEvent.gamepadHandler, nativeEvent.windowHandler);
    }

    jobject createJavaCharEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/CharEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.CharEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(ICJ)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.CharEvent!");
            return nullptr;
        }
        return env->NewObject(uiEventClass, ctor, nativeEvent.length, static_cast<jchar>(nativeEvent.character), nativeEvent.windowHandler);
    }

    jobject createJavaExitEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/ExitEvent");
        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(J)V");
        return env->NewObject(uiEventClass, ctor, nativeEvent.windowHandler);
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

    jobject createJavaKeyEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/KeyEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.KeyEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(Ltech/lib/ui/enu/KeyEnum;IZJ)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.KeyEvent!");
            return nullptr;
        }

        // Java Key Enum
        jclass keyEnumClass = env->FindClass("tech/lib/ui/enu/KeyEnum");
        if (keyEnumClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.KeyEnum!");
            return nullptr;
        }
        jmethodID valuesMethod = env->GetStaticMethodID(keyEnumClass, "values", "()[Ltech/lib/ui/enu/KeyEnum;");
        if (valuesMethod == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class tech.lib.ui.enu.KeyEnum does not have values method!");
            return nullptr;
        }
        jobjectArray enumArray = static_cast<jobjectArray>(env->CallStaticObjectMethod(keyEnumClass, valuesMethod));
        if (enumArray == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Call values method of class tech.lib.ui.enu.KeyEnum fail!");
            return nullptr;
        }
        jsize ordinal = static_cast<jint>(nativeEvent.key);
        jsize arrayLength = env->GetArrayLength(enumArray);
        if (ordinal >= 0 && ordinal < arrayLength) {
            jobject javaKey = env->GetObjectArrayElement(enumArray, ordinal);
            return env->NewObject(uiEventClass, ctor, javaKey, static_cast<jint>(nativeEvent.modifiers), static_cast<jboolean> (nativeEvent.down), nativeEvent.windowHandler);
        } else {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "ordinal from java out of CPP enum list!");
            return nullptr;
        }
   }

    jobject createJavaMouseEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/MouseEvent");
        if (uiEventClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.event.MouseEvent!");
            return nullptr;
        }

        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(IIILtech/lib/ui/enu/MouseButton;ZZJ)V");
        if (ctor == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Constructor access error for tech.lib.ui.event.MouseEvent!");
            return nullptr;
        }

        // Java MouseButton
        jclass mouseButtonClass = env->FindClass("tech/lib/ui/enu/MouseButton");
        if (mouseButtonClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.MouseButton!");
            return nullptr;
        }
        jmethodID valuesMethod = env->GetStaticMethodID(mouseButtonClass, "values", "()[Ltech/lib/ui/enu/MouseButton;");
        if (valuesMethod == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class tech.lib.ui.enu.MouseButton does not have values method!");
            return nullptr;
        }
        jobjectArray enumArray = static_cast<jobjectArray>(env->CallStaticObjectMethod(mouseButtonClass, valuesMethod));
        if (enumArray == nullptr) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Call values method of class tech.lib.ui.enu.MouseButton fail!");
            return nullptr;
        }
        jsize ordinal = static_cast<jint>(nativeEvent.button);
        jsize arrayLength = env->GetArrayLength(enumArray);
        if (ordinal >= 0 && ordinal < arrayLength) {
            jobject javaMouseButton = env->GetObjectArrayElement(enumArray, ordinal);
            return env->NewObject(uiEventClass, ctor, static_cast<jint> (nativeEvent.mx), static_cast<jint> (nativeEvent.my),
                static_cast<jint> (nativeEvent.mz), javaMouseButton, static_cast<jboolean> (nativeEvent.down), static_cast<jboolean> (nativeEvent.move),
                nativeEvent.windowHandler);
        } else {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "ordinal from java out of CPP enum list!");
            return nullptr;
        }
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

    jobject createJavaWindowEvent(JNIEnv* env, const NativeUiEvent nativeEvent) {
        jclass uiEventClass = env->FindClass("tech/lib/ui/event/WindowEvent");
        jmethodID ctor = env->GetMethodID(uiEventClass, "<init>", "(J)V");
        return env->NewObject(uiEventClass, ctor, nativeEvent.windowHandler);
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
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateAxisEvent(JNIEnv* env, jclass clzz, jobject gamepadAxis, jint value, jlong gamepadHandler, jlong windowHandler) {
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
                type = GamepadAxis::GamepadAxisType::None;
                name = "None";
                break;
        }

        // Create the GamepadAxis object
        GamepadAxis axis(type, name);

        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Axis;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.axis = axis;
        nativeEvent.value = value;
        nativeEvent.gamepadHandler = static_cast<int64_t> (gamepadHandler);

        return createJavaAxisEvent(env, nativeEvent);
    }

   /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateCharEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateCharEvent(JNIEnv* env, jclass clzz, jint length, jchar character, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Char;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.length = length;
        nativeEvent.character = static_cast<int64_t>(character);

        return createJavaCharEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateExitEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateExitEvent(JNIEnv* env, jclass clzz, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Exit;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);
        return createJavaExitEvent(env, nativeEvent);
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
    * Method:    selfCreateWindowEvent
    * Signature: (ICJ)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateWindowEvent(JNIEnv* env, jclass clzz, jlong windowHandler) {
        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Window;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);
        return createJavaWindowEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateMouseEvent
    * Signature: (Ltech/lib/ui/enu/SuspendState;J)Ltech/lib/ui/event/UiEvent;
    */
    JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateMouseEvent(JNIEnv* env, jclass clzz,
        jint mx, jint my, jint mz, jobject button, jboolean down, jboolean move, jlong windowHandler) {
        // Initialize the struct fields
        // Get the class of the Java enum
        jclass stateEnumClass = env->FindClass("tech/lib/ui/enu/MouseButton");
        if (stateEnumClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.MouseButton!");
            return nullptr;
        }

        // Get the ordinal of the Java enum constant (its position in the enum)
        jmethodID ordinalMethod = env->GetMethodID(stateEnumClass, "ordinal", "()I");
        if (ordinalMethod == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Enum tech.lib.ui.enu.MouseButton does not have method ordinal!");
            return nullptr;
        }

        jint ordinal = env->CallIntMethod(button, ordinalMethod);

        // Convert the Java enum ordinal to the corresponding C++ enum value
        MouseButton cppButton;
        switch (ordinal) {
            case 0:
                cppButton = MouseButton::None;
                break;
            case 1:
                cppButton = MouseButton::Left;
                break;
            case 2:
                cppButton = MouseButton::Middle;
                break;
            case 3:
                cppButton = MouseButton::Right;
                break;
            default:
                cppButton = MouseButton::OTHER;
                break;
        }

        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Mouse;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.button = cppButton;
        nativeEvent.mx = static_cast<int32_t>(mx);
        nativeEvent.my = static_cast<int32_t>(my);
        nativeEvent.mz = static_cast<int32_t>(mz);
        nativeEvent.down = static_cast<bool>(down);
        nativeEvent.move = static_cast<bool>(move);

        return createJavaMouseEvent(env, nativeEvent);
    }

    /*
    * Class:     tech_lib_ui_jni_EventManager
    * Method:    selfCreateKeyEvent
    * Signature: (Ltech/lib/ui/enu/SuspendState;J)Ltech/lib/ui/event/UiEvent;
    */
   JNIEXPORT jobject JNICALL Java_tech_lib_ui_jni_EventManager_selfCreateKeyEvent(JNIEnv* env, jclass clzz, jobject keyEnum, jint modifiers, jboolean down, jlong windowHandler) {
        // Initialize the struct fields
        // Get the class of the Java enum KeyEnum
        jclass stateEnumClass = env->FindClass("tech/lib/ui/enu/KeyEnum");
        if (stateEnumClass == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Class not found tech.lib.ui.enu.KeyEnum!");
            return nullptr;
        }

        // Get the ordinal of the Java enum constant (its position in the enum)
        jmethodID ordinalMethod = env->GetMethodID(stateEnumClass, "ordinal", "()I");
        if (ordinalMethod == NULL) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Enum tech.lib.ui.enu.KeyEnum does not have method ordinal!");
            return nullptr;
        }

        jint ordinal = env->CallIntMethod(keyEnum, ordinalMethod);

        // Convert the Java enum ordinal to the corresponding C++ enum value
        KeyEnum cppKey;
        switch (ordinal) {
            case 0: cppKey = KeyEnum::None; break;
            case 1: cppKey = KeyEnum::Esc; break;
            case 2: cppKey = KeyEnum::Return; break;
            case 3: cppKey = KeyEnum::Tab; break;
            case 4: cppKey = KeyEnum::Space; break;
            case 5: cppKey = KeyEnum::Backspace; break;
            case 6: cppKey = KeyEnum::Up; break;
            case 7: cppKey = KeyEnum::Down; break;
            case 8: cppKey = KeyEnum::Left; break;
            case 9: cppKey = KeyEnum::Right; break;
            case 10: cppKey = KeyEnum::Insert; break;
            case 11: cppKey = KeyEnum::Delete; break;
            case 12: cppKey = KeyEnum::Home; break;
            case 13: cppKey = KeyEnum::End; break;
            case 14: cppKey = KeyEnum::PageUp; break;
            case 15: cppKey = KeyEnum::PageDown; break;
            case 16: cppKey = KeyEnum::Print; break;
            case 17: cppKey = KeyEnum::Plus; break;
            case 18: cppKey = KeyEnum::Minus; break;
            case 19: cppKey = KeyEnum::LeftBracket; break;
            case 20: cppKey = KeyEnum::RightBracket; break;
            case 21: cppKey = KeyEnum::Semicolon; break;
            case 22: cppKey = KeyEnum::Quote; break;
            case 23: cppKey = KeyEnum::Comma; break;
            case 24: cppKey = KeyEnum::Period; break;
            case 25: cppKey = KeyEnum::Slash; break;
            case 26: cppKey = KeyEnum::Backslash; break;
            case 27: cppKey = KeyEnum::Tilde; break;
            case 28: cppKey = KeyEnum::F1; break;
            case 29: cppKey = KeyEnum::F2; break;
            case 30: cppKey = KeyEnum::F3; break;
            case 31: cppKey = KeyEnum::F4; break;
            case 32: cppKey = KeyEnum::F5; break;
            case 33: cppKey = KeyEnum::F6; break;
            case 34: cppKey = KeyEnum::F7; break;
            case 35: cppKey = KeyEnum::F8; break;
            case 36: cppKey = KeyEnum::F9; break;
            case 37: cppKey = KeyEnum::F10; break;
            case 38: cppKey = KeyEnum::F11; break;
            case 39: cppKey = KeyEnum::F12; break;
            case 40: cppKey = KeyEnum::NumPad0; break;
            case 41: cppKey = KeyEnum::NumPad1; break;
            case 42: cppKey = KeyEnum::NumPad2; break;
            case 43: cppKey = KeyEnum::NumPad3; break;
            case 44: cppKey = KeyEnum::NumPad4; break;
            case 45: cppKey = KeyEnum::NumPad5; break;
            case 46: cppKey = KeyEnum::NumPad6; break;
            case 47: cppKey = KeyEnum::NumPad7; break;
            case 48: cppKey = KeyEnum::NumPad8; break;
            case 49: cppKey = KeyEnum::NumPad9; break;
            case 50: cppKey = KeyEnum::Key0; break;
            case 51: cppKey = KeyEnum::Key1; break;
            case 52: cppKey = KeyEnum::Key2; break;
            case 53: cppKey = KeyEnum::Key3; break;
            case 54: cppKey = KeyEnum::Key4; break;
            case 55: cppKey = KeyEnum::Key5; break;
            case 56: cppKey = KeyEnum::Key6; break;
            case 57: cppKey = KeyEnum::Key7; break;
            case 58: cppKey = KeyEnum::Key8; break;
            case 59: cppKey = KeyEnum::Key9; break;
            case 60: cppKey = KeyEnum::KeyA; break;
            case 61: cppKey = KeyEnum::KeyB; break;
            case 62: cppKey = KeyEnum::KeyC; break;
            case 63: cppKey = KeyEnum::KeyD; break;
            case 64: cppKey = KeyEnum::KeyE; break;
            case 65: cppKey = KeyEnum::KeyF; break;
            case 66: cppKey = KeyEnum::KeyG; break;
            case 67: cppKey = KeyEnum::KeyH; break;
            case 68: cppKey = KeyEnum::KeyI; break;
            case 69: cppKey = KeyEnum::KeyJ; break;
            case 70: cppKey = KeyEnum::KeyK; break;
            case 71: cppKey = KeyEnum::KeyL; break;
            case 72: cppKey = KeyEnum::KeyM; break;
            case 73: cppKey = KeyEnum::KeyN; break;
            case 74: cppKey = KeyEnum::KeyO; break;
            case 75: cppKey = KeyEnum::KeyP; break;
            case 76: cppKey = KeyEnum::KeyQ; break;
            case 77: cppKey = KeyEnum::KeyR; break;
            case 78: cppKey = KeyEnum::KeyS; break;
            case 79: cppKey = KeyEnum::KeyT; break;
            case 80: cppKey = KeyEnum::KeyU; break;
            case 81: cppKey = KeyEnum::KeyV; break;
            case 82: cppKey = KeyEnum::KeyW; break;
            case 83: cppKey = KeyEnum::KeyX; break;
            case 84: cppKey = KeyEnum::KeyY; break;
            case 85: cppKey = KeyEnum::KeyZ; break;
            case 86: cppKey = KeyEnum::GamepadA; break;
            case 87: cppKey = KeyEnum::GamepadB; break;
            case 88: cppKey = KeyEnum::GamepadX; break;
            case 89: cppKey = KeyEnum::GamepadY; break;
            case 90: cppKey = KeyEnum::GamepadThumbL; break;
            case 91: cppKey = KeyEnum::GamepadThumbR; break;
            case 92: cppKey = KeyEnum::GamepadShoulderL; break;
            case 93: cppKey = KeyEnum::GamepadShoulderR; break;
            case 94: cppKey = KeyEnum::GamepadUp; break;
            case 95: cppKey = KeyEnum::GamepadDown; break;
            case 96: cppKey = KeyEnum::GamepadLeft; break;
            case 97: cppKey = KeyEnum::GamepadRight; break;
            case 98: cppKey = KeyEnum::GamepadBack; break;
            case 99: cppKey = KeyEnum::GamepadStart; break;
            case 100: cppKey = KeyEnum::GamepadGuide; break;
            default:
                env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknow key value!");
                return nullptr;
        }

        NativeUiEvent nativeEvent;
        nativeEvent.type = UiEventType::Key;
        nativeEvent.windowHandler = static_cast<int64_t> (windowHandler);

        nativeEvent.key = cppKey;
        nativeEvent.modifiers = static_cast<int32_t>(modifiers);
        nativeEvent.down = static_cast<bool>(down);

        return createJavaKeyEvent(env, nativeEvent);
    }
}