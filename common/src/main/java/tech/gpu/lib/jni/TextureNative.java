package tech.gpu.lib.jni;

import tech.gpu.lib.graphics.TextureInfo;

import java.io.InputStream;

public class TextureNative {

    public static native TextureInfo loadTexture(long gpuPointer, String fullPath, boolean useMipMaps, int pixelFormat);

    public static native TextureInfo loadTextureFromStream(long gpuPointer, InputStream imageStream, boolean useMipMaps, int pixelFormat);

    public static native TextureInfo createEmptyTexture(long gpuPointer, int width, int height, int pixelFormat);

    // Release from memory
    public static native void release(long texturePointer);

    public static native TextureInfo getTextureInfo(long texturePointer);

}
