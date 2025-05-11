package tech.lib.ui.data;

import lombok.Data;
import tech.lib.ui.enu.GamepadAxis;

import java.util.HashMap;
import java.util.Map;

@Data
public class Gamepad {
    Map<GamepadAxis, Integer> axises;

    public Gamepad() {
        reset();
    }

    public void reset() {
        axises = new HashMap<>();
        for (GamepadAxis axis : GamepadAxis.values()) {
            axises.put(axis, 0);
        }
    }

    void setAxis(GamepadAxis axis, int value) {
        axises.put(axis, value);
    }

    int getAxis(GamepadAxis axis) {
        return axises.get(axis);
    }
}
