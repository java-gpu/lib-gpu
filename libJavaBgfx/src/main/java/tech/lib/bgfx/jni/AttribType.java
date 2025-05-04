package tech.lib.bgfx.jni;

import lombok.Getter;

@Getter
public enum AttribType {
    UINT8(0),
    UINT10(1),
    INT16(2),
    HALF(3),
    FLOAT(4),
    COUNT(5); // Not used in layout but included for completeness

    public final int value;

    AttribType(int value) {
        this.value = value;
    }
}