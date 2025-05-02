package tech.lib.bgfx.jni;

import com.sun.jna.Native;
import tech.lib.bgfx.util.PlatformInfo;

import javax.swing.*;
import java.awt.*;

public class Bgfx {

    static {
        JniLoader.loadBgFxJni();
    }

    public static native boolean init(long windowHandlerPointer);

    public static native void shutdown();

    public static native void frame();

    public static long getNativeHandler(JFrame frame, Canvas canvas) {
        PlatformInfo platformInfo = PlatformInfo.getInstance();
        switch (platformInfo.getPlatformType()) {
            case MACOS:
                return getMacOSNativeHandlerFromCanvas(canvas);
            case LINUX:
                // TODO fix later
                return 0;
            case WINDOWS:
                return Native.getComponentPointer(frame).getLong(0);
            default:
                return 0;
        }
    }

    private static native long getMacOSNativeHandlerFromCanvas(Canvas canvas);
}
