package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AppWindow;
import tech.lib.ui.event.CustomMouseEvent;
import tech.lib.ui.jni.EventManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventQueueListener implements MouseListener {

    private final AppWindow appWindow;

    public MouseEventQueueListener(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        EventManager.pushUiEvent(new CustomMouseEvent(e, 0, appWindow.getWindowPtr()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        EventManager.pushUiEvent(new CustomMouseEvent(e, 0, appWindow.getWindowPtr()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        EventManager.pushUiEvent(new CustomMouseEvent(e, 0, appWindow.getWindowPtr()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        EventManager.pushUiEvent(new CustomMouseEvent(e, 0, appWindow.getWindowPtr()));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        EventManager.pushUiEvent(new CustomMouseEvent(e, 0, appWindow.getWindowPtr()));
    }
}
