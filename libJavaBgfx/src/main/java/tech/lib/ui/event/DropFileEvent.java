package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class DropFileEvent implements AppEvent {

    private String filePath;
    private long windowHandler;
    private Object eventData;

    public DropFileEvent(String filePath, long windowHandler, Object data) {
        this.windowHandler = windowHandler;
        this.eventData = data;
        this.filePath = filePath;
    }

    public DropFileEvent(String filePath, long windowHandler) {
        this(filePath, windowHandler, null);
    }

    public DropFileEvent(String filePath) {
        this(filePath, Long.MAX_VALUE);
    }

    @Override
    public AppEvent clone() {
        return new DropFileEvent(filePath, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.DropFile;
    }
}
