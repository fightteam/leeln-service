package org.fightteam.leeln.core.exception;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class InterfaceNotFoundException extends BaseException {
    public InterfaceNotFoundException() {
        super();
    }

    public InterfaceNotFoundException(String message) {
        super(message);
    }

    public InterfaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterfaceNotFoundException(Throwable cause) {
        super(cause);
    }
}
