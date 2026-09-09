package framework.config;

import framework.exceptions.ConfigurationException;
import java.util.Locale;

/** Supported local browser implementations. */
public enum Browser {
    CHROME, FIREFOX, EDGE;

    public static Browser from(String value) {
        try {
            return Browser.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ConfigurationException("Unsupported browser: " + value, exception);
        }
    }
}
