package tech.gpu.lib.ex;

public class GpuRuntimeException extends RuntimeException {
    public GpuRuntimeException() {
    }

    public GpuRuntimeException(String message) {
        super(message);
    }

    public GpuRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GpuRuntimeException(Throwable cause) {
        super(cause);
    }

    public GpuRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
