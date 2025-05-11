package tech.lib.ui.input;

import lombok.Data;
import tech.lib.bgfx.enu.AppConst;
import tech.lib.ui.enu.MouseButton;

import java.util.HashMap;
import java.util.Map;

@Data
public class InputMouse {
    private int[] absolutes;
    private float[] norms;
    private int wheel;
    private Map<MouseButton, Integer> buttons;
    private int width;
    private int height;
    private int wheelDelta;
    private boolean lock;

    public InputMouse() {
        this.width = AppConst.ENTRY_DEFAULT_WIDTH;
        this.height = AppConst.ENTRY_DEFAULT_HEIGHT;
        this.wheelDelta = AppConst.ENTRY_DEFAULT_WHEEL_DATA;
        this.lock = false;
        this.absolutes = new int[3];
        reset();
    }

    public void reset() {
        norms = new float[]{0, 0, 0};
        buttons = new HashMap<>();
        for (MouseButton button : MouseButton.values()) {
            buttons.put(button, 0);
        }
    }

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPos(int _mx, int _my, int _mz) {
        this.absolutes[0] = _mx;
        this.absolutes[1] = _my;
        this.absolutes[2] = _mz;
        norms[0] = (float) (_mx / width);
        norms[1] = (float) (_my / height);
        norms[2] = (float) (_mz / wheelDelta);
    }

    public void setButtonState(MouseButton button, int _state) {
        buttons.put(button, _state);
    }
}
