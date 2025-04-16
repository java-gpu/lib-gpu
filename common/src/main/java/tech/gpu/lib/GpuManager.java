package tech.gpu.lib;

import tech.gpu.lib.util.CommonNativeLoader;

import java.util.HashMap;
import java.util.Map;

public class GpuManager {

    public static Map<Integer, GpuInfo> gpuMap;
    public static GpuInfo gpuSelected = null;

    static {
        CommonNativeLoader.load();
        refreshGpuList();
        String defaultGpuName = getSystemDefaultGPUName();
        for (Map.Entry<Integer, GpuInfo> entry : gpuMap.entrySet()) {
            GpuInfo gpuInfo = entry.getValue();
            long pointer = gpuInfo.getPointer();
            if (defaultGpuName.equalsIgnoreCase(getGPUNameByPointer(pointer))) {
                gpuSelected = gpuInfo;
            }
        }
        if (gpuSelected == null) {
            gpuSelected = gpuMap.values().iterator().next();
        }
    }

    public static void refreshGpuList() {
        gpuMap = new HashMap<>();
        int count = getGPUCount();
        for (var i = 0; i < count; i++) {
            long pointer = getGPUPointerAtIndex(i);
            String name = getGPUNameByPointer(pointer);
            gpuMap.put(i, new GpuInfo(i, name, pointer));
        }
    }

    public static native String getSystemDefaultGPUName();

    public static native String[] getAllGPUNames();

    public static native int getGPUCount();

    public static native long getGPUPointerAtIndex(int index);

    public static native String getGPUNameByPointer(long pointer);

    public static native void releaseGpu(long pointer);

    public static void releaseAllGpus() {
        gpuMap.forEach((k, v) -> releaseGpu(v.getPointer()));
    }

    public static void changeSelectedGpu(GpuInfo selectedPointer) {
        gpuSelected = selectedPointer;
    }
}
