package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.KeyEnum;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class KeyEvent extends UiEvent {
    private KeyEnum key;
    private int modifiers;
    private boolean down;

    public KeyEvent(KeyEnum key, int modifiers, boolean down, long windowHandler) {
        super(UiEventType.Key, windowHandler);
        this.key = key;
        this.modifiers = modifiers;
        this.down = down;
    }

    public KeyEvent(KeyEnum key, int modifiers, boolean down) {
        this(key, modifiers, down, Long.MAX_VALUE);
    }
}
