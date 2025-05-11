package tech.lib.ui.data;

import lombok.Data;
import tech.lib.ui.enu.MouseButton;

@Data
public class MouseState {

    /**
     * The first number in the range of ids used for mouse events.
     */
    public static final int MOUSE_FIRST = 500;

    /**
     * The last number in the range of ids used for mouse events.
     */
    public static final int MOUSE_LAST = 507;

    /**
     * The "mouse clicked" event. This {@code MouseEvent}
     * occurs when a mouse button is pressed and released.
     */
    public static final int MOUSE_CLICKED = MOUSE_FIRST;

    /**
     * The "mouse pressed" event. This {@code MouseEvent}
     * occurs when a mouse button is pushed down.
     */
    public static final int MOUSE_PRESSED = 1 + MOUSE_FIRST; //Event.MOUSE_DOWN

    /**
     * The "mouse released" event. This {@code MouseEvent}
     * occurs when a mouse button is let up.
     */
    public static final int MOUSE_RELEASED = 2 + MOUSE_FIRST; //Event.MOUSE_UP

    /**
     * The "mouse moved" event. This {@code MouseEvent}
     * occurs when the mouse position changes.
     */
    public static final int MOUSE_MOVED = 3 + MOUSE_FIRST; //Event.MOUSE_MOVE

    /**
     * The "mouse entered" event. This {@code MouseEvent}
     * occurs when the mouse cursor enters the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_ENTERED = 4 + MOUSE_FIRST; //Event.MOUSE_ENTER

    /**
     * The "mouse exited" event. This {@code MouseEvent}
     * occurs when the mouse cursor exits the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_EXITED = 5 + MOUSE_FIRST; //Event.MOUSE_EXIT

    /**
     * The "mouse dragged" event. This {@code MouseEvent}
     * occurs when the mouse position changes while a mouse button is pressed.
     */
    public static final int MOUSE_DRAGGED = 6 + MOUSE_FIRST; //Event.MOUSE_DRAG

    /**
     * The "mouse wheel" event.  This is the only {@code MouseWheelEvent}.
     * It occurs when a mouse equipped with a wheel has its wheel rotated.
     *
     * @since 1.4
     */
    public static final int MOUSE_WHEEL = 7 + MOUSE_FIRST;

    /**
     * Indicates no mouse buttons; used by {@link #getButton}.
     *
     * @since 1.4
     */
    public static final int BUTTON_EMPTY = 0;

    private float x;
    private float y;
    private float z;

    /**
     * Indicates which, if any, of the mouse buttons has changed state.
     *
     * @serial
     * @see #getButton()
     * @see java.awt.Toolkit#areExtraMouseButtonsEnabled()
     */
    MouseButton button;
}
