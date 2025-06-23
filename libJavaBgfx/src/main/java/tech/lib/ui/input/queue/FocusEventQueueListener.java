package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AwtAppWindow;
import tech.lib.ui.event.CustomFocusEvent;
import tech.lib.ui.jni.EventManager;

import java.awt.event.FocusEvent;

public class FocusEventQueueListener implements java.awt.event.FocusListener {

    private final AwtAppWindow appWindow;

    public FocusEventQueueListener(AwtAppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void focusGained(FocusEvent e) {
        EventManager.pushUiEvent(new CustomFocusEvent(e, appWindow.getWindowPointer(), e));
    }

    @Override
    public void focusLost(FocusEvent e) {
        EventManager.pushUiEvent(new CustomFocusEvent(e, appWindow.getWindowPointer(), e));
    }
}
