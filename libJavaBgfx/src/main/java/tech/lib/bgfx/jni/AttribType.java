package tech.lib.bgfx.jni;

import lombok.Getter;

@Getter
public enum AttribType {
    UINT8(0), UINT10(1), INT16(2), HALF(3), FLOAT(4);

    public final int value;

    AttribType(int value) {
        this.value = value;
    }
}