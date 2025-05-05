package tech.lib.bgfx.jni;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.lib.bgfx.util.JniLogger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class GpuManagerTest {

    @BeforeAll
    public static void setUp() {
        var jniLogger = JniLogger.getInstance();
        jniLogger.setStopThread(false);
        log.debug("🔍 Library path: {}", System.getProperty("java.library.path"));
    }

    @AfterAll
    public static void tearDown() {
        var jniLogger = JniLogger.getInstance();
        jniLogger.setStopThread(true);
        GpuManager.releaseAllGpus();
    }

    @Test
    void testGetGpuInfo() {
        List<GpuInfo> gpus = new LinkedList<>(GpuManager.gpuMap.values());
        assertFalse(gpus.isEmpty());
        String systemDefaultGPUName = GpuManager.getSystemDefaultGPUName();
        assertNotEquals("Unknown GPU", systemDefaultGPUName, systemDefaultGPUName);
        var allGpuNames = GpuManager.getAllGPUNames();
        assertTrue(Arrays.stream(allGpuNames).noneMatch(g -> g.equalsIgnoreCase("Unknown GPU")),
                Arrays.toString(allGpuNames));
        var firstGpu = gpus.getFirst().getName();
        assertNotEquals("Unknown GPU", firstGpu, firstGpu);
    }

}
