package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.SuspendState;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class SuspendEvent extends UiEvent {

    private SuspendState state;

    public SuspendEvent(SuspendState state, long windowHandler) {
        super(UiEventType.Suspend, windowHandler);
        this.state = state;
    }

    public SuspendEvent(SuspendState state) {
        this(state, Long.MAX_VALUE);
    }
}
