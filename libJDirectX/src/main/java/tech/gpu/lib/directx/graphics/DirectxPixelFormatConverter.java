package tech.gpu.lib.directx.graphics;

import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.PixelFormatConverter;

public class DirectxPixelFormatConverter implements PixelFormatConverter {

    @Override
    public PixelFormat fromNativeFormat(int formatVal) throws PixelFormatNotSupportedException {
        return switch (formatVal) {
            case 30 -> PixelFormat.LuminanceAlpha;   // DXGI_FORMAT_R8G8_UNORM
            case 56 -> PixelFormat.RGB565;           // DXGI_FORMAT_B5G6R5_UNORM
            case 115 -> PixelFormat.RGBA4444;         // DXGI_FORMAT_B4G4R4A4_UNORM
            case 28 -> PixelFormat.RGBA8888;         // DXGI_FORMAT_R8G8B8A8_UNORM
            case 29 -> PixelFormat.RGBA8_sRGB;       // DXGI_FORMAT_R8G8B8A8_UNORM_SRGB
            case 87 -> PixelFormat.BGRA8;            // DXGI_FORMAT_B8G8R8A8_UNORM
            case 10 -> PixelFormat.RGBA16Float;      // DXGI_FORMAT_R16G16B16A16_FLOAT
            case 2 -> PixelFormat.RGBA32Float;      // DXGI_FORMAT_R32G32B32A32_FLOAT
            case 24 -> PixelFormat.RGB10A2Unorm;     // DXGI_FORMAT_R10G10B10A2_UNORM
            case 40 -> PixelFormat.Depth32Float;     // DXGI_FORMAT_D32_FLOAT
            case 45 -> PixelFormat.Depth24_Stencil8; // DXGI_FORMAT_D24_UNORM_S8_UINT
            case 157 -> PixelFormat.ASTC_4x4_sRGB;    // DXGI_FORMAT_ASTC_4x4_UNORM
            case 71 -> PixelFormat.BC1_RGBA;         // DXGI_FORMAT_BC1_UNORM
            case 42 -> PixelFormat.R32Uint;          // DXGI_FORMAT_R32_UINT
            case 57 -> PixelFormat.RG32Uint;         // DXGI_FORMAT_R32G32_UINT
            case 65 -> PixelFormat.RGBA16Uint;       // DXGI_FORMAT_R16G16B16A16_UINT
            default -> throw new PixelFormatNotSupportedException("Unsupported DX format value: " + formatVal);
        };
    }

    @Override
    public int toNativeFormat(PixelFormat format) throws PixelFormatNotSupportedException {
        return switch (format) {
            case Alpha -> 28;  // DXGI_FORMAT_R8_UNORM
            case Intensity -> 29;  // DXGI_FORMAT_R16_UNORM
            case LuminanceAlpha -> 30;  // DXGI_FORMAT_R8G8_UNORM
            case RGB565 -> 56;  // DXGI_FORMAT_B5G6R5_UNORM
            case RGBA4444 -> 115; // DXGI_FORMAT_B4G4R4A4_UNORM
            case RGB888 -> 28;  // DXGI_FORMAT_R8G8B8A8_UNORM
            case RGBA8888 -> 28;  // DXGI_FORMAT_R8G8B8A8_UNORM
            case RGBA8_sRGB -> 29;  // DXGI_FORMAT_R8G8B8A8_UNORM_SRGB
            case BGRA8 -> 87;  // DXGI_FORMAT_B8G8R8A8_UNORM
            case RGBA16Float -> 10;  // DXGI_FORMAT_R16G16B16A16_FLOAT
            case RGBA32Float -> 2;   // DXGI_FORMAT_R32G32B32A32_FLOAT
            case RGB10A2Unorm -> 24;  // DXGI_FORMAT_R10G10B10A2_UNORM
            case Depth32Float -> 40;  // DXGI_FORMAT_D32_FLOAT
            case Depth24_Stencil8 -> 45;// DXGI_FORMAT_D24_UNORM_S8_UINT
            case Stencil8 -> 65;  // DXGI_FORMAT_S8_UINT
            case ASTC_4x4_sRGB -> 157; // DXGI_FORMAT_ASTC_4x4_UNORM
            case BC1_RGBA -> 71;  // DXGI_FORMAT_BC1_UNORM
            case R32Uint -> 42;  // DXGI_FORMAT_R32_UINT
            case RG32Uint -> 57;  // DXGI_FORMAT_R32G32_UINT
            case RGBA16Uint -> 65;  // DXGI_FORMAT_R16G16B16A16_UINT
            default -> throw new PixelFormatNotSupportedException("Unsupported DX pixel format: " + format);
        };
    }
}
