package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class ExitEvent extends UiEvent {

    public ExitEvent(long windowHandler) {
        super(UiEventType.Exit, windowHandler);
    }

    public ExitEvent() {
        super(UiEventType.Exit);
    }
}
