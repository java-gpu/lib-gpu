package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class DropFileEvent extends UiEvent {

    private String filePath;

    public DropFileEvent(String filePath, long windowHandler) {
        super(UiEventType.DropFile, windowHandler);
        this.filePath = filePath;
    }

    public DropFileEvent(String filePath) {
        this(filePath, Long.MAX_VALUE);
    }
}
