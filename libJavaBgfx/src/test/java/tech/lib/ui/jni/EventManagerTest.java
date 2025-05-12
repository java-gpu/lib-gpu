package tech.lib.ui.jni;

import org.junit.jupiter.api.Test;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.SuspendState;
import tech.lib.ui.event.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class EventManagerTest {

    Random random = new Random();

    @Test
    public void testSelfCreateAxisEvent() {
        long windowHandler = random.nextLong();
        long gamepadHandler = random.nextLong();
        int value = random.nextInt();
        GamepadAxis[] axisArr = GamepadAxis.values();
        for (GamepadAxis axis : axisArr) {
            AppEvent event = EventManager.selfCreateAxisEvent(axis, value, gamepadHandler, windowHandler);
            assertInstanceOf(AxisEvent.class, event);
            AxisEvent axisEvent = (AxisEvent) event;
            assertEquals(axis, axisEvent.getAxis());
            assertEquals(value, axisEvent.getValue());
            assertEquals(gamepadHandler, axisEvent.getGamepadHandler());
            assertEquals(windowHandler, axisEvent.getWindowHandler());
        }
    }

    @Test
    public void testSelfCreateGamepadEvent() {
        long windowHandler = random.nextLong();
        long gamepadHandler = random.nextLong();
        AppEvent event = EventManager.selfCreateGamepadEvent(true, gamepadHandler, windowHandler);
        assertInstanceOf(GamepadEvent.class, event);
        GamepadEvent gamepadEvent = (GamepadEvent) event;
        assertTrue(gamepadEvent.isConnected());
        assertEquals(gamepadHandler, gamepadEvent.getGamepadHandler());
        assertEquals(windowHandler, gamepadEvent.getWindowHandler());
        // -----
        event = EventManager.selfCreateGamepadEvent(false, gamepadHandler, windowHandler);
        assertInstanceOf(GamepadEvent.class, event);
        gamepadEvent = (GamepadEvent) event;
        assertFalse(gamepadEvent.isConnected());
        assertEquals(gamepadHandler, gamepadEvent.getGamepadHandler());
        assertEquals(windowHandler, gamepadEvent.getWindowHandler());
    }

    @Test
    public void testSelfCreateDropFileEvent() {
        long windowHandler = random.nextLong();
        String filePath = "/tmp/no-existed-path";
        AppEvent event = EventManager.selfCreateDropFileEvent(filePath, windowHandler);
        assertInstanceOf(DropFileEvent.class, event);
        DropFileEvent dropFileEvent = (DropFileEvent) event;
        assertEquals(filePath, dropFileEvent.getFilePath());
        assertEquals(windowHandler, dropFileEvent.getWindowHandler());
    }

    @Test
    public void testSelfCreateSizeEvent() {
        long windowHandler = random.nextLong();
        int width = random.nextInt();
        int height = random.nextInt();
        AppEvent event = EventManager.selfCreateSizeEvent(width, height, windowHandler);
        assertInstanceOf(SizeEvent.class, event);
        SizeEvent sizeEvent = (SizeEvent) event;
        assertEquals(width, sizeEvent.getWidth());
        assertEquals(height, sizeEvent.getHeight());
        assertEquals(windowHandler, sizeEvent.getWindowHandler());
    }

    @Test
    public void testSelfCreateSuspendEvent() {
        long windowHandler = random.nextLong();
        SuspendState[] states = SuspendState.values();
        for (SuspendState state : states) {
            AppEvent event = EventManager.selfCreateSuspendEvent(state, windowHandler);
            assertInstanceOf(SuspendEvent.class, event);
            SuspendEvent suspendEvent = (SuspendEvent) event;
            assertEquals(state, suspendEvent.getState());
            assertEquals(windowHandler, suspendEvent.getWindowHandler());
        }
    }
}
