package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.AppEventType;

import java.awt.*;
import java.awt.event.MouseEvent;

@Setter
@Getter
@ToString(callSuper = true)
public class CustomMouseEvent extends MouseEvent implements AppEvent {
    private int z;
    private long windowHandler;
    private Object eventData;

    public CustomMouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount,
                            boolean popupTrigger, int button, int z, long windowHandler, Object eventData) {
        super(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
        this.z = z;
        this.windowHandler = windowHandler;
        this.eventData = eventData;
    }

    public CustomMouseEvent(MouseEvent mouseEvent, int z, long windowHandler, Object eventData) {
        this(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(),
                mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(),
                mouseEvent.getButton(), z, windowHandler, eventData);
    }

    public CustomMouseEvent(MouseEvent mouseEvent, int z, long windowHandler) {
        this(mouseEvent, z, windowHandler, null);
    }

    @Override
    public AppEventType getEventType() {
        return AppEventType.Mouse;
    }

    @Override
    public AppEvent clone() {
        return new CustomMouseEvent(this, z, windowHandler, eventData);
    }
}
