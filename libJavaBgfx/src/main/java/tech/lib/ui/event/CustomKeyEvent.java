package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

import java.awt.*;
import java.awt.event.KeyEvent;

@Setter
@Getter
@ToString(callSuper = true)
public class CustomKeyEvent extends KeyEvent implements AppEvent {

    private long windowHandler;
    private Object eventData;

    public CustomKeyEvent(Component source, int id, long when, int modifiers, int keyCode, char keyChar, int keyLocation, long windowHandler, Object eventData) {
        super(source, id, when, modifiers, keyCode, keyChar, keyLocation);
        this.windowHandler = windowHandler;
        this.eventData = eventData;
    }

    public CustomKeyEvent(KeyEvent keyEvent, long windowHandler, Object eventData) {
        this(keyEvent.getComponent(), keyEvent.getID(), keyEvent.getWhen(), keyEvent.getModifiersEx(), keyEvent.getKeyCode(), keyEvent.getKeyChar(), keyEvent.getKeyLocation(), windowHandler, eventData);
    }

    public CustomKeyEvent(KeyEvent keyEvent, long windowHandler) {
        this(keyEvent, windowHandler, null);
    }

    @Override
    public AppEvent clone() {
        return new CustomKeyEvent(this, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Key;
    }
}
