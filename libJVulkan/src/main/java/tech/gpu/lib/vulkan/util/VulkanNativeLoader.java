package tech.gpu.lib.vulkan.util;

public class VulkanNativeLoader {
    private static final String LIBRARY_NAME = "VulkanBridge";
    private static boolean loaded = false;

    public static synchronized void load() {
        if (!loaded) {
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
        }
    }
}
