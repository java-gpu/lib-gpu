package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.AppEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class AxisEvent implements AppEvent {

    private GamepadAxis axis;
    private float value;
    private long gamepadHandler;
    private long windowHandler;
    private Object eventData;

    public AxisEvent(GamepadAxis axis, float value, long gamepadHandler, long windowHandler, Object data) {
        this.windowHandler = windowHandler;
        this.eventData = data;
        this.axis = axis;
        this.value = value;
        this.gamepadHandler = gamepadHandler;
    }

    public AxisEvent(GamepadAxis axis, float value, long gamepadHandler, long windowHandler) {
        this(axis, value, gamepadHandler, windowHandler, null);
    }

    public AxisEvent(GamepadAxis axis, float value, long gamepadHandler) {
        this(axis, value, gamepadHandler, Long.MAX_VALUE);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Axis;
    }

    @Override
    public AppEvent clone() {
        return new AxisEvent(axis, value, gamepadHandler, windowHandler, eventData);
    }
}
