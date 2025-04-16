package tech.gpu.lib.graphics;

import tech.gpu.lib.ex.PixelFormatNotSupportedException;

public interface PixelFormatConverter {

    PixelFormat fromNativeFormat(int formatVal) throws PixelFormatNotSupportedException;

    int toNativeFormat(PixelFormat format) throws PixelFormatNotSupportedException;

    default PixelFormat getDefaultFormat() {
        return PixelFormat.RGBA8888;
    }

}
