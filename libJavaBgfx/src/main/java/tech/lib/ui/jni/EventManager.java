package tech.lib.ui.jni;

import tech.lib.bgfx.jni.JniLoader;
import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.enu.SuspendState;
import tech.lib.ui.event.AppEvent;
import tech.lib.ui.event.SuspendEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {

    private static final Map<Long, SystemEventQueue> systemEventQueueMap = new HashMap<>();
    private static final Set<Long> windowRegisteredForSuspendEvent = new HashSet<>();
    private static boolean pollingGamePad = false;
    private static final Set<Long> windowRegisteredForGamepadEvent = new HashSet<>();

    static {
        JniLoader.loadBgFxJni();
    }

    public static SystemEventQueue getWindowEventQueue(long windowHandler) {
        if (!systemEventQueueMap.containsKey(windowHandler)) {
            var eventQueue = new SystemEventQueue();
            systemEventQueueMap.put(windowHandler, eventQueue);
            return eventQueue;
        }
        return systemEventQueueMap.get(windowHandler);
    }

    public static AppEvent pollUiEvent(long windowHandler) {
        return getWindowEventQueue(windowHandler).pollEvent();
    }

    public static SystemEventQueue removeWindowEventQueue(long windowHandler) {
        return systemEventQueueMap.remove(windowHandler);
    }

    public static void pushUiEvent(long windowHandler, AppEvent appEvent) {
        getWindowEventQueue(windowHandler).addLast(appEvent);
    }

    public static void pushUiEvent(AppEvent appEvent) {
        pushUiEvent(appEvent.getWindowHandler(), appEvent);
    }

    /**
     * This method will be called from JNI.
     *
     * @param state SuspendState
     */
    public static void pushSuspendState(SuspendState state) {
        for (Long windowHandler : windowRegisteredForSuspendEvent) {
            getWindowEventQueue(windowHandler).addLast(new SuspendEvent(state, windowHandler));
        }
    }

    /**
     * Generate a fake AxisEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param axis           GamepadAxis
     * @param value          Value
     * @param gamepadHandler Gamepad Handler Pointer
     * @param windowHandler  Window Handler Pointer
     * @return An AxisEvent
     */
    public native static AppEvent selfCreateAxisEvent(GamepadAxis axis, float value, long gamepadHandler, long windowHandler);

    /**
     * Generate a fake GamepadEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param connected      Connected or not
     * @param gamepadHandler Gamepad Handler Pointer
     * @param windowHandler  Window Handler Pointer
     * @return An GamepadEvent
     */
    public native static AppEvent selfCreateGamepadEvent(boolean connected, long gamepadHandler, long windowHandler);

    /**
     * Generate a fake DropFileEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param filePath      File absolute path
     * @param windowHandler Window Handler Pointer
     * @return An DropFileEvent
     */
    public native static AppEvent selfCreateDropFileEvent(String filePath, long windowHandler);

    /**
     * Generate a fake SizeEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param width         Width
     * @param height        Height
     * @param windowHandler Window Handler Pointer
     * @return An SizeEvent
     */
    public native static AppEvent selfCreateSizeEvent(int width, int height, long windowHandler);

    /**
     * Generate a fake SuspendEvent. This method usually use for testing purpose since we need to implement for multiple platform.
     *
     * @param state         SuspendState
     * @param windowHandler Window Handler Pointer
     * @return An SuspendEvent
     */
    public native static AppEvent selfCreateSuspendEvent(SuspendState state, long windowHandler);

    /**
     * Register to receive suspend events.
     *
     * @param windowHandler Window handler pointer.
     */
    public static void registerForSuspendEvents(long windowHandler) {
        if (!windowRegisteredForSuspendEvent.contains(windowHandler)) {
            registerForNativeSuspendEvents(windowHandler);
            windowRegisteredForSuspendEvent.add(windowHandler);
        }
    }

    /**
     * Register to receive suspend events.
     *
     * @param windowHandler Window handler pointer.
     */
    private static native void registerForNativeSuspendEvents(long windowHandler);

    /**
     * Un-Register to receive suspend events.
     *
     * @param windowHandler Window handler pointer.
     */
    public static void unregisterForSuspendEvents(long windowHandler) {
        if (windowRegisteredForSuspendEvent.contains(windowHandler)) {
            unregisterForNativeSuspendEvents(windowHandler);
            windowRegisteredForSuspendEvent.remove(windowHandler);
        }
    }

    /**
     * Un-Register to receive suspend events.
     *
     * @param windowHandler Window handler pointer.
     */
    private static native void unregisterForNativeSuspendEvents(long windowHandler);

    public static void startGamePadListening() {
        if (!pollingGamePad) {
            startGamePadNativeListening();
            pollingGamePad = true;
        }
    }

    private static native void startGamePadNativeListening();

    public static void stopGamePadListening() {
        if (pollingGamePad) {
            stopGamePadNativeListening();
            pollingGamePad = false;
        }
    }

    private static native void stopGamePadNativeListening();

    /**
     * Register to receive gamepad events.
     *
     * @param windowHandler Window handler pointer.
     */
    public static void registerForGamepadEvents(long windowHandler) {
        windowRegisteredForGamepadEvent.add(windowHandler);
    }

    /**
     * Un-Register to receive suspend events.
     *
     * @param windowHandler Window handler pointer.
     */
    public static void unregisterForGamepadEvents(long windowHandler) {
        windowRegisteredForGamepadEvent.remove(windowHandler);
    }

    /**
     * This method will be called from Gamepad event manager.
     *
     * @param event UIEvent
     */
    public static void pushGamepadEvent(AppEvent event) {
        for (Long windowHandler : windowRegisteredForGamepadEvent) {
            var ev = event.clone();
            ev.setWindowHandler(windowHandler);
            getWindowEventQueue(windowHandler).addLast(ev);
        }
    }
}
