package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class GamepadEvent extends UiEvent {

    private boolean connected;
    private long gamepadHandler;

    public GamepadEvent(boolean connected, long gamepadHandler, long windowHandler) {
        super(UiEventType.Gamepad, windowHandler);
        this.connected = connected;
        this.gamepadHandler = gamepadHandler;
    }

    public GamepadEvent(boolean connected, long gamepadHandler) {
        this(connected, gamepadHandler, Long.MAX_VALUE);
    }
}
