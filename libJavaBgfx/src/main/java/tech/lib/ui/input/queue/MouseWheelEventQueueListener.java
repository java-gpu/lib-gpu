package tech.lib.ui.input.queue;

import tech.lib.bgfx.app.AwtAppWindow;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelEventQueueListener implements MouseWheelListener {

    private final AwtAppWindow appWindow;

    public MouseWheelEventQueueListener(AwtAppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
