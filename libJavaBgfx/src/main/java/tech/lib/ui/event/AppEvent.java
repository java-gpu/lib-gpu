package tech.lib.ui.event;

import tech.lib.ui.enu.AppEventType;

public interface AppEvent extends Cloneable {

    AppEventType getEventType();

    long getWindowHandler();
    void setWindowHandler(long windowHandler);

    Object getEventData();

    AppEvent clone();
}
