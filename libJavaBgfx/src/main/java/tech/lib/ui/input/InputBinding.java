package tech.lib.ui.input;

import lombok.Data;
import tech.lib.ui.enu.Modifier;

import java.awt.event.KeyEvent;

@Data
public class InputBinding<T> {
    private KeyEvent key;
    private Modifier modifiers;
    private int flags;
    private InputBindingCallback fn;
    private T userData;

    interface InputBindingCallback {
        void callback(Object data);
    }
}
