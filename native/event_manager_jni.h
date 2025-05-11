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
        LeftX, LeftY, LeftZ, RightX, RightY, RightZ, None
    };

    GamepadAxisType type;
    std::string name;

    GamepadAxis(GamepadAxisType t, const std::string& n) : type(t), name(n) {}
    GamepadAxis() : type(GamepadAxisType::LeftX), name("LeftX") {}
};

enum class KeyEnum {
    None,
    Esc,
    Return,
    Tab,
    Space,
    Backspace,
    Up,
    Down,
    Left,
    Right,
    Insert,
    Delete,
    Home,
    End,
    PageUp,
    PageDown,
    Print,
    Plus,
    Minus,
    LeftBracket,
    RightBracket,
    Semicolon,
    Quote,
    Comma,
    Period,
    Slash,
    Backslash,
    Tilde,
    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12,
    NumPad0,
    NumPad1,
    NumPad2,
    NumPad3,
    NumPad4,
    NumPad5,
    NumPad6,
    NumPad7,
    NumPad8,
    NumPad9,
    Key0,
    Key1,
    Key2,
    Key3,
    Key4,
    Key5,
    Key6,
    Key7,
    Key8,
    Key9,
    KeyA,
    KeyB,
    KeyC,
    KeyD,
    KeyE,
    KeyF,
    KeyG,
    KeyH,
    KeyI,
    KeyJ,
    KeyK,
    KeyL,
    KeyM,
    KeyN,
    KeyO,
    KeyP,
    KeyQ,
    KeyR,
    KeyS,
    KeyT,
    KeyU,
    KeyV,
    KeyW,
    KeyX,
    KeyY,
    KeyZ,

    GamepadA,
    GamepadB,
    GamepadX,
    GamepadY,
    GamepadThumbL,
    GamepadThumbR,
    GamepadShoulderL,
    GamepadShoulderR,
    GamepadUp,
    GamepadDown,
    GamepadLeft,
    GamepadRight,
    GamepadBack,
    GamepadStart,
    GamepadGuide
};

enum class SuspendState {
    WillSuspend,
    DidSuspend,
    WillResume,
    DidResume
};

enum class MouseButton {
    None, Left, Middle, Right, OTHER
};

struct NativeUiEvent {
    UiEventType type;
    int64_t windowHandler;

    // Axis Event
    GamepadAxis axis;
    int32_t value;
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
    KeyEnum key;
    int32_t modifiers;
    bool down;

    // Mouse Event
    int32_t mx, my, mz;
    MouseButton button;
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
          axis(GamepadAxis(GamepadAxis::GamepadAxisType::LeftX, "LeftX")), value(0), gamepadHandler(0),
          // Char Event
          length(0), character(0),
          // Drop File Event
          filePath(""),
          //Gamepad Event
          connected(false),
          // KeyEvent
          key(KeyEnum::None), modifiers(0), down(false),
          // Mouse Event
          mx(0), my(0), mz(0), button(MouseButton::None), move(false),
          // Size Event
          width(0), height(0),
          // Suspend Event
          suspendState(SuspendState::DidResume)
    {}
};

#ifdef __cplusplus
extern "C" {
#endif

    jobject createJavaUiEvent(JNIEnv* env, const NativeUiEvent nativeEvent);

    bool pollEventFromSystem(void* windowHandler, NativeUiEvent* evt);

//    #ifdef __APPLE__
//        // Forward declare the Objective-C event polling function
//        NativeUiEvent pollEventFromSystem(void* windowHandler);
//    #endif

//    #ifdef __linux__
//        bgfx::PlatformData setupLinuxPlatformData(JNIEnv* env, jobject canvas);
//    #endif

    jobject createJavaAxisEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaCharEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaExitEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaGamepadEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaKeyEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaMouseEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaSizeEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaWindowEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaSuspendEvent(JNIEnv* env, const NativeUiEvent nativeEvent);
    jobject createJavaDropFileEvent(JNIEnv* env, const NativeUiEvent nativeEvent);

#ifdef __cplusplus
}
#endif

#endif /* EVENTMANAGER_H */
