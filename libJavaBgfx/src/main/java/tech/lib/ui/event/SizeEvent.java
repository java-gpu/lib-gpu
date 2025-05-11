package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class SizeEvent extends UiEvent {
    private int width;
    private int height;

    public SizeEvent(int width, int height, long windowHandler) {
        super(UiEventType.Size, windowHandler);
        this.width = width;
        this.height = height;
    }

    public SizeEvent(int width, int height) {
        this(width, height, Long.MAX_VALUE);
    }
}
