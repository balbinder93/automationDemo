package framework.exceptions;

/** Base unchecked exception for actionable framework failures. */
public class FrameworkException extends RuntimeException {
    public FrameworkException(String message) { super(message); }
    public FrameworkException(String message, Throwable cause) { super(message, cause); }
}
