package tech.lib.ui.jni;

import org.junit.jupiter.api.Test;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.KeyEnum;
import tech.lib.ui.enu.MouseButton;
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
            UiEvent event = EventManager.selfCreateAxisEvent(axis, value, gamepadHandler, windowHandler);
            assertInstanceOf(AxisEvent.class, event);
            AxisEvent axisEvent = (AxisEvent) event;
            assertEquals(axis, axisEvent.getAxis());
            assertEquals(value, axisEvent.getValue());
            assertEquals(gamepadHandler, axisEvent.getGamepadHandler());
            assertEquals(windowHandler, axisEvent.getWindowHandler());
        }
    }

    @Test
    public void testSelfCreateCharEvent() {
        long windowHandler = random.nextLong();
        int length = random.nextInt();
        char c = (char) random.nextInt();
        UiEvent event = EventManager.selfCreateCharEvent(length, c, windowHandler);
        assertInstanceOf(CharEvent.class, event);
        CharEvent charEvent = (CharEvent) event;
        assertEquals(length, charEvent.getLength());
        assertEquals(c, charEvent.getCharacter());
        assertEquals(windowHandler, charEvent.getWindowHandler());
    }

    @Test
    public void testSelfCreateExitEvent() {
        long windowHandler = random.nextLong();
        UiEvent event = EventManager.selfCreateExitEvent(windowHandler);
        assertInstanceOf(ExitEvent.class, event);
        assertEquals(windowHandler, event.getWindowHandler());
    }

    @Test
    public void testSelfCreateGamepadEvent() {
        long windowHandler = random.nextLong();
        long gamepadHandler = random.nextLong();
        UiEvent event = EventManager.selfCreateGamepadEvent(true, gamepadHandler, windowHandler);
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
        UiEvent event = EventManager.selfCreateDropFileEvent(filePath, windowHandler);
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
        UiEvent event = EventManager.selfCreateSizeEvent(width, height, windowHandler);
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
            UiEvent event = EventManager.selfCreateSuspendEvent(state, windowHandler);
            assertInstanceOf(SuspendEvent.class, event);
            SuspendEvent suspendEvent = (SuspendEvent) event;
            assertEquals(state, suspendEvent.getState());
            assertEquals(windowHandler, suspendEvent.getWindowHandler());
        }
    }

    @Test
    public void testSelfCreateWindowEvent() {
        long windowHandler = random.nextLong();
        UiEvent event = EventManager.selfCreateWindowEvent(windowHandler);
        assertInstanceOf(WindowEvent.class, event);
        assertEquals(windowHandler, event.getWindowHandler());
    }

    @Test
    public void testSelfCreateMouseEvent() {
        long windowHandler = random.nextLong();
        int mx = random.nextInt();
        int my = random.nextInt();
        int mz = random.nextInt();
        boolean down = random.nextBoolean();
        boolean move = random.nextBoolean();
        MouseButton[] buttons = MouseButton.values();
        for (MouseButton button : buttons) {
            UiEvent event = EventManager.selfCreateMouseEvent(mx, my, mz, button, down, move, windowHandler);
            assertInstanceOf(MouseEvent.class, event);
            MouseEvent mouseEvent = (MouseEvent) event;
            assertEquals(mx, mouseEvent.getMx());
            assertEquals(my, mouseEvent.getMy());
            assertEquals(mz, mouseEvent.getMz());
            assertEquals(down, mouseEvent.isDown());
            assertEquals(move, mouseEvent.isMove());
            assertEquals(windowHandler, mouseEvent.getWindowHandler());
        }
    }

    @Test
    public void testSelfCreateKeyEvent() {
        long windowHandler = random.nextLong();
        int modifiers = random.nextInt();
        boolean down = random.nextBoolean();
        KeyEnum[] keys = KeyEnum.values();
        for (KeyEnum key : keys) {
            UiEvent event = EventManager.selfCreateKeyEvent(key, modifiers, down, windowHandler);
            assertInstanceOf(KeyEvent.class, event);
            KeyEvent KeyEvent = (KeyEvent) event;
            assertEquals(key, KeyEvent.getKey());
            assertEquals(modifiers, KeyEvent.getModifiers());
            assertEquals(down, KeyEvent.isDown());
            assertEquals(windowHandler, KeyEvent.getWindowHandler());
        }
    }
}
