package tech.lib.ui.input;

import tech.lib.ui.enu.GamepadAxis;
import tech.lib.ui.event.AxisEvent;
import tech.lib.ui.event.GamepadEvent;
import tech.lib.ui.jni.EventManager;

public class GamepadEventManager {

    public static void onAxisEvent(int gamepadId, int axisId, float value) {
        GamepadAxis axis = GamepadAxis.fromId(axisId);
        EventManager.pushGamepadEvent(new AxisEvent(axis, value, gamepadId));
    }

    public static void onGamepadConnected(int gamepadId) {
        EventManager.pushGamepadEvent(new GamepadEvent(true, gamepadId));
    }

    public static void onGamepadDisconnected(int gamepadId) {
        EventManager.pushGamepadEvent(new GamepadEvent(false, gamepadId));
    }

}
