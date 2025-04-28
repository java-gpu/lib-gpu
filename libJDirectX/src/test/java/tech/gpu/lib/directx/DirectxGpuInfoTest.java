package tech.gpu.lib.directx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import tech.gpu.lib.jni.GpuInfo;
import tech.gpu.lib.jni.GpuManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DirectxGpuInfoTest {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testGetGpuInfo() {
        try {
            List<GpuInfo> gpus = new LinkedList<>(GpuManager.gpuMap.values());
            assertFalse(gpus.isEmpty());
            String systemDefaultGPUName = GpuManager.getSystemDefaultGPUName();
            assertNotEquals("Unknown GPU", systemDefaultGPUName, systemDefaultGPUName);
            var allGpuNames = GpuManager.getAllGPUNames();
            System.out.println("GPUs" + Arrays.toString(allGpuNames));
            assertTrue(Arrays.stream(allGpuNames).noneMatch(g -> g.equalsIgnoreCase("Unknown GPU")),
                    Arrays.toString(allGpuNames));
            var firstGpu = gpus.getFirst().getName();
            assertNotEquals("Unknown GPU", firstGpu, firstGpu);
        } finally {
            GpuManager.releaseAllGpus();
        }
    }

}
