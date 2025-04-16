package tech.gpu.lib.metal.graphics;

import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.PixelFormatConverter;

public class MetalPixelFormatConverter implements PixelFormatConverter {

    @Override
    public PixelFormat fromNativeFormat(int formatVal) throws PixelFormatNotSupportedException {
        return switch (formatVal) {
            case 0x31 -> PixelFormat.RGB565;         // MTLPixelFormatB5G6R5Unorm = 49
            case 0x30 -> PixelFormat.RGBA4444;        // MTLPixelFormatABGR4Unorm = 48
            case 70 -> PixelFormat.RGBA8888;        // MTLPixelFormatRGBA8Unorm
            case 71 -> PixelFormat.RGBA8_sRGB;      // MTLPixelFormatRGBA8Unorm_sRGB
            case 80 -> PixelFormat.BGRA8;           // MTLPixelFormatBGRA8Unorm
            case 112 -> PixelFormat.RGBA16Float;     // MTLPixelFormatRGBA16Float
            case 125 -> PixelFormat.RGBA32Float;     // MTLPixelFormatRGBA32Float
            case 90 -> PixelFormat.RGB10A2Unorm;    // MTLPixelFormatRGB10A2Unorm
            case 252 -> PixelFormat.Depth32Float;    // MTLPixelFormatDepth32Float
            case 255 -> PixelFormat.Depth24_Stencil8;// MTLPixelFormatDepth24Unorm_Stencil8
            case 253 -> PixelFormat.Stencil8;        // MTLPixelFormatStencil8
            case 157 -> PixelFormat.ASTC_4x4_sRGB;   // MTLPixelFormatASTC_4x4_sRGB
            case 130 -> PixelFormat.BC1_RGBA;        // MTLPixelFormatBC1_RGBA
            case 103 -> PixelFormat.R32Uint;         // MTLPixelFormatR32Uint
            case 107 -> PixelFormat.RG32Uint;        // MTLPixelFormatRG32Uint
            case 115 -> PixelFormat.RGBA16Uint;      // MTLPixelFormatRGBA16Uint
            default ->
                    throw new PixelFormatNotSupportedException("Unsupported native Metal pixel format value: " + formatVal);
        };
    }

    @Override
    public int toNativeFormat(PixelFormat format) throws PixelFormatNotSupportedException {
        return switch (format) {
            case RGB565 -> 0x31; // MTLPixelFormatB5G6R5Unorm = 49
            case RGBA4444 -> 0x30; // MTLPixelFormatABGR4Unorm = 48
            case RGB888, RGBA8888 -> 70; // MTLPixelFormatRGBA8Unorm
            case RGBA8_sRGB -> 71; // MTLPixelFormatRGBA8Unorm_sRGB
            case BGRA8 -> 80; // MTLPixelFormatBGRA8Unorm
            case RGBA16Float -> 112; // MTLPixelFormatRGBA16Float
            case RGBA32Float -> 125; // MTLPixelFormatRGBA32Float
            case RGB10A2Unorm -> 90; // MTLPixelFormatRGB10A2Unorm
            case Depth32Float -> 252; // MTLPixelFormatDepth32Float
            case Depth24_Stencil8 -> 255; // MTLPixelFormatDepth24Unorm_Stencil8
            case Stencil8 -> 253; // MTLPixelFormatStencil8
            case ASTC_4x4_sRGB -> 157; // MTLPixelFormatASTC_4x4_sRGB
            case BC1_RGBA -> 130; // MTLPixelFormatBC1_RGBA
            case R32Uint -> 103; // MTLPixelFormatR32Uint
            case RG32Uint -> 107; // MTLPixelFormatRG32Uint
            case RGBA16Uint -> 115; // MTLPixelFormatRGBA16Uint
            default -> throw new PixelFormatNotSupportedException("Unsupported format: " + format);
        };
    }
}
