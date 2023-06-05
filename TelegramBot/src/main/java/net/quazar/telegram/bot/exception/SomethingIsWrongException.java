package net.quazar.telegram.bot.exception;

public class SomethingIsWrongException extends RuntimeException {
    public SomethingIsWrongException() {
    }

    public SomethingIsWrongException(String message) {
        super(message);
    }

    public SomethingIsWrongException(String message, Throwable cause) {
        super(message, cause);
    }

    public SomethingIsWrongException(Throwable cause) {
        super(cause);
    }

    public SomethingIsWrongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
