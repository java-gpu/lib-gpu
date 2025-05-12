package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class ExitEvent implements AppEvent {

    private long windowHandler;
    private Object eventData;

    public ExitEvent(long windowHandler, Object data) {
        this.windowHandler = windowHandler;
        this.eventData = data;
    }

    public ExitEvent(long windowHandler) {
        this(windowHandler, null);
    }

    @Override
    public AppEvent clone() {
        return new ExitEvent(windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Exit;
    }
}
