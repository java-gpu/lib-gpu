package tech.gpu.lib.utils;

public class CommonNativeLoader {
    private static final String LIBRARY_NAME = "GpuCommon";
    private static boolean loaded = false;

    public static synchronized void load() {
        if (!loaded) {
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
        }
    }
}
