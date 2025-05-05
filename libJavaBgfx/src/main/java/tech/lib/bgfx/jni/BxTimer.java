package tech.lib.bgfx.jni;

public class BxTimer {
    static {
        JniLoader.loadBgFxJni();
    }
    
    public static native long getHPCounter();

    public static native long getHPFrequency();

    public static double getElapsedTimeSeconds(long start, long end) {
        return (end - start) / (double) getHPFrequency();
    }
}
