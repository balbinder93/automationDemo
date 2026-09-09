package framework.config;

import framework.exceptions.ConfigurationException;
import org.testng.annotations.Test;
import java.net.URI;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FrameworkConfigurationTest {
    @Test
    void systemPropertiesTakePrecedenceOverEnvironmentAndProfile() {
        DriverConfig config = new FrameworkConfiguration(
                Map.of("env", "qa", "browser", "edge", "threads", "4"),
                Map.of("BROWSER", "firefox", "HEADLESS", "false")).load();
        assertThat(config.environment()).isEqualTo("qa");
        assertThat(config.browser()).isEqualTo(Browser.EDGE);
        assertThat(config.threads()).isEqualTo(4);
        assertThat(config.baseUrl().toString()).isEqualTo("https://qa.example.test");
        assertThat(config.browserOptions().headless()).isFalse();
    }
    @Test
    void environmentVariablesOverrideTheProfile() {
        DriverConfig config = new FrameworkConfiguration(Map.of("env", "staging"), Map.of("EXECUTION", "remote", "REMOTE_URL", "http://grid.example.test:4444")).load();
        assertThat(config.executionMode()).isEqualTo(ExecutionMode.REMOTE);
        assertThat(config.remoteUrl()).hasValue(URI.create("http://grid.example.test:4444"));
    }
    @Test
    void rejectsRemoteExecutionWithoutRemoteUrl() {
        assertThatThrownBy(() -> new FrameworkConfiguration(Map.of("execution", "grid"), Map.of()).load())
                .isInstanceOf(ConfigurationException.class).hasMessageContaining("remote-url");
    }
    @Test
    void rejectsUnknownBrowser() {
        assertThatThrownBy(() -> Browser.from("safari"))
                .isInstanceOf(ConfigurationException.class).hasMessageContaining("Unsupported browser");
    }
}
