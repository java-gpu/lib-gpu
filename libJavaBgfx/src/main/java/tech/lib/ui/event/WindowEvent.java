package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class WindowEvent extends UiEvent {

    public WindowEvent(long windowHandler) {
        super(UiEventType.Window, windowHandler);
    }

    public WindowEvent() {
        super(UiEventType.Window);
    }
}
