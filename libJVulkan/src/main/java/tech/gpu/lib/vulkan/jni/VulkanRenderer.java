package tech.gpu.lib.vulkan.jni;

import lombok.Getter;
import tech.gpu.lib.vulkan.util.VulkanNativeLoader;

@Getter
public class VulkanRenderer {
    static {
        VulkanNativeLoader.load();
    }

    private final long nativeHandle;

    public VulkanRenderer(int gpuIndex) {
        nativeHandle = init(gpuIndex);
    }

    private native long init(int gpuIndex);

    public native void draw();

//  public native void renderSprite(String imagePath, float x, float y, float width, float height);

    public native void release();
}
