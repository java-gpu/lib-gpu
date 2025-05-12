package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

import java.awt.*;
import java.awt.event.WindowEvent;

@Setter
@Getter
@ToString(callSuper = true)
public class CustomWindowEvent extends WindowEvent implements AppEvent {

    private long windowHandler;
    private Object eventData;

    public CustomWindowEvent(Window source, int id, Window opposite, int oldState, int newState, long windowHandler, Object eventData) {
        super(source, id, opposite, oldState, newState);
        this.windowHandler = windowHandler;
        this.eventData = eventData;
    }

    public CustomWindowEvent(WindowEvent windowEvent, long windowHandler, Object eventData) {
        this((Window) windowEvent.getSource(), windowEvent.getID(), windowEvent.getOppositeWindow(), windowEvent.getOldState(),
                windowEvent.getNewState(), windowHandler, eventData);
    }

    public CustomWindowEvent(WindowEvent windowEvent, long windowHandler) {
        this(windowEvent, windowHandler, null);
    }

    @Override
    public AppEvent clone() {
        return new CustomWindowEvent(this, windowHandler, eventData);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Window;
    }
}
