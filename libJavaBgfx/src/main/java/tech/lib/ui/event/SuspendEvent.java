package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;
import tech.lib.ui.enu.SuspendState;

@Setter
@Getter
@ToString(callSuper = true)
public class SuspendEvent implements AppEvent {

    private long windowHandler;
    private Object eventData;
    private SuspendState state;

    public SuspendEvent(SuspendState state, long windowHandler, Object eventData) {
        this.windowHandler = windowHandler;
        this.eventData = eventData;
        this.state = state;
    }

    public SuspendEvent(SuspendState state, long windowHandler) {
        this(state, windowHandler, null);
    }

    public SuspendEvent(SuspendState state) {
        this(state, Long.MAX_VALUE);
    }

    @Override
    public AppEvent clone() {
        return new SuspendEvent(state, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Suspend;
    }
}
