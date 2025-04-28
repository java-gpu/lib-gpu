package tech.gpu.lib.vulkan.graphics;

import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.PixelFormatConverter;

public class VulkanPixelFormatConverter implements PixelFormatConverter {

    @Override
    public PixelFormat fromNativeFormat(int formatVal) throws PixelFormatNotSupportedException {
        return switch (formatVal) {
            case 1 -> PixelFormat.Alpha;            // VK_FORMAT_R8_UNORM
            case 2 -> PixelFormat.Intensity;        // VK_FORMAT_R16_UNORM
            case 3 -> PixelFormat.LuminanceAlpha;   // VK_FORMAT_R8G8_UNORM
            case 50 -> PixelFormat.RGB565;           // VK_FORMAT_R5G6B5_UNORM_PACK16
            case 4 -> PixelFormat.RGBA4444;         // VK_FORMAT_R4G4B4A4_UNORM_PACK16
            case 37 -> PixelFormat.RGBA8888;         // VK_FORMAT_R8G8B8A8_UNORM
            case 43 -> PixelFormat.RGBA8_sRGB;       // VK_FORMAT_R8G8B8A8_SRGB
            case 44 -> PixelFormat.BGRA8;            // VK_FORMAT_B8G8R8A8_UNORM
            case 97 -> PixelFormat.RGBA16Float;      // VK_FORMAT_R16G16B16A16_SFLOAT
            case 109 -> PixelFormat.RGBA32Float;      // VK_FORMAT_R32G32B32A32_SFLOAT
            case 64 -> PixelFormat.RGB10A2Unorm;     // VK_FORMAT_A2B10G10R10_UNORM_PACK32
            case 126 -> PixelFormat.Depth32Float;     // VK_FORMAT_D32_SFLOAT
            case 129 -> PixelFormat.Depth24_Stencil8; // VK_FORMAT_D24_UNORM_S8_UINT
            case 127 -> PixelFormat.Stencil8;         // VK_FORMAT_S8_UINT
            case 157 -> PixelFormat.ASTC_4x4_sRGB;    // VK_FORMAT_ASTC_4x4_UNORM_BLOCK
            case 131 -> PixelFormat.BC1_RGBA;         // VK_FORMAT_BC1_RGBA_UNORM_BLOCK
            case 98 -> PixelFormat.R32Uint;          // VK_FORMAT_R32_UINT
            case 100 -> PixelFormat.RG32Uint;         // VK_FORMAT_R32G32_UINT
            case 90 -> PixelFormat.RGBA16Uint;       // VK_FORMAT_R16G16B16A16_UINT
            default -> throw new PixelFormatNotSupportedException("Unsupported Vulkan format value: " + formatVal);
        };
    }

    @Override
    public int toNativeFormat(PixelFormat format) throws PixelFormatNotSupportedException {
        return switch (format) {
            case Alpha -> 1;   // VK_FORMAT_R8_UNORM
            case Intensity -> 2;   // VK_FORMAT_R16_UNORM
            case LuminanceAlpha -> 3;   // VK_FORMAT_R8G8_UNORM
            case RGB565 -> 50;  // VK_FORMAT_R5G6B5_UNORM_PACK16
            case RGBA4444 -> 4;   // VK_FORMAT_R4G4B4A4_UNORM_PACK16
            case RGB888, RGBA8888 -> 37;  // VK_FORMAT_R8G8B8A8_UNORM
            case RGBA8_sRGB -> 43;  // VK_FORMAT_R8G8B8A8_SRGB
            case BGRA8 -> 44;  // VK_FORMAT_B8G8R8A8_UNORM
            case RGBA16Float -> 97;  // VK_FORMAT_R16G16B16A16_SFLOAT
            case RGBA32Float -> 109; // VK_FORMAT_R32G32B32A32_SFLOAT
            case RGB10A2Unorm -> 64;  // VK_FORMAT_A2B10G10R10_UNORM_PACK32
            case Depth32Float -> 126; // VK_FORMAT_D32_SFLOAT
            case Depth24_Stencil8 -> 129; // VK_FORMAT_D24_UNORM_S8_UINT
            case Stencil8 -> 127; // VK_FORMAT_S8_UINT
            case ASTC_4x4_sRGB -> 157; // VK_FORMAT_ASTC_4x4_UNORM_BLOCK
            case BC1_RGBA -> 131; // VK_FORMAT_BC1_RGBA_UNORM_BLOCK
            case R32Uint -> 98;  // VK_FORMAT_R32_UINT
            case RG32Uint -> 100; // VK_FORMAT_R32G32_UINT
            case RGBA16Uint -> 90;  // VK_FORMAT_R16G16B16A16_UINT
            default -> throw new PixelFormatNotSupportedException("Unsupported Vulkan pixel format: " + format);
        };
    }
}
