package framework.config;

import framework.exceptions.ConfigurationException;
import java.util.Locale;

/** Browser execution targets supported by the framework. */
public enum ExecutionMode {
    LOCAL, REMOTE, GRID;

    public static ExecutionMode from(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ConfigurationException("Unsupported execution mode: " + value, exception);
        }
    }
}
