package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AppWindow;
import tech.lib.ui.event.CustomFocusEvent;
import tech.lib.ui.jni.EventManager;

import java.awt.event.FocusEvent;

public class FocusEventQueueListener implements java.awt.event.FocusListener {

    private final AppWindow appWindow;

    public FocusEventQueueListener(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void focusGained(FocusEvent e) {
        EventManager.pushUiEvent(new CustomFocusEvent(e, appWindow.getWindowPtr(), e));
    }

    @Override
    public void focusLost(FocusEvent e) {
        EventManager.pushUiEvent(new CustomFocusEvent(e, appWindow.getWindowPtr(), e));
    }
}
