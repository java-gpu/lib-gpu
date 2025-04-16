package tech.gpu.lib.metal.util;

public class MetalNativeLoader {
    private static final String LIBRARY_NAME = "MetalBridge";
    private static boolean loaded = false;

    public static synchronized void load() {
        if (!loaded) {
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
        }
    }
}
