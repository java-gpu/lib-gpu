package tech.gpu.lib.graphics;

public enum TextureFilter {
    /**
     * Fetch the nearest texel that best maps to the pixel on screen.
     */
    Nearest,

    /**
     * Fetch four nearest texels that best maps to the pixel on screen.
     */
    Linear,

    /**
     * @see TextureFilter#LinearMipMapLinear
     */
    MipMap,

    /**
     * Fetch the best fitting image from the mip map chain based on the pixel/texel ratio and then sample the texels with a
     * nearest filter.
     */
    NearestMipMapNearest,

    /**
     * Fetch the best fitting image from the mip map chain based on the pixel/texel ratio and then sample the texels with a
     * linear filter.
     */
    LinearMipMapNearest,

    /**
     * Fetch the two best fitting images from the mip map chain and then sample the nearest texel from each of the two images,
     * combining them to the final output pixel.
     */
    NearestMipMapLinear,

    /**
     * Fetch the two best fitting images from the mip map chain and then sample the four nearest texels from each of the two
     * images, combining them to the final output pixel.
     */
    LinearMipMapLinear
}
