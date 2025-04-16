package tech.gpu.lib.metal;

import tech.gpu.lib.GpuInfo;
import tech.gpu.lib.GpuManager;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.Texture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MetalWrapper {

    public static void main(String[] args) throws PixelFormatNotSupportedException {
        try {
            List<GpuInfo> gpus = new LinkedList<>(GpuManager.gpuMap.values());
            System.out.println("Default device: " + GpuManager.getSystemDefaultGPUName());
            System.out.println("All GPUs: " + Arrays.toString(GpuManager.getAllGPUNames()));
            System.out.println("GPU count: " + gpus.size());
            System.out.println("GPU at index 0: " + gpus.getFirst().getName());
        } finally {
            GpuManager.releaseAllGpus();
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