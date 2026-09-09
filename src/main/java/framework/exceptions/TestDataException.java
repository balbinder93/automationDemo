package framework.exceptions;

/** Indicates unreadable, invalid, or incomplete test data. */
public final class TestDataException extends FrameworkException {
    public TestDataException(String message, Throwable cause) { super(message, cause); }
    public TestDataException(String message) { super(message); }
}
