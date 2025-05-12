package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AppWindow;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelEventQueueListener implements MouseWheelListener {

    private final AppWindow appWindow;

    public MouseWheelEventQueueListener(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
