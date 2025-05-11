package tech.lib.bgfx.enu;

import lombok.Getter;

@Getter
public enum BgfxDebugFlag {
    NONE(0x00000000),
    WIREFRAME(0x00000001),
    IFH(0x00000002),
    STATS(0x00000004),
    TEXT(0x00000008),
    PROFILER(0x00000010);

    private final int value;

    BgfxDebugFlag(int value) {
        this.value = value;
    }

    // Combine multiple flags into a single int
    public static int combine(BgfxDebugFlag... flags) {
        int combined = 0;
        for (BgfxDebugFlag flag : flags) {
            combined |= flag.value;
        }
        return combined;
    }
}
