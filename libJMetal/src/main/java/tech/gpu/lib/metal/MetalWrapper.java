package tech.gpu.lib.metal;

import tech.gpu.lib.common.GpuInfo;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.Texture;

import java.util.Arrays;

public class MetalWrapper {

    public static void main(String[] args) throws PixelFormatNotSupportedException {
        try {
            System.out.println("Default device: " + GpuInfo.getSystemDefaultGPUName());
            System.out.println("All GPUs: " + Arrays.toString(GpuInfo.getAllGPUNames()));
            System.out.println("GPU count: " + GpuInfo.getGPUCount());
            System.out.println("GPU at index 0: " + GpuInfo.getGPUNameByPointer(GpuInfo.getGPUPointerAtIndex(0)));
        } finally {
            GpuInfo.releaseAllGpus();
        }

        MetalRenderer renderer = new MetalRenderer(0);
        renderer.draw();
        // Render a sprite from a file at position (100, 100) with size (200, 200)
//        renderer.renderSprite("curl-logs.png", 100, 100, 200, 200);
        renderer.release();

        MetalApplication metalApplication = new MetalApplication();
        Texture texture = new Texture(metalApplication, 100, 100, PixelFormat.RGBA8888);
        texture.release();
    }
}