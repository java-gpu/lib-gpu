package tech.lib.ui.input;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.enu.AppConst;
import tech.lib.ui.data.Gamepad;

import java.awt.event.KeyEvent;
import java.util.Map;

@Data
@Slf4j
public class Input {

    private Map<String, InputBinding<?>> inputBindingsMap;

    private InputKeyboard keyboardInput;
    private InputMouse mouseInput;
    private Gamepad[] gamepad;
    private long windowHandler;

    public Input(long windowHandler) {
        this.windowHandler = windowHandler;
        keyboardInput = new InputKeyboard();
        mouseInput = new InputMouse();
        gamepad = new Gamepad[AppConst.ENTRY_CONFIG_MAX_GAMEPADS];
        for (int i = 0; i < gamepad.length; i++) {
            gamepad[i] = new Gamepad();
        }
        reset();
    }


    public void addBindings(String _name, InputBinding<?> _bindings) {
        inputBindingsMap.put(_name, _bindings);
    }

    public InputBinding<?> removeBindings(String _name) {
        return inputBindingsMap.remove(_name);
    }

    public void process(InputBinding<?> _bindings) {
        for (Map.Entry<String, InputBinding<?>> entry : inputBindingsMap.entrySet()) {
            InputBinding<?> binding = entry.getValue();
            var keyEvent = binding.getKey();
            var keyCode = keyEvent.getKeyCode();
            if (keyCode != KeyEvent.VK_HOME) {
                InputKeyboard.KeyState keyState = InputKeyboard.decodeKeyState(keyboardInput.getKeys().get(keyCode).getEncoded());
                int modifiers = keyState.getModifier();
                boolean down = keyState.isDown();

                if (binding.getFlags() == 1) {
                    if (down) {
                        if (modifiers == binding.getModifiers().getValue() && !keyboardInput.getOnce().get(keyCode)) {
                            executeBinding(binding);
                            keyboardInput.getOnce().put(keyCode, true);
                        }
                    } else {
                        keyboardInput.getOnce().put(keyCode, false);
                    }
                } else {
                    if (down && modifiers == binding.getModifiers().getValue()) {
                        executeBinding(binding);
                    }
                }
            }
        }
    }

    private void executeBinding(InputBinding<?> binding) {
        if (binding.getFn() != null) {
            binding.getFn().callback(binding.getUserData());
        } else {
            //TODO
            //cmdExec( (const char*)binding->m_userData);
            log.debug("Warning!! Empty binding callback!!");
        }
    }

    public void process() {
        for (Map.Entry<String, InputBinding<?>> entry : inputBindingsMap.entrySet()) {
            InputBinding<?> binding = entry.getValue();
            process(binding);
        }
    }

    public void reset() {
        mouseInput.reset();
        keyboardInput.reset();
        for (Gamepad gamepad : gamepad) {
            gamepad.reset();
        }
    }

    public boolean inputIsMouseLocked() {
        return mouseInput.isLock();
    }
}
