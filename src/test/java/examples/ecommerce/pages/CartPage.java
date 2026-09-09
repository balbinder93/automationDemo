package examples.ecommerce.pages;

import framework.actions.UiActions;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class CartPage {
    private static final By PAGE = By.cssSelector("[data-testid='cart-page']");
    private final WebDriver driver;
    private final UiActions actions;
    private final UiWaits waits;
    CartPage(WebDriver driver, UiActions actions, UiWaits waits) { this.driver = driver; this.actions = actions; this.waits = waits; waits.waitForVisible(PAGE); }
    public int itemCount() { return Integer.parseInt(waits.waitForVisible(By.cssSelector("[data-testid='cart-item-count']")).getText()); }
    public String itemName(int position) { return waits.waitForVisible(By.cssSelector("[data-testid='cart-item']:nth-child(" + position + ") [data-testid='cart-item-name']")).getText(); }
    public void removeItem(String productName) { actions.click(By.cssSelector("[data-testid='cart-item'][data-product-name='" + productName + "'] [data-testid='remove-from-cart']")); }
}
