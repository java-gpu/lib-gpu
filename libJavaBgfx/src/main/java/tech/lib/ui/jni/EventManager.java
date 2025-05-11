package tech.lib.ui.jni;

import tech.lib.bgfx.jni.JniLoader;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.KeyEnum;
import tech.lib.ui.enu.MouseButton;
import tech.lib.ui.enu.SuspendState;
import tech.lib.ui.event.UiEvent;

public class EventManager {

    static {
        JniLoader.loadBgFxJni();
    }

    public native static UiEvent pollUiEvent(long windowHandler);

    /**
     * Generate a fake AxisEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param axis           GamepadAxis
     * @param value          Value
     * @param gamepadHandler Gamepad Handler Pointer
     * @param windowHandler  Window Handler Pointer
     * @return An AxisEvent
     */
    public native static UiEvent selfCreateAxisEvent(GamepadAxis axis, int value, long gamepadHandler, long windowHandler);

    /**
     * Generate a fake CharEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param length        Length
     * @param character     Character
     * @param windowHandler Window Handler Pointer
     * @return An CharEvent
     */
    public native static UiEvent selfCreateCharEvent(int length, char character, long windowHandler);

    /**
     * Generate a fake ExitEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param windowHandler Window Handler Pointer
     * @return An ExitEvent
     */
    public native static UiEvent selfCreateExitEvent(long windowHandler);

    /**
     * Generate a fake GamepadEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param connected      Connected or not
     * @param gamepadHandler Gamepad Handler Pointer
     * @param windowHandler  Window Handler Pointer
     * @return An GamepadEvent
     */
    public native static UiEvent selfCreateGamepadEvent(boolean connected, long gamepadHandler, long windowHandler);

    /**
     * Generate a fake DropFileEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param filePath      File absolute path
     * @param windowHandler Window Handler Pointer
     * @return An DropFileEvent
     */
    public native static UiEvent selfCreateDropFileEvent(String filePath, long windowHandler);

    /**
     * Generate a fake SizeEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param width         Width
     * @param height        Height
     * @param windowHandler Window Handler Pointer
     * @return An SizeEvent
     */
    public native static UiEvent selfCreateSizeEvent(int width, int height, long windowHandler);

    /**
     * Generate a fake SuspendEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param state         SuspendState
     * @param windowHandler Window Handler Pointer
     * @return An SuspendEvent
     */
    public native static UiEvent selfCreateSuspendEvent(SuspendState state, long windowHandler);

    /**
     * Generate a fake WindowEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param windowHandler Window Handler Pointer
     * @return An WindowEvent
     */
    public native static UiEvent selfCreateWindowEvent(long windowHandler);

    /**
     * Generate a fake MouseEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param mx            X position of mouse
     * @param my            Y position of mouse
     * @param mz            Z position of mouse
     * @param button        Mouse Button
     * @param down          Mouse button pressed down or not
     * @param move          Mouse moved or not
     * @param windowHandler Window Handler Pointer
     * @return An MouseEvent
     */
    public native static UiEvent selfCreateMouseEvent(int mx, int my, int mz, MouseButton button, boolean down, boolean move, long windowHandler);

    /**
     * Generate a fake KeyEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param key           Key triggered
     * @param modifiers     Modifiers
     * @param down          Key pressed down or not
     * @param windowHandler Window Handler Pointer
     * @return An KeyEvent
     */
    public native static UiEvent selfCreateKeyEvent(KeyEnum key, int modifiers, boolean down, long windowHandler);
}
