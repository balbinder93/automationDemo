package framework.driver;

import framework.exceptions.DriverLifecycleException;
import org.openqa.selenium.WebDriver;
import java.util.Objects;

/** Thread-confined WebDriver storage for parallel test isolation. */
public final class DriverManager {
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public void set(WebDriver webDriver) {
        if (driver.get() != null) throw new DriverLifecycleException("A browser session already exists for this thread");
        driver.set(Objects.requireNonNull(webDriver, "driver must not be null"));
    }
    public WebDriver get() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) throw new DriverLifecycleException("No browser session exists for this thread");
        return webDriver;
    }
    public void quit() {
        WebDriver webDriver = driver.get();
        try { if (webDriver != null) webDriver.quit(); } finally { driver.remove(); }
    }
}
