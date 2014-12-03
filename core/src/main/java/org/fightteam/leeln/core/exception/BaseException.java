package org.fightteam.leeln.core.exception;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class BaseException extends Exception{

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
