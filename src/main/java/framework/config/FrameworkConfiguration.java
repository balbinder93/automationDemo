package framework.config;

import framework.exceptions.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/** Resolves immutable driver configuration with a documented, deterministic precedence. */
public final class FrameworkConfiguration {
    private static final String DEFAULT_ENVIRONMENT = "local";
    private final Map<String, String> systemProperties;
    private final Map<String, String> environmentVariables;

    public FrameworkConfiguration(Map<String, String> systemProperties, Map<String, String> environmentVariables) {
        this.systemProperties = Map.copyOf(Objects.requireNonNull(systemProperties, "systemProperties must not be null"));
        this.environmentVariables = Map.copyOf(Objects.requireNonNull(environmentVariables, "environmentVariables must not be null"));
    }

    public static FrameworkConfiguration system() {
        Properties properties = System.getProperties();
        return new FrameworkConfiguration(properties.stringPropertyNames().stream().collect(java.util.stream.Collectors.toMap(key -> key, properties::getProperty)), System.getenv());
    }

    public DriverConfig load() {
        String environment = systemProperties.get("env");
        if (!hasValue(environment)) environment = environmentVariables.get("ENV");
        if (!hasValue(environment)) environment = DEFAULT_ENVIRONMENT;
        validateEnvironment(environment);
        Properties defaults = loadProperties("config/framework.properties");
        Properties profile = loadProperties("config/" + environment + ".properties");
        return new DriverConfig(
                environment,
                uri("base-url", profile, defaults, "https://example.test"),
                Browser.from(first("browser", profile, defaults, "chrome")),
                ExecutionMode.from(first("execution", profile, defaults, "local")),
                optionalUri("remote-url", profile, defaults),
                integer("threads", profile, defaults, 1),
                seconds("page-load-timeout", profile, defaults, 30),
                seconds("implicit-wait", profile, defaults, 0),
                seconds("explicit-wait", profile, defaults, 10),
                seconds("script-timeout", profile, defaults, 30),
                new BrowserOptions(
                        bool("headless", profile, defaults, true),
                        bool("maximize-window", profile, defaults, true),
                        integer("window-width", profile, defaults, 0),
                        integer("window-height", profile, defaults, 0),
                        csv("browser-arguments", profile, defaults),
                        Path.of(first("download-directory", profile, defaults, "target/downloads"))
                ));
    }

    private String first(String key, Properties profile, Properties defaults, String fallback) {
        String property = systemProperties.get(key);
        if (hasValue(property)) return property;
        String environment = environmentVariables.get(key.toUpperCase(Locale.ROOT).replace('-', '_'));
        if (hasValue(environment)) return environment;
        String profileValue = profile.getProperty(key);
        if (hasValue(profileValue)) return profileValue;
        String defaultValue = defaults.getProperty(key);
        return hasValue(defaultValue) ? defaultValue : fallback;
    }
    private URI uri(String key, Properties profile, Properties defaults, String fallback) {
        try { return URI.create(first(key, profile, defaults, fallback)); }
        catch (IllegalArgumentException exception) { throw new ConfigurationException("Invalid " + key, exception); }
    }
    private Optional<URI> optionalUri(String key, Properties profile, Properties defaults) {
        String value = first(key, profile, defaults, "");
        return value.isBlank() ? Optional.empty() : Optional.of(uri(key, profile, defaults, ""));
    }
    private int integer(String key, Properties profile, Properties defaults, int fallback) {
        try { return Integer.parseInt(first(key, profile, defaults, Integer.toString(fallback))); }
        catch (NumberFormatException exception) { throw new ConfigurationException("Invalid integer for " + key, exception); }
    }
    private Duration seconds(String key, Properties profile, Properties defaults, int fallback) {
        return Duration.ofSeconds(integer(key, profile, defaults, fallback));
    }
    private boolean bool(String key, Properties profile, Properties defaults, boolean fallback) {
        String value = first(key, profile, defaults, Boolean.toString(fallback));
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        throw new ConfigurationException(key + " must be true or false");
    }
    private List<String> csv(String key, Properties profile, Properties defaults) {
        String value = first(key, profile, defaults, "");
        return value.isBlank() ? List.of() : Arrays.stream(value.split(",")).map(String::trim).filter(part -> !part.isEmpty()).toList();
    }
    private Properties loadProperties(String resource) {
        Properties properties = new Properties();
        try (InputStream input = FrameworkConfiguration.class.getClassLoader().getResourceAsStream(resource)) {
            if (input != null) properties.load(input);
            return properties;
        } catch (IOException exception) { throw new ConfigurationException("Unable to read " + resource, exception); }
    }
    private void validateEnvironment(String environment) {
        if (!List.of("local", "dev", "qa", "staging").contains(environment)) throw new ConfigurationException("Unsupported environment: " + environment);
    }
    private static boolean hasValue(String value) { return value != null && !value.isBlank(); }
}
