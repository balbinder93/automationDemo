package framework.config;

import framework.exceptions.ConfigurationException;
import org.testng.annotations.Test;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DriverConfigTest {
    @Test
    void retainsWindowArgumentsDownloadsAndTimeouts() {
        DriverConfig config = new DriverConfig("local", URI.create("https://example.test"), Browser.CHROME, ExecutionMode.LOCAL, Optional.empty(), 2,
                Duration.ofSeconds(20), Duration.ZERO, Duration.ofSeconds(5), Duration.ofSeconds(15),
                new BrowserOptions(true, false, 1440, 900, List.of("--lang=en-US"), Path.of("target/downloads")));
        assertThat(config.browserOptions().hasWindowSize()).isTrue();
        assertThat(config.browserOptions().arguments()).containsExactly("--lang=en-US");
        assertThat(config.implicitWait()).isZero();
        assertThat(config.explicitWait()).isEqualTo(Duration.ofSeconds(5));
    }
    @Test
    void rejectsNegativeImplicitWait() {
        assertThatThrownBy(() -> new DriverConfig("local", URI.create("https://example.test"), Browser.CHROME, ExecutionMode.LOCAL, Optional.empty(), 1,
                Duration.ofSeconds(20), Duration.ofSeconds(-1), Duration.ofSeconds(5), Duration.ofSeconds(15),
                new BrowserOptions(true, true, 0, 0, List.of(), Path.of("target/downloads"))))
                .isInstanceOf(ConfigurationException.class).hasMessageContaining("implicit-wait");
    }
}
