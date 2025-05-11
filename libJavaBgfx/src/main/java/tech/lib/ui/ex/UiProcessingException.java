package tech.lib.ui.ex;

public class UiProcessingException extends RuntimeException {
    public UiProcessingException() {
    }

    public UiProcessingException(String message) {
        super(message);
    }

    public UiProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UiProcessingException(Throwable cause) {
        super(cause);
    }

    public UiProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
