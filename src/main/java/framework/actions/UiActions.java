package framework.actions;

import framework.exceptions.UiActionException;
import framework.waits.UiWaits;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

/** Reusable UI interactions with synchronization, logging, and contextual failures. */
public final class UiActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiActions.class);
    private final WebDriver driver;
    private final UiWaits waits;
    private final Duration timeout;

    public UiActions(WebDriver driver, UiWaits waits, Duration timeout) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        this.waits = Objects.requireNonNull(waits, "waits must not be null");
        this.timeout = Objects.requireNonNull(timeout, "timeout must not be null");
    }
    public void click(By locator) { action("click " + locator, () -> waits.waitForClickable(locator).click()); }
    public void type(By locator, String value) { action("type into " + locator, () -> waits.waitForVisible(locator).sendKeys(value)); }
    public void clearAndType(By locator, String value) { action("clear and type into " + locator, () -> { WebElement element = waits.waitForVisible(locator); element.clear(); element.sendKeys(value); }); }
    public void hover(By locator) { action("hover " + locator, () -> new Actions(driver).moveToElement(waits.waitForVisible(locator)).perform()); }
    public void scroll(By locator) { action("scroll to " + locator, () -> ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", waits.waitForVisible(locator))); }
    public void select(By locator, String visibleText) { action("select '" + visibleText + "' at " + locator, () -> new Select(waits.waitForVisible(locator)).selectByVisibleText(visibleText)); }
    public void upload(By locator, Path file) { action("upload " + file.getFileName() + " at " + locator, () -> waits.waitForVisible(locator).sendKeys(file.toAbsolutePath().toString())); }
    public void switchWindow(String expectedTitle) { action("switch window titled '" + expectedTitle + "'", () -> { for (String handle : driver.getWindowHandles()) { driver.switchTo().window(handle); if (expectedTitle.equals(driver.getTitle())) return; } throw new IllegalStateException("No window has title '" + expectedTitle + "'"); }); }
    public void switchFrame(By locator) { action("switch frame " + locator, () -> new WebDriverWait(driver, timeout).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator))); }
    public void acceptAlert() { alert("accept alert", Alert::accept); }
    public void dismissAlert() { alert("dismiss alert", Alert::dismiss); }
    private void alert(String name, AlertOperation operation) { action(name, () -> operation.apply(new WebDriverWait(driver, timeout).until(ExpectedConditions.alertIsPresent()))); }
    private void action(String description, Runnable operation) {
        try { LOGGER.info("UI action: {}", description); operation.run(); }
        catch (RuntimeException exception) { throw new UiActionException("Failed to " + description + " at " + driver.getCurrentUrl(), exception); }
    }
    @FunctionalInterface private interface AlertOperation { void apply(Alert alert); }
}
