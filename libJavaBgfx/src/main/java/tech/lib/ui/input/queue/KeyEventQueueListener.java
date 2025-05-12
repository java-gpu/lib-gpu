package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AppWindow;
import tech.lib.ui.event.CustomKeyEvent;
import tech.lib.ui.jni.EventManager;

public class KeyEventQueueListener implements java.awt.event.KeyListener {

    private final AppWindow appWindow;

    public KeyEventQueueListener(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        EventManager.pushUiEvent(new CustomKeyEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        EventManager.pushUiEvent(new CustomKeyEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        EventManager.pushUiEvent(new CustomKeyEvent(e, appWindow.getWindowPtr()));
    }

}
