package tech.lib.bgfx.enu;

import lombok.Getter;

@Getter
public enum RendererType {
    NOOP(0), AGC(1), DIRECT3D11(2), DIRECT3D12(3), GNM(4), METAL(5), OPENGLES(7), OPENGL(7), VULKAN(6), AUTO(255);

    public final int value;

    RendererType(int value) {
        this.value = value;
    }

    public static RendererType fromInt(int value) {
        return values()[value];
    }
}
