package tech.lib.bgfx.jni;

import lombok.Getter;

@Getter
public enum Attrib {
    POSITION(0),
    NORMAL(1),
    TANGENT(2),
    BITANGENT(3),
    COLOR0(4),
    COLOR1(5),
    COLOR2(6),
    COLOR3(7),
    INDICES(8),
    WEIGHT(9),
    TEXCOORD0(10),
    TEXCOORD1(11),
    TEXCOORD2(12),
    TEXCOORD3(13),
    TEXCOORD4(14),
    TEXCOORD5(15),
    TEXCOORD6(16),
    TEXCOORD7(17),
    COUNT(18); // Not used in layout but included for completeness

    public final int value;

    Attrib(int value) {
        this.value = value;
    }
}
