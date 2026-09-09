package framework.pages;

import framework.actions.UiActions;
import framework.waits.UiWaits;
import org.openqa.selenium.WebDriver;
import java.time.Duration;
import java.util.Objects;

/** Minimal shared page support for driver, actions, and readiness synchronization. */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final UiWaits waits;
    protected final UiActions actions;
    protected BasePage(WebDriver driver, Duration explicitWait) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        this.waits = new UiWaits(driver, explicitWait);
        this.actions = new UiActions(driver, waits, explicitWait);
    }
    protected final void waitForDocumentReady() { waits.waitForPageReady(); }
}
