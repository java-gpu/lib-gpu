package tech.lib.bgfx.enu;

import lombok.Getter;

@Getter
public enum ViewMode {
    Default(0),
    Sequential(1),
    DepthAscending(2),
    DepthDescending(3);

    private final int value;

    ViewMode(int value) {
        this.value = value;
    }

    // Optional: mapping back from int to enum
    public static ViewMode fromValue(int value) {
        for (ViewMode mode : values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown ViewMode value: " + value);
    }
}
