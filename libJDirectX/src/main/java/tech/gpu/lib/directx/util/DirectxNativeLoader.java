package tech.gpu.lib.directx.util;

public class DirectxNativeLoader {
    private static final String LIBRARY_NAME = "DirectxBridge";
    private static boolean loaded = false;

    public static synchronized void load() {
        if (!loaded) {
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
        }
    }
}
