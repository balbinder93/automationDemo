package framework.data;

import framework.exceptions.TestDataException;

/** Reads non-secret runtime overrides with system properties taking precedence over environment variables. */
public final class RuntimeValueResolver {
    private RuntimeValueResolver() { }
    public static String required(String propertyName, String environmentName) {
        String value = optional(propertyName, environmentName);
        if (value == null || value.isBlank()) throw new TestDataException("Missing required runtime value: " + propertyName + " or " + environmentName);
        return value;
    }
    public static String optional(String propertyName, String environmentName) {
        String property = System.getProperty(propertyName);
        return property != null && !property.isBlank() ? property : System.getenv(environmentName);
    }
}
