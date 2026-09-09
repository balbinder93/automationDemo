package framework.exceptions;

/** Indicates that a UI condition did not become true before the explicit timeout elapsed. */
public final class UiWaitException extends FrameworkException {
    public UiWaitException(String message, Throwable cause) { super(message, cause); }
}
