package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

import java.awt.*;
import java.awt.event.FocusEvent;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomFocusEvent extends FocusEvent implements AppEvent {
    private long windowHandler;
    private Object eventData;

    public CustomFocusEvent(Component source, int id, boolean temporary, Component opposite, Cause cause, long windowHandler, Object eventData) {
        super(source, id, temporary, opposite, cause);
        this.windowHandler = windowHandler;
        this.eventData = eventData;
    }

    public CustomFocusEvent(Component source, int id, boolean temporary, Component opposite, Cause cause, long windowHandler) {
        this(source, id, temporary, opposite, cause, windowHandler, null);
    }

    public CustomFocusEvent(FocusEvent focusEvent, long windowHandler, Object eventData) {
        this(focusEvent.getComponent(), focusEvent.getID(), focusEvent.isTemporary(), focusEvent.getOppositeComponent(), focusEvent.getCause(), windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Focus;
    }

    @Override
    public AppEvent clone() {
        return new CustomFocusEvent(getComponent(), getID(), isTemporary(), getOppositeComponent(), getCause(), windowHandler, eventData);
    }
}
