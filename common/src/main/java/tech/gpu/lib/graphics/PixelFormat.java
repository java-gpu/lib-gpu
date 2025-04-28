package tech.gpu.lib.graphics;

import lombok.Getter;
import tech.gpu.lib.ex.GpuRuntimeException;
import tech.gpu.lib.graphics.g2d.Gdx2DPixmap;

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

    public static int toGdx2DPixmapFormat(PixelFormat format) {
        if (format == Alpha) return Gdx2DPixmap.GDX2D_FORMAT_ALPHA;
        if (format == Intensity) return Gdx2DPixmap.GDX2D_FORMAT_ALPHA;
        if (format == LuminanceAlpha) return Gdx2DPixmap.GDX2D_FORMAT_LUMINANCE_ALPHA;
        if (format == RGB565) return Gdx2DPixmap.GDX2D_FORMAT_RGB565;
        if (format == RGBA4444) return Gdx2DPixmap.GDX2D_FORMAT_RGBA4444;
        if (format == RGB888) return Gdx2DPixmap.GDX2D_FORMAT_RGB888;
        if (format == RGBA8888) return Gdx2DPixmap.GDX2D_FORMAT_RGBA8888;
        throw new GpuRuntimeException("Unknown Format: " + format);
    }

    public static PixelFormat fromGdx2DPixmapFormat(int format) {
        if (format == Gdx2DPixmap.GDX2D_FORMAT_ALPHA) return Alpha;
        if (format == Gdx2DPixmap.GDX2D_FORMAT_LUMINANCE_ALPHA) return LuminanceAlpha;
        if (format == Gdx2DPixmap.GDX2D_FORMAT_RGB565) return RGB565;
        if (format == Gdx2DPixmap.GDX2D_FORMAT_RGBA4444) return RGBA4444;
        if (format == Gdx2DPixmap.GDX2D_FORMAT_RGB888) return RGB888;
        if (format == Gdx2DPixmap.GDX2D_FORMAT_RGBA8888) return RGBA8888;
        throw new GpuRuntimeException("Unknown Gdx2DPixmap Format: " + format);
    }

    public static int toGlFormat(PixelFormat format) {
        return Gdx2DPixmap.toGlFormat(toGdx2DPixmapFormat(format));
    }

    public static int toGlType(PixelFormat format) {
        return Gdx2DPixmap.toGlType(toGdx2DPixmapFormat(format));
    }
}
