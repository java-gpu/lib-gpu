package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import tech.lib.ui.enu.AppEventType;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

@Getter
@Setter
public class CustomMouseWheelEvent extends MouseWheelEvent implements AppEvent {
    private long windowHandler;
    private Object eventData;

    public CustomMouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount,
                                 boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation, long windowHandler, Object eventData) {
        super(source, id, when, modifiers, x, y, clickCount, popupTrigger, scrollType, scrollAmount, wheelRotation);
        this.windowHandler = windowHandler;
        this.eventData = eventData;
    }

    public CustomMouseWheelEvent(MouseWheelEvent mouseWheelEvent, long windowHandler, Object eventData) {
        this(mouseWheelEvent.getComponent(), mouseWheelEvent.getID(), mouseWheelEvent.getWhen(), mouseWheelEvent.getModifiersEx(),
                mouseWheelEvent.getX(), mouseWheelEvent.getY(), mouseWheelEvent.getClickCount(), mouseWheelEvent.isPopupTrigger(),
                mouseWheelEvent.getScrollType(), mouseWheelEvent.getScrollAmount(), mouseWheelEvent.getWheelRotation(),
                windowHandler, eventData);
    }

    public CustomMouseWheelEvent(MouseWheelEvent mouseWheelEvent, long windowHandler) {
        this(mouseWheelEvent, windowHandler, null);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.MouseWheel;
    }

    @Override
    public AppEvent clone() {
        return new CustomMouseWheelEvent(this, windowHandler);
    }
}
