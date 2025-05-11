package tech.lib.bgfx.enu;

import lombok.Getter;

@Getter
public enum BgfxResetFlag {
    NONE(0x00000000),
    FULLSCREEN(0x00000001),
    MSAA_X2(0x00000010),
    MSAA_X4(0x00000020),
    MSAA_X8(0x00000030),
    MSAA_X16(0x00000040),
    VSYNC(0x00000080),
    MAXANISOTROPY(0x00000100),
    CAPTURING(0x00000200),
    HI_DPI(0x00000400),
    DEPTH_CLAMP(0x00000800),
    SUSPEND(0x00001000),
    FULLSCREEN_SHIFT(31), // Used internally for flags
    FLUSH_AFTER_RENDER(0x00002000),
    FLIP_AFTER_RENDER(0x00004000),
    SRGB_BACKBUFFER(0x00008000),
    HDR10(0x00010000),
    ALPHA_TO_COVERAGE(0x00020000);

    private final int value;

    BgfxResetFlag(int value) {
        this.value = value;
    }

    public static int combine(BgfxResetFlag... flags) {
        int combined = 0;
        for (BgfxResetFlag flag : flags) {
            combined |= flag.value;
        }
        return combined;
    }
}
