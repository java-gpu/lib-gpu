package tech.gpu.lib.graphics;

public enum TextureWrap {
    MirroredRepeat, ClampToEdge, Repeat, ClampToBorder,
    /**
     * Vulkan only.
     */
    MirrorClampToEdge
}
