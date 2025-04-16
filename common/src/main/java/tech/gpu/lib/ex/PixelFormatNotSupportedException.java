package tech.gpu.lib.ex;

public class PixelFormatNotSupportedException extends Exception {
    public PixelFormatNotSupportedException() {
    }

    public PixelFormatNotSupportedException(String message) {
        super(message);
    }

    public PixelFormatNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PixelFormatNotSupportedException(Throwable cause) {
        super(cause);
    }

    public PixelFormatNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
