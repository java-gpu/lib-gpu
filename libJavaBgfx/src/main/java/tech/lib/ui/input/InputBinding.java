package tech.lib.ui.input;

import lombok.Data;
import tech.lib.ui.enu.KeyEnum;
import tech.lib.ui.enu.Modifier;

@Data
public class InputBinding<T> {
    private KeyEnum key;
    private Modifier modifiers;
    private int flags;
    private InputBindingCallback fn;
    private T userData;

    interface InputBindingCallback {
        void callback(Object data);
    }
}
