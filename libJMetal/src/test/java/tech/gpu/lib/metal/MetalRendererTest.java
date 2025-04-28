package tech.gpu.lib.metal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import tech.gpu.lib.jni.GpuInfo;
import tech.gpu.lib.jni.GpuManager;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.metal.jni.MetalRenderer;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetalRendererTest {

    @Test
    @EnabledOnOs(OS.MAC)
    void testCreateRenderer() throws PixelFormatNotSupportedException {
        try {
            List<GpuInfo> gpus = new LinkedList<>(GpuManager.gpuMap.values());
            assertFalse(gpus.isEmpty());
            MetalRenderer renderer = new MetalRenderer(0);
            assertNotNull(renderer);
            renderer.draw();
            renderer.release();
        } finally {
            GpuManager.releaseAllGpus();
        }
    }
}
