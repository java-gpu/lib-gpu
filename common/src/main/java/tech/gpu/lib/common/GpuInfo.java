package tech.gpu.lib.common;

import tech.gpu.lib.util.CommonNativeLoader;

import java.util.HashMap;
import java.util.Map;

public class GpuInfo {

    public static Map<Integer, Long> gpuMap;
    public static Long gpuSelected = -1L;

    static {
        CommonNativeLoader.load();
        refreshGpuList();
        String defaultGpuName = getSystemDefaultGPUName();
        for (Map.Entry<Integer, Long> entry : gpuMap.entrySet()) {
            long pointer = entry.getValue();
            if (defaultGpuName.equalsIgnoreCase(getGPUNameByPointer(pointer))) {
                gpuSelected = pointer;
            }
        }
        if (gpuSelected < 0) {
            gpuSelected = gpuMap.entrySet().iterator().next().getValue();
        }
    }

    public static void refreshGpuList() {
        gpuMap = new HashMap<>();
        int count = getGPUCount();
        for (var i = 0; i < count; i++) {
            long pointer = getGPUPointerAtIndex(i);
            gpuMap.put(i, pointer);
        }
    }

    public static native String getSystemDefaultGPUName();

    public static native String[] getAllGPUNames();

    public static native int getGPUCount();

    public static native long getGPUPointerAtIndex(int index);

    public static native String getGPUNameByPointer(long pointer);

    public static native void releaseGpu(long pointer);

    public static void releaseAllGpus() {
        gpuMap.forEach((k, v) -> releaseGpu(v));
    }

    public static void changeSelectedGpu(long selectedPointer) {
        gpuSelected = selectedPointer;
    }
}
