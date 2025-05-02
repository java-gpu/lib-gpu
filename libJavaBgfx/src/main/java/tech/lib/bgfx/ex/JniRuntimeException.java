package tech.lib.bgfx.ex;

public class JniRuntimeException extends RuntimeException {
    public JniRuntimeException() {
    }

    public JniRuntimeException(String message) {
        super(message);
    }

    public JniRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JniRuntimeException(Throwable cause) {
        super(cause);
    }

    public JniRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
