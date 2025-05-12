package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class GamepadEvent implements AppEvent {

    private boolean connected;
    private long gamepadHandler;
    private long windowHandler;
    private Object eventData;

    public GamepadEvent(boolean connected, long gamepadHandler, long windowHandler, Object data) {
        this.windowHandler = windowHandler;
        this.eventData = data;
        this.connected = connected;
        this.gamepadHandler = gamepadHandler;
    }

    public GamepadEvent(boolean connected, long gamepadHandler, long windowHandler) {
        this(connected, gamepadHandler, windowHandler, null);
    }

    public GamepadEvent(boolean connected, long gamepadHandler) {
        this(connected, gamepadHandler, Long.MAX_VALUE);
    }

    @Override
    public AppEvent clone() {
        return new GamepadEvent(connected, gamepadHandler, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Gamepad;
    }
}
