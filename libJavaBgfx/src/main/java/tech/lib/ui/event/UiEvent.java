package tech.lib.ui.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.lib.ui.enu.UiEventType;

@Data
@AllArgsConstructor
public abstract class UiEvent {
    private UiEventType type;
    private long windowHandler;

    public UiEvent(UiEventType type) {
        this(type, Long.MAX_VALUE);
    }
}
