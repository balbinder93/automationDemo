package examples.ecommerce.pages;

import examples.ecommerce.components.ProductCardComponent;
import framework.actions.UiActions;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class SearchResultsPage {
    private static final By PAGE = By.cssSelector("[data-testid='search-results-page']");
    private final WebDriver driver;
    private final UiActions actions;
    private final UiWaits waits;
    SearchResultsPage(WebDriver driver, UiActions actions, UiWaits waits) { this.driver = driver; this.actions = actions; this.waits = waits; waits.waitForVisible(PAGE); }
    public String resultSummary() { return waits.waitForVisible(By.cssSelector("[data-testid='search-summary']")).getText(); }
    public ProductPage openProduct(String productName) { ProductCardComponent product = new ProductCardComponent(productName, waits, actions); product.waitUntilReady(); product.open(); return new ProductPage(driver, actions, waits); }
}
