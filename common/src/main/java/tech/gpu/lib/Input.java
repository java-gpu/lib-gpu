/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <br/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <br/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib;

import tech.gpu.lib.input.NativeInputConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Interface to the input facilities. This allows polling the state of the keyboard, the touch screen and the accelerometer. On
 * some backends (desktop, gwt, etc) the touch screen is replaced by mouse input. The accelerometer is of course not available on
 * all backends.
 * </p>
 *
 * <p>
 * Instead of polling for events, one can process all input events with an {@link InputProcessor}. You can set the InputProcessor
 * via the {@link #setInputProcessor(InputProcessor)} method. It will be called before the {@link ApplicationListener#render()}
 * method in each frame.
 * </p>
 *
 * <p>
 * Keyboard keys are translated to the constants in {@link Keys} transparently on all systems. Do not use system specific key
 * constants.
 * </p>
 *
 * <p>
 * The class also offers methods to use (and test for the presence of) other input systems like vibration, compass, on-screen
 * keyboards, and cursor capture. Support for simple input dialogs is also provided.
 * </p>
 *
 * @author mzechner
 */
public interface Input {
    /**
     * Callback interface for {@link Input#getTextInput(TextInputListener, String, String, String)}
     *
     * @author mzechner
     */
    interface TextInputListener {
        void input(String text);

        void canceled();
    }

    /**
     * Mouse buttons.
     *
     * @author mzechner
     */
    class Buttons {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int MIDDLE = 2;
        public static final int BACK = 3;
        public static final int FORWARD = 4;
    }

    /**
     * Keys.
     *
     * @author mzechner
     */
    class Keys {
        public static final int ANY_KEY = -1;
        static final int NUM_0 = 7;
        static final int NUM_1 = 8;
        static final int NUM_2 = 9;
        static final int NUM_3 = 10;
        static final int NUM_4 = 11;
        static final int NUM_5 = 12;
        static final int NUM_6 = 13;
        static final int NUM_7 = 14;
        static final int NUM_8 = 15;
        static final int NUM_9 = 16;
        static final int A = 29;
        static final int ALT_LEFT = 57;
        static final int ALT_RIGHT = 58;
        static final int APOSTROPHE = 75;
        static final int AT = 77;
        static final int B = 30;
        static final int BACK = 4;
        static final int BACKSLASH = 73;
        static final int C = 31;
        static final int CALL = 5;
        static final int CAMERA = 27;
        static final int CAPS_LOCK = 115;
        static final int CLEAR = 28;
        static final int COMMA = 55;
        static final int D = 32;
        static final int DEL = 67;
        static final int BACKSPACE = 67;
        static final int FORWARD_DEL = 112;
        static final int DPAD_CENTER = 23;
        static final int DPAD_DOWN = 20;
        static final int DPAD_LEFT = 21;
        static final int DPAD_RIGHT = 22;
        static final int DPAD_UP = 19;
        static final int CENTER = 23;
        static final int DOWN = 20;
        static final int LEFT = 21;
        static final int RIGHT = 22;
        static final int UP = 19;
        static final int E = 33;
        static final int ENDCALL = 6;
        static final int ENTER = 66;
        static final int ENVELOPE = 65;
        static final int EQUALS = 70;
        static final int EXPLORER = 64;
        static final int F = 34;
        static final int FOCUS = 80;
        static final int G = 35;
        static final int GRAVE = 68;
        static final int H = 36;
        static final int HEADSETHOOK = 79;
        static final int HOME = 3;
        static final int I = 37;
        static final int J = 38;
        static final int K = 39;
        static final int L = 40;
        static final int LEFT_BRACKET = 71;
        static final int M = 41;
        static final int MEDIA_FAST_FORWARD = 90;
        static final int MEDIA_NEXT = 87;
        static final int MEDIA_PLAY_PAUSE = 85;
        static final int MEDIA_PREVIOUS = 88;
        static final int MEDIA_REWIND = 89;
        static final int MEDIA_STOP = 86;
        static final int MENU = 82;
        static final int MINUS = 69;
        static final int MUTE = 91;
        static final int N = 42;
        static final int NOTIFICATION = 83;
        static final int NUM = 78;
        static final int O = 43;
        static final int P = 44;
        static final int PAUSE = 121; // aka break
        static final int PERIOD = 56;
        static final int PLUS = 81;
        static final int POUND = 18;
        static final int POWER = 26;
        static final int PRINT_SCREEN = 120; // aka SYSRQ
        static final int Q = 45;
        static final int R = 46;
        static final int RIGHT_BRACKET = 72;
        static final int S = 47;
        static final int SCROLL_LOCK = 116;
        static final int SEARCH = 84;
        static final int SEMICOLON = 74;
        static final int SHIFT_LEFT = 59;
        static final int SHIFT_RIGHT = 60;
        static final int SLASH = 76;
        static final int SOFT_LEFT = 1;
        static final int SOFT_RIGHT = 2;
        static final int SPACE = 62;
        static final int STAR = 17;
        static final int SYM = 63; // on MacOS, this is Command (⌘)
        static final int T = 48;
        static final int TAB = 61;
        static final int U = 49;
        static final int UNKNOWN = 0;
        static final int V = 50;
        static final int VOLUME_DOWN = 25;
        static final int VOLUME_UP = 24;
        static final int W = 51;
        static final int X = 52;
        static final int Y = 53;
        static final int Z = 54;
        static final int META_ALT_LEFT_ON = 16;
        static final int META_ALT_ON = 2;
        static final int META_ALT_RIGHT_ON = 32;
        static final int META_SHIFT_LEFT_ON = 64;
        static final int META_SHIFT_ON = 1;
        static final int META_SHIFT_RIGHT_ON = 128;
        static final int META_SYM_ON = 4;
        static final int CONTROL_LEFT = 129;
        static final int CONTROL_RIGHT = 130;
        static final int ESCAPE = 111;
        static final int END = 123;
        static final int INSERT = 124;
        static final int PAGE_UP = 92;
        static final int PAGE_DOWN = 93;
        static final int PICTSYMBOLS = 94;
        static final int SWITCH_CHARSET = 95;
        static final int BUTTON_CIRCLE = 255;
        static final int BUTTON_A = 96;
        static final int BUTTON_B = 97;
        static final int BUTTON_C = 98;
        static final int BUTTON_X = 99;
        static final int BUTTON_Y = 100;
        static final int BUTTON_Z = 101;
        static final int BUTTON_L1 = 102;
        static final int BUTTON_R1 = 103;
        static final int BUTTON_L2 = 104;
        static final int BUTTON_R2 = 105;
        static final int BUTTON_THUMBL = 106;
        static final int BUTTON_THUMBR = 107;
        static final int BUTTON_START = 108;
        static final int BUTTON_SELECT = 109;
        static final int BUTTON_MODE = 110;

        static final int NUMPAD_0 = 144;
        static final int NUMPAD_1 = 145;
        static final int NUMPAD_2 = 146;
        static final int NUMPAD_3 = 147;
        static final int NUMPAD_4 = 148;
        static final int NUMPAD_5 = 149;
        static final int NUMPAD_6 = 150;
        static final int NUMPAD_7 = 151;
        static final int NUMPAD_8 = 152;
        static final int NUMPAD_9 = 153;

        static final int NUMPAD_DIVIDE = 154;
        static final int NUMPAD_MULTIPLY = 155;
        static final int NUMPAD_SUBTRACT = 156;
        static final int NUMPAD_ADD = 157;
        static final int NUMPAD_DOT = 158;
        static final int NUMPAD_COMMA = 159;
        static final int NUMPAD_ENTER = 160;
        static final int NUMPAD_EQUALS = 161;
        static final int NUMPAD_LEFT_PAREN = 162;
        static final int NUMPAD_RIGHT_PAREN = 163;
        static final int NUM_LOCK = 143;

// static final int BACKTICK = 0;
// static final int TILDE = 0;
// static final int UNDERSCORE = 0;
// static final int DOT = 0;
// static final int BREAK = 0;
// static final int PIPE = 0;
// static final int EXCLAMATION = 0;
// static final int QUESTIONMARK = 0;

        // ` | VK_BACKTICK
// ~ | VK_TILDE
// : | VK_COLON
// _ | VK_UNDERSCORE
// . | VK_DOT
// (break) | VK_BREAK
// | | VK_PIPE
// ! | VK_EXCLAMATION
// ? | VK_QUESTION
        static final int COLON = 243;
        static final int F1 = 131;
        static final int F2 = 132;
        static final int F3 = 133;
        static final int F4 = 134;
        static final int F5 = 135;
        static final int F6 = 136;
        static final int F7 = 137;
        static final int F8 = 138;
        static final int F9 = 139;
        static final int F10 = 140;
        static final int F11 = 141;
        static final int F12 = 142;
        static final int F13 = 183;
        static final int F14 = 184;
        static final int F15 = 185;
        static final int F16 = 186;
        static final int F17 = 187;
        static final int F18 = 188;
        static final int F19 = 189;
        static final int F20 = 190;
        static final int F21 = 191;
        static final int F22 = 192;
        static final int F23 = 193;
        static final int F24 = 194;

        static final int MAX_KEYCODE = 255;

        /**
         * @return a human readable representation of the keycode. The returned value can be used in
         * {@link Keys#valueOf(String)}
         */
        static String toString(int keycode) {
            if (keycode < 0) throw new IllegalArgumentException("keycode cannot be negative, keycode: " + keycode);
            if (keycode > MAX_KEYCODE)
                throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + keycode);
            return switch (keycode) {
                // META* variables should not be used with this method.
                case UNKNOWN -> "Unknown";
                case SOFT_LEFT -> "Soft Left";
                case SOFT_RIGHT -> "Soft Right";
                case HOME -> "Home";
                case BACK -> "Back";
                case CALL -> "Call";
                case ENDCALL -> "End Call";
                case NUM_0 -> "0";
                case NUM_1 -> "1";
                case NUM_2 -> "2";
                case NUM_3 -> "3";
                case NUM_4 -> "4";
                case NUM_5 -> "5";
                case NUM_6 -> "6";
                case NUM_7 -> "7";
                case NUM_8 -> "8";
                case NUM_9 -> "9";
                case STAR -> "*";
                case POUND -> "#";
                case UP -> "Up";
                case DOWN -> "Down";
                case LEFT -> "Left";
                case RIGHT -> "Right";
                case CENTER -> "Center";
                case VOLUME_UP -> "Volume Up";
                case VOLUME_DOWN -> "Volume Down";
                case POWER -> "Power";
                case CAMERA -> "Camera";
                case CLEAR -> "Clear";
                case A -> "A";
                case B -> "B";
                case C -> "C";
                case D -> "D";
                case E -> "E";
                case F -> "F";
                case G -> "G";
                case H -> "H";
                case I -> "I";
                case J -> "J";
                case K -> "K";
                case L -> "L";
                case M -> "M";
                case N -> "N";
                case O -> "O";
                case P -> "P";
                case Q -> "Q";
                case R -> "R";
                case S -> "S";
                case T -> "T";
                case U -> "U";
                case V -> "V";
                case W -> "W";
                case X -> "X";
                case Y -> "Y";
                case Z -> "Z";
                case COMMA -> ",";
                case PERIOD -> ".";
                case ALT_LEFT -> "L-Alt";
                case ALT_RIGHT -> "R-Alt";
                case SHIFT_LEFT -> "L-Shift";
                case SHIFT_RIGHT -> "R-Shift";
                case TAB -> "Tab";
                case SPACE -> "Space";
                case SYM -> "SYM";
                case EXPLORER -> "Explorer";
                case ENVELOPE -> "Envelope";
                case ENTER -> "Enter";
                case DEL -> "Delete"; // also BACKSPACE
                case GRAVE -> "`";
                case MINUS -> "-";
                case EQUALS -> "=";
                case LEFT_BRACKET -> "[";
                case RIGHT_BRACKET -> "]";
                case BACKSLASH -> "\\";
                case SEMICOLON -> ";";
                case APOSTROPHE -> "'";
                case SLASH -> "/";
                case AT -> "@";
                case NUM -> "Num";
                case HEADSETHOOK -> "Headset Hook";
                case FOCUS -> "Focus";
                case PLUS -> "Plus";
                case MENU -> "Menu";
                case NOTIFICATION -> "Notification";
                case SEARCH -> "Search";
                case MEDIA_PLAY_PAUSE -> "Play/Pause";
                case MEDIA_STOP -> "Stop Media";
                case MEDIA_NEXT -> "Next Media";
                case MEDIA_PREVIOUS -> "Prev Media";
                case MEDIA_REWIND -> "Rewind";
                case MEDIA_FAST_FORWARD -> "Fast Forward";
                case MUTE -> "Mute";
                case PAGE_UP -> "Page Up";
                case PAGE_DOWN -> "Page Down";
                case PICTSYMBOLS -> "PICTSYMBOLS";
                case SWITCH_CHARSET -> "SWITCH_CHARSET";
                case BUTTON_A -> "A Button";
                case BUTTON_B -> "B Button";
                case BUTTON_C -> "C Button";
                case BUTTON_X -> "X Button";
                case BUTTON_Y -> "Y Button";
                case BUTTON_Z -> "Z Button";
                case BUTTON_L1 -> "L1 Button";
                case BUTTON_R1 -> "R1 Button";
                case BUTTON_L2 -> "L2 Button";
                case BUTTON_R2 -> "R2 Button";
                case BUTTON_THUMBL -> "Left Thumb";
                case BUTTON_THUMBR -> "Right Thumb";
                case BUTTON_START -> "Start";
                case BUTTON_SELECT -> "Select";
                case BUTTON_MODE -> "Button Mode";
                case FORWARD_DEL -> "Forward Delete";
                case CONTROL_LEFT -> "L-Ctrl";
                case CONTROL_RIGHT -> "R-Ctrl";
                case ESCAPE -> "Escape";
                case END -> "End";
                case INSERT -> "Insert";
                case NUMPAD_0 -> "Numpad 0";
                case NUMPAD_1 -> "Numpad 1";
                case NUMPAD_2 -> "Numpad 2";
                case NUMPAD_3 -> "Numpad 3";
                case NUMPAD_4 -> "Numpad 4";
                case NUMPAD_5 -> "Numpad 5";
                case NUMPAD_6 -> "Numpad 6";
                case NUMPAD_7 -> "Numpad 7";
                case NUMPAD_8 -> "Numpad 8";
                case NUMPAD_9 -> "Numpad 9";
                case COLON -> ":";
                case F1 -> "F1";
                case F2 -> "F2";
                case F3 -> "F3";
                case F4 -> "F4";
                case F5 -> "F5";
                case F6 -> "F6";
                case F7 -> "F7";
                case F8 -> "F8";
                case F9 -> "F9";
                case F10 -> "F10";
                case F11 -> "F11";
                case F12 -> "F12";
                case F13 -> "F13";
                case F14 -> "F14";
                case F15 -> "F15";
                case F16 -> "F16";
                case F17 -> "F17";
                case F18 -> "F18";
                case F19 -> "F19";
                case F20 -> "F20";
                case F21 -> "F21";
                case F22 -> "F22";
                case F23 -> "F23";
                case F24 -> "F24";
                case NUMPAD_DIVIDE -> "Num /";
                case NUMPAD_MULTIPLY -> "Num *";
                case NUMPAD_SUBTRACT -> "Num -";
                case NUMPAD_ADD -> "Num +";
                case NUMPAD_DOT -> "Num .";
                case NUMPAD_COMMA -> "Num ,";
                case NUMPAD_ENTER -> "Num Enter";
                case NUMPAD_EQUALS -> "Num =";
                case NUMPAD_LEFT_PAREN -> "Num (";
                case NUMPAD_RIGHT_PAREN -> "Num )";
                case NUM_LOCK -> "Num Lock";
                case CAPS_LOCK -> "Caps Lock";
                case SCROLL_LOCK -> "Scroll Lock";
                case PAUSE -> "Pause";
                case PRINT_SCREEN -> "Print";
                // BUTTON_CIRCLE unhandled, as it conflicts with the more likely to be pressed F12
                default ->
                    // key name not found
                        null;
            };
        }

        private static Map<String, Integer> keyNames;

        /**
         * @param keyname the keyname returned by the {@link Keys#toString(int)} method
         * @return the int keycode
         */
        static int valueOf(String keyname) {
            if (keyNames == null) initializeKeyNames();
            Integer value = keyNames.get(keyname);
            return Objects.requireNonNullElse(value, -1);
        }

        /**
         * lazily intialized in {@link Keys#valueOf(String)}
         */
        private static void initializeKeyNames() {
            keyNames = new HashMap<String, Integer>();
            for (int i = 0; i < 256; i++) {
                String name = toString(i);
                if (name != null) keyNames.put(name, i);
            }
        }
    }

    /**
     * Enumeration of potentially available peripherals. Use with {@link Input#isPeripheralAvailable(Peripheral)}.
     *
     * @author mzechner
     */
    enum Peripheral {
        HardwareKeyboard, OnscreenKeyboard, MultitouchScreen, Accelerometer, Compass, Vibrator, HapticFeedback, Gyroscope, RotationVector, Pressure
    }

    /**
     * @return The acceleration force in m/s^2 applied to the device in the X axis, including the force of gravity
     */
    float getAccelerometerX();

    /**
     * @return The acceleration force in m/s^2 applied to the device in the Y axis, including the force of gravity
     */
    float getAccelerometerY();

    /**
     * @return The acceleration force in m/s^2 applied to the device in the Z axis, including the force of gravity
     */
    float getAccelerometerZ();

    /**
     * @return The rate of rotation in rad/s around the X axis
     */
    float getGyroscopeX();

    /**
     * @return The rate of rotation in rad/s around the Y axis
     */
    float getGyroscopeY();

    /**
     * @return The rate of rotation in rad/s around the Z axis
     */
    float getGyroscopeZ();

    /**
     * @return The maximum number of pointers supported
     */
    int getMaxPointers();

    /**
     * @return The x coordinate of the last touch on touch screen devices and the current mouse position on desktop for the first
     * pointer in screen coordinates. The screen origin is the top left corner.
     */
    int getX();

    /**
     * Returns the x coordinate in screen coordinates of the given pointer. Pointers are indexed from 0 to n. The pointer id
     * identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1 is the second and so on.
     * When two fingers are touched down and the first one is lifted the second one keeps its index. If another finger is placed on
     * the touch screen the first free index will be used.
     *
     * @param pointer the pointer id.
     * @return the x coordinate
     */
    int getX(int pointer);

    /**
     * @return the different between the current pointer location and the last pointer location on the x-axis.
     */
    int getDeltaX();

    /**
     * @return the different between the current pointer location and the last pointer location on the x-axis.
     */
    int getDeltaX(int pointer);

    /**
     * @return The y coordinate of the last touch on touch screen devices and the current mouse position on desktop for the first
     * pointer in screen coordinates. The screen origin is the top left corner.
     */
    int getY();

    /**
     * Returns the y coordinate in screen coordinates of the given pointer. Pointers are indexed from 0 to n. The pointer id
     * identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1 is the second and so on.
     * When two fingers are touched down and the first one is lifted the second one keeps its index. If another finger is placed on
     * the touch screen the first free index will be used.
     *
     * @param pointer the pointer id.
     * @return the y coordinate
     */
    int getY(int pointer);

    /**
     * @return the different between the current pointer location and the last pointer location on the y-axis.
     */
    int getDeltaY();

    /**
     * @return the different between the current pointer location and the last pointer location on the y-axis.
     */
    int getDeltaY(int pointer);

    /**
     * @return whether the screen is currently touched.
     */
    boolean isTouched();

    /**
     * @return whether a new touch down event just occurred.
     */
    boolean justTouched();

    /**
     * Whether the screen is currently touched by the pointer with the given index. Pointers are indexed from 0 to n. The pointer
     * id identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1 is the second and so on.
     * When two fingers are touched down and the first one is lifted the second one keeps its index. If another finger is placed on
     * the touch screen the first free index will be used.
     *
     * @param pointer the pointer
     * @return whether the screen is touched by the pointer
     */
    boolean isTouched(int pointer);

    /**
     * @return the pressure of the first pointer
     */
    float getPressure();

    /**
     * Returns the pressure of the given pointer, where 0 is untouched. On Android it should be up to 1.0, but it can go above
     * that slightly and its not consistent between devices. On iOS 1.0 is the normal touch and significantly more of hard touch.
     * Check relevant manufacturer documentation for details. Check availability with
     * {@link Input#isPeripheralAvailable(Peripheral)}. If not supported, returns 1.0 when touched.
     *
     * @param pointer the pointer id.
     * @return the pressure
     */
    float getPressure(int pointer);

    /**
     * Whether a given button is pressed or not. Button constants can be found in {@link Buttons}. On Android only the
     * Buttons#LEFT constant is meaningful before version 4.0.
     *
     * @param button the button to check.
     * @return whether the button is down or not.
     */
    boolean isButtonPressed(int button);

    /**
     * Returns whether a given button has just been pressed. Button constants can be found in {@link Buttons}. On Android only the
     * Buttons#LEFT constant is meaningful before version 4.0. On WebGL (GWT), only LEFT, RIGHT and MIDDLE buttons are supported.
     *
     * @param button the button to check.
     * @return true or false.
     */
    boolean isButtonJustPressed(int button);

    /**
     * Returns whether the key is pressed.
     *
     * @param key The key code as found in {@link Keys}.
     * @return true or false.
     */
    boolean isKeyPressed(int key);

    /**
     * Returns whether the key has just been pressed.
     *
     * @param key The key code as found in {@link Keys}.
     * @return true or false.
     */
    boolean isKeyJustPressed(int key);

    /**
     * System dependent method to input a string of text. A dialog box will be created with the given title and the given text as
     * a message for the user. Will use the Default keyboard type. Once the dialog has been closed the provided
     * {@link TextInputListener} will be called on the rendering thread.
     *
     * @param listener The TextInputListener.
     * @param title    The title of the text input dialog.
     * @param text     The message presented to the user.
     */
    void getTextInput(TextInputListener listener, String title, String text, String hint);

    /**
     * System dependent method to input a string of text. A dialog box will be created with the given title and the given text as
     * a message for the user. Once the dialog has been closed the provided {@link TextInputListener} will be called on the
     * rendering thread.
     *
     * @param listener The TextInputListener.
     * @param title    The title of the text input dialog.
     * @param text     The message presented to the user.
     * @param type     which type of keyboard we wish to display
     */
    void getTextInput(TextInputListener listener, String title, String text, String hint, OnscreenKeyboardType type);

    /**
     * Sets the on-screen keyboard visible if available. Will use the Default keyboard type.
     *
     * @param visible visible or not
     */
    void setOnscreenKeyboardVisible(boolean visible);

    /**
     * Sets the on-screen keyboard visible if available.
     *
     * @param visible visible or not
     * @param type    which type of keyboard we wish to display. Can be null when hiding
     */
    void setOnscreenKeyboardVisible(boolean visible, OnscreenKeyboardType type);

    static interface InputStringValidator {
        /**
         * @param toCheck The string that should be validated
         * @return true, if the string is acceptable, false if not.
         */
        boolean validate(String toCheck);
    }

    /**
     * Sets the on-screen keyboard visible if available.
     *
     * @param configuration The configuration for the native input field
     */
    void openTextInputField(NativeInputConfiguration configuration);

    /**
     * Closes the native input field and applies the result to the input wrapper.
     *
     * @param sendReturn Whether a "return" key should be send after processing
     */
    void closeTextInputField(boolean sendReturn);

    static interface KeyboardHeightObserver {
        void onKeyboardHeightChanged(int height);
    }

    /**
     * This will set a keyboard height callback. This will get called, whenever the keyboard height changes. Note: When using
     * openTextInputField, it will report the height of the native input field too.
     */
    void setKeyboardHeightObserver(KeyboardHeightObserver observer);

    enum OnscreenKeyboardType {
        Default, NumberPad, PhonePad, Email, Password, URI
    }

    /**
     * Generates a simple haptic effect of a given duration or a vibration effect on devices without haptic capabilities. Note
     * that on Android backend you'll need the permission
     * <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for this to work.
     * On iOS backend you'll need to set <code>useHaptics = true</code> for devices with haptics capabilities to use them.
     *
     * @param milliseconds the number of milliseconds to vibrate.
     */
    void vibrate(int milliseconds);

    /**
     * Generates a simple haptic effect of a given duration and default amplitude. Note that on Android backend you'll need the
     * permission <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for
     * this to work. On iOS backend you'll need to set <code>useHaptics = true</code> for devices with haptics capabilities to use
     * them.
     *
     * @param milliseconds the duration of the haptics effect
     * @param fallback     whether to use non-haptic vibrator on devices without haptics capabilities (or haptics disabled). Fallback
     *                     non-haptic vibrations may ignore length parameter in some backends.
     */
    void vibrate(int milliseconds, boolean fallback);

    /**
     * Generates a simple haptic effect of a given duration and amplitude. Note that on Android backend you'll need the permission
     * <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for this to work.
     * On iOS backend you'll need to set <code>useHaptics = true</code> for devices with haptics capabilities to use them.
     *
     * @param milliseconds the duration of the haptics effect
     * @param amplitude    the amplitude/strength of the haptics effect. Valid values in the range [0, 255].
     * @param fallback     whether to use non-haptic vibrator on devices without haptics capabilities (or haptics disabled). Fallback
     *                     non-haptic vibrations may ignore length and/or amplitude parameters in some backends.
     */
    void vibrate(int milliseconds, int amplitude, boolean fallback);

    /**
     * Generates a simple haptic effect of a type. VibrationTypes are length/amplitude haptic effect presets that depend on each
     * device and are defined by manufacturers. Should give most consistent results across devices and OSs. Note that on Android
     * backend you'll need the permission <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your
     * manifest file in order for this to work. On iOS backend you'll need to set <code>useHaptics = true</code> for devices with
     * haptics capabilities to use them.
     *
     * @param vibrationType the type of vibration
     */
    void vibrate(VibrationType vibrationType);

    enum VibrationType {
        LIGHT, MEDIUM, HEAVY;
    }

    /**
     * The azimuth is the angle of the device's orientation around the z-axis. The positive z-axis points towards the earths
     * center.
     *
     * @return the azimuth in degrees
     * @see <a
     * href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[],
     * float[], float[], float[])</a>
     */
    float getAzimuth();

    /**
     * The pitch is the angle of the device's orientation around the x-axis. The positive x-axis roughly points to the west and is
     * orthogonal to the z- and y-axis.
     *
     * @return the pitch in degrees
     * @see <a
     * href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[],
     * float[], float[], float[])</a>
     */
    float getPitch();

    /**
     * The roll is the angle of the device's orientation around the y-axis. The positive y-axis points to the magnetic north pole
     * of the earth.
     *
     * @return the roll in degrees
     * @see <a
     * href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[],
     * float[], float[], float[])</a>
     */
    float getRoll();

    /**
     * Returns the rotation matrix describing the devices rotation as per
     * <a href= "http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[],
     * float[], float[])" >SensorManager#getRotationMatrix(float[], float[], float[], float[])</a>. Does not manipulate the matrix
     * if the platform does not have an accelerometer.
     *
     * @param matrix Matrix to rotate
     */
    void getRotationMatrix(float[] matrix);

    /**
     * @return the time of the event currently reported to the {@link InputProcessor}.
     */
    long getCurrentEventTime();

    /**
     * Sets whether the given key on Android or GWT should be caught. No effect on other platforms. All keys that are not caught
     * may be handled by other apps or background processes on Android, or may trigger default browser behaviour on GWT. For
     * example, media or volume buttons are handled by background media players if present, or Space key triggers a scroll. All
     * keys you need to control your game should be caught to prevent unintended behaviour.
     *
     * @param keycode  keycode to catch
     * @param catchKey whether to catch the given keycode
     */
    void setCatchKey(int keycode, boolean catchKey);

    /**
     * @param keycode keycode to check if caught
     * @return true if the given keycode is configured to be caught
     */
    boolean isCatchKey(int keycode);

    /**
     * Sets the {@link InputProcessor} that will receive all touch and key input events. It will be called before the
     * {@link ApplicationListener#render()} method each frame.
     *
     * @param processor the InputProcessor
     */
    void setInputProcessor(InputProcessor processor);

    /**
     * @return the currently set {@link InputProcessor} or null.
     */
    InputProcessor getInputProcessor();

    /**
     * Queries whether a {@link Peripheral} is currently available. In case of Android and the {@link Peripheral#HardwareKeyboard}
     * this returns the whether the keyboard is currently slid out or not.
     *
     * @param peripheral the {@link Peripheral}
     * @return whether the peripheral is available or not.
     */
    boolean isPeripheralAvailable(Peripheral peripheral);

    /**
     * @return the rotation of the device with respect to its native orientation.
     */
    int getRotation();

    /**
     * @return the native orientation of the device.
     */
    Orientation getNativeOrientation();

    enum Orientation {
        Landscape, Portrait
    }

    /**
     * Only viable on the desktop. Will confine the mouse cursor location to the window and hide the mouse cursor. X and y
     * coordinates are still reported as if the mouse was not catched.
     *
     * @param catched whether to catch or not to catch the mouse cursor
     */
    void setCursorCatched(boolean catched);

    /**
     * @return whether the mouse cursor is catched.
     */
    boolean isCursorCatched();

    /**
     * Only viable on the desktop. Will set the mouse cursor location to the given window coordinates (origin top-left corner).
     *
     * @param x the x-position
     * @param y the y-position
     */
    void setCursorPosition(int x, int y);
}
