package tech.lib.ui.input.queue;

import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.app.AppWindow;

import java.awt.dnd.DnDConstants;

@Slf4j
public class AppWindowEventQueueInitializer {

    public static void monitorWindowEventQueue(AppWindow appWindow) {
        appWindow.addFocusListener(new FocusEventQueueListener(appWindow));
        appWindow.addKeyListener(new KeyEventQueueListener(appWindow));
        appWindow.addMouseListener(new MouseEventQueueListener(appWindow));
        appWindow.addMouseWheelListener(new MouseWheelEventQueueListener(appWindow));
        appWindow.addWindowListener(new WindowEventQueueListener(appWindow));
        // Set up DropTarget
        var dt = new DropTargetAsFileQueue(appWindow, DnDConstants.ACTION_COPY_OR_MOVE);
        appWindow.setDropTarget(dt);
    }
}
