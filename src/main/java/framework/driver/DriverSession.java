package framework.driver;

import framework.config.DriverConfig;
import org.openqa.selenium.WebDriver;
import java.util.Objects;

/** Composes factory and thread-confined lifecycle without exposing driver construction to tests. */
public final class DriverSession implements AutoCloseable {
    private final DriverManager manager;
    private final WebDriverFactory factory;
    public DriverSession(DriverManager manager, WebDriverFactory factory) {
        this.manager = Objects.requireNonNull(manager, "manager must not be null");
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }
    public WebDriver start(DriverConfig config) {
        WebDriver driver = factory.create(config);
        manager.set(driver);
        return driver;
    }
    public WebDriver driver() { return manager.get(); }
    @Override public void close() { manager.quit(); }
}
