package framework.driver;

import framework.config.DriverConfig;
import org.openqa.selenium.WebDriver;

/** Creates isolated browser sessions from framework configuration. */
@FunctionalInterface
public interface WebDriverFactory {
    WebDriver create(DriverConfig configuration);
}
