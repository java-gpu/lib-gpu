package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class AxisEvent extends UiEvent {

    private GamepadAxis axis;
    private int value;
    private long gamepadHandler;

    public AxisEvent(GamepadAxis axis, int value, long gamepadHandler, long windowHandler) {
        super(UiEventType.Axis, windowHandler);
        this.axis = axis;
        this.value = value;
        this.gamepadHandler = gamepadHandler;
    }

    public AxisEvent(GamepadAxis axis, int value, long gamepadHandler) {
        this(axis, value, gamepadHandler, Long.MAX_VALUE);
    }
}
