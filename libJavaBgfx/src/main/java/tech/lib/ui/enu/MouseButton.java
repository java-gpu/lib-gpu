package tech.lib.ui.enu;

import lombok.Getter;

@Getter
public enum MouseButton {
    None(0),
    Left(1),
    Middle(2),
    Right(3),
    OTHER(4);

    private final int code;

    MouseButton(int code) {
        this.code = code;
    }
}
