package tech.lib.ui.enu;

import java.util.EnumSet;

public enum Modifier {
    None(0),
    LeftAlt(0x01),
    RightAlt(0x02),
    LeftCtrl(0x04),
    RightCtrl(0x08),
    LeftShift(0x10),
    RightShift(0x20),
    LeftMeta(0x40),
    RightMeta(0x80);

    private final int value;

    Modifier(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Static helper to combine modifiers into a bitmask
    public static int combine(Modifier... modifiers) {
        int result = 0;
        for (Modifier mod : modifiers) {
            result |= mod.value;
        }
        return result;
    }

    // Static helper to extract modifiers from a bitmask
    public static EnumSet<Modifier> fromBitmask(int bitmask) {
        EnumSet<Modifier> result = EnumSet.noneOf(Modifier.class);
        for (Modifier mod : Modifier.values()) {
            if ((bitmask & mod.value) != 0) {
                result.add(mod);
            }
        }
        return result;
    }
}
