package framework.exceptions;

/** Indicates that a named UI action could not complete with the configured synchronization. */
public final class UiActionException extends FrameworkException {
    public UiActionException(String message, Throwable cause) { super(message, cause); }
}
