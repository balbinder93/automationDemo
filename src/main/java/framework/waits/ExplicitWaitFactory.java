package framework.waits;

import framework.config.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Objects;

/** Produces explicit waits with the configured timeout; implicit waits remain zero by default. */
public final class ExplicitWaitFactory {
    public WebDriverWait create(WebDriver driver, DriverConfig config) {
        return new WebDriverWait(Objects.requireNonNull(driver, "driver must not be null"), Objects.requireNonNull(config, "config must not be null").explicitWait());
    }
}
