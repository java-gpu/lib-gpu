package tech.gpu.lib.graphics;

import lombok.Getter;

@Getter
public enum PixelFormat {
    Alpha(1), Intensity(2), LuminanceAlpha(3),
    RGB565(4), RGBA4444(5), RGB888(6),
    RGBA8888(7),

    /**
     * sRGB-correct color space
     */
    RGBA8_sRGB(8),
    /**
     * Often used in iOS/macOS back buffers
     */
    BGRA8(9),
    /**
     * High dynamic range (HDR)
     */
    RGBA16Float(10),
    /**
     * Scientific / HDR / high precision
     */
    RGBA32Float(11),
    /**
     * HDR-like with compact size
     */
    RGB10A2Unorm(12),
    /**
     * Depth testing
     */
    Depth32Float(13),
    /**
     * Depth + Stencil
     */
    Depth24_Stencil8(14),
    /**
     * Stencil-only rendering
     */
    Stencil8(15),

    /**
     * High quality texture compression
     */
    ASTC_4x4_sRGB(16),

    /**
     * DXT1-style block compression (macOS only)
     */
    BC1_RGBA(17),

    /**
     * Compute shaders, raw data.
     */
    R32Uint(18),

    /**
     * Raw buffers with 2 channels
     */
    RG32Uint(19),

    /**
     * Medium-precision packed integers
     */
    RGBA16Uint(20);

    private final int mappedFormatIndex;

    PixelFormat(int mappedFormatIndex) {
        this.mappedFormatIndex = mappedFormatIndex;
    }

    @Override
    public String toString() {
        return name();
    }
}
