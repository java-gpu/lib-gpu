package tech.gpu.lib.metal.jni;

import lombok.Getter;
import tech.gpu.lib.metal.util.MetalNativeLoader;

@Getter
public class MetalRenderer {
    static {
        MetalNativeLoader.load();
    }

    private final long nativeHandle;

    public MetalRenderer(int gpuIndex) {
        nativeHandle = init(gpuIndex);
    }

    private native long init(int gpuIndex);

    public native void draw();

//  public native void renderSprite(String imagePath, float x, float y, float width, float height);

    public native void release();
}
