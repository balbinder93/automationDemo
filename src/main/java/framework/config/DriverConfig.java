package framework.config;

import framework.exceptions.ConfigurationException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/** Immutable configuration consumed by the browser driver layer. */
public record DriverConfig(
        String environment,
        URI baseUrl,
        Browser browser,
        ExecutionMode executionMode,
        Optional<URI> remoteUrl,
        int threads,
        Duration pageLoadTimeout,
        Duration implicitWait,
        Duration explicitWait,
        Duration scriptTimeout,
        BrowserOptions browserOptions) {
    public DriverConfig {
        environment = requireText(environment, "environment");
        baseUrl = requireAbsoluteUri(baseUrl, "baseUrl");
        browser = Objects.requireNonNull(browser, "browser must not be null");
        executionMode = Objects.requireNonNull(executionMode, "executionMode must not be null");
        remoteUrl = Objects.requireNonNull(remoteUrl, "remoteUrl must not be null");
        if (executionMode != ExecutionMode.LOCAL && remoteUrl.isEmpty()) throw new ConfigurationException("remote-url is required for " + executionMode);
        if (remoteUrl.isPresent()) requireAbsoluteUri(remoteUrl.get(), "remoteUrl");
        if (threads < 1) throw new ConfigurationException("threads must be at least one");
        pageLoadTimeout = positive(pageLoadTimeout, "page-load-timeout");
        implicitWait = nonNegative(implicitWait, "implicit-wait");
        explicitWait = positive(explicitWait, "explicit-wait");
        scriptTimeout = positive(scriptTimeout, "script-timeout");
        browserOptions = Objects.requireNonNull(browserOptions, "browserOptions must not be null");
    }
    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) throw new ConfigurationException(name + " must not be blank");
        return value;
    }
    private static URI requireAbsoluteUri(URI value, String name) {
        if (value == null || value.getScheme() == null || value.getHost() == null) throw new ConfigurationException(name + " must be an absolute URL");
        return value;
    }
    private static Duration positive(Duration value, String name) {
        if (value == null || value.isZero() || value.isNegative()) throw new ConfigurationException(name + " must be greater than zero");
        return value;
    }
    private static Duration nonNegative(Duration value, String name) {
        if (value == null || value.isNegative()) throw new ConfigurationException(name + " must not be negative");
        return value;
    }
}
