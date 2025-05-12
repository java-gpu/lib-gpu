package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class SizeEvent implements AppEvent {
    private int width;
    private int height;
    private long windowHandler;
    private Object eventData;

    public SizeEvent(int width, int height, long windowHandler, Object eventData) {
        this.windowHandler = windowHandler;
        this.eventData = eventData;
        this.width = width;
        this.height = height;
    }

    public SizeEvent(int width, int height, long windowHandler) {
        this(width, height, windowHandler, null);
    }

    public SizeEvent(int width, int height) {
        this(width, height, Long.MAX_VALUE);
    }

    @Override
    public AppEvent clone() {
        return new SizeEvent(width, height, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Size;
    }
}
