package tech.lib.bgfx.jni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.lib.bgfx.enu.RendererType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BgfxCaps {
    private RendererType rendererType;
    // BGFX_CAPS_* bitmask
    private long supported;
    // GPU vendor PCI ID
    private int vendorId;
    // GPU device ID
    private int deviceId;
    // true if NDC depth is [-1, 1]
    private boolean homogeneousDepth;
    // true if NDC origin is bottom left
    private boolean originBottomLeft;
    // Number of GPU
    private int numGPUs;
    // Limits
    private BgfxLimits limits;
    /// Supported texture format capabilities flags:
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_NONE` - Texture format is not supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_2D` - Texture format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_2D_SRGB` - Texture as sRGB format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_2D_EMULATED` - Texture format is emulated.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_3D` - Texture format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_3D_SRGB` - Texture as sRGB format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_3D_EMULATED` - Texture format is emulated.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_CUBE` - Texture format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_CUBE_SRGB` - Texture as sRGB format is supported.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_CUBE_EMULATED` - Texture format is emulated.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_VERTEX` - Texture format can be used from vertex shader.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_IMAGE_READ` - Texture format can be used as image and read from.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_IMAGE_WRITE` - Texture format can be used as image and written to.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_FRAMEBUFFER` - Texture format can be used as frame buffer.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_FRAMEBUFFER_MSAA` - Texture format can be used as MSAA frame buffer.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_MSAA` - Texture can be sampled as MSAA.
    ///   - `BGFX_CAPS_FORMAT_TEXTURE_MIP_AUTOGEN` - Texture format supports auto-generated mips.
    /// Please see {@link #BgfxFormatCaps}
    private int[] textureFormatCaps;
}
