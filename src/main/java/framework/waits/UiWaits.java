package framework.waits;

import framework.exceptions.UiWaitException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Objects;

/** Condition-based synchronization facade. No fixed-duration sleeps are used. */
public final class UiWaits {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiWaits.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    public UiWaits(WebDriver driver, Duration timeout) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        this.wait = new WebDriverWait(driver, Objects.requireNonNull(timeout, "timeout must not be null"));
    }
    public WebElement waitForVisible(By locator) {
        return condition("visible: " + locator, () -> wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }
    public WebElement waitForClickable(By locator) {
        return condition("clickable: " + locator, () -> wait.until(ExpectedConditions.elementToBeClickable(locator)));
    }
    public void waitForText(By locator, String expectedText) {
        condition("text '" + expectedText + "' at " + locator, () -> wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText)));
    }
    public void waitForUrl(String expectedUrlFragment) {
        condition("URL containing '" + expectedUrlFragment + "'", () -> wait.until(ExpectedConditions.urlContains(expectedUrlFragment)));
    }
    public void waitForPageReady() {
        condition("document ready state", () -> wait.until(webDriver -> "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"))));
    }
    public void waitForElementToDisappear(By locator) {
        condition("disappearance: " + locator, () -> wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)));
    }
    private <T> T condition(String description, CheckedSupplier<T> supplier) {
        try {
            LOGGER.debug("Waiting for {}", description);
            return supplier.get();
        } catch (TimeoutException exception) {
            throw new UiWaitException("Timed out waiting for " + description + " at " + driver.getCurrentUrl(), exception);
        }
    }
    @FunctionalInterface private interface CheckedSupplier<T> { T get(); }
}
