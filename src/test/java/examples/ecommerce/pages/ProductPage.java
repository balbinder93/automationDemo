package examples.ecommerce.pages;

import framework.actions.UiActions;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class ProductPage {
    private static final By PAGE = By.cssSelector("[data-testid='product-page']");
    private static final By ADD_TO_CART = By.cssSelector("[data-testid='add-to-cart']");
    private final WebDriver driver;
    private final UiActions actions;
    private final UiWaits waits;
    ProductPage(WebDriver driver, UiActions actions, UiWaits waits) { this.driver = driver; this.actions = actions; this.waits = waits; waits.waitForVisible(PAGE); }
    public String productName() { return waits.waitForVisible(By.cssSelector("[data-testid='product-name']")).getText(); }
    public CartPage addToCart() { actions.click(ADD_TO_CART); return new CartPage(driver, actions, waits); }
}
