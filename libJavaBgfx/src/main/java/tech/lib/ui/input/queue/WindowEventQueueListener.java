package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AppWindow;
import tech.lib.ui.event.CustomWindowEvent;
import tech.lib.ui.jni.EventManager;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowEventQueueListener implements WindowListener {
    private final AppWindow appWindow;

    public WindowEventQueueListener(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowClosing(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowClosed(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowIconified(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowActivated(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        EventManager.pushUiEvent(new CustomWindowEvent(e, appWindow.getWindowPtr()));
    }
}
