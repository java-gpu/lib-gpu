package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class CharEvent extends UiEvent {
    private int length;
    private char character;

    public CharEvent(int length, char character, long windowHandler) {
        super(UiEventType.Char, windowHandler);
        this.length = length;
        this.character = character;
    }

    public CharEvent(int length, char character) {
        this(length, character, 0L);
    }
}
