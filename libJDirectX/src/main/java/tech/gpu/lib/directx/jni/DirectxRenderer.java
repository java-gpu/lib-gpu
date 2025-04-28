package tech.gpu.lib.directx.jni;

import lombok.Getter;
import tech.gpu.lib.directx.util.DirectxNativeLoader;

@Getter
public class DirectxRenderer {
    static {
        DirectxNativeLoader.load();
    }

    private final long nativeHandle;

    public DirectxRenderer(int gpuIndex) {
        nativeHandle = init(gpuIndex);
    }

    private native long init(int gpuIndex);

    public native void draw();

//  public native void renderSprite(String imagePath, float x, float y, float width, float height);

    public native void release();
}
