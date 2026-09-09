package examples.ecommerce.pages;

import examples.ecommerce.components.FooterComponent;
import examples.ecommerce.components.HeaderComponent;
import examples.ecommerce.components.SearchComponent;
import framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

/** Example application page; locators are a documented contract for a testable e-commerce UI. */
public final class HomePage extends BasePage {
    private static final By PAGE = By.cssSelector("[data-testid='home-page']");
    private final HeaderComponent header = new HeaderComponent(waits, actions);
    private final SearchComponent search = new SearchComponent(waits, actions);
    private final FooterComponent footer = new FooterComponent(waits);
    public HomePage(WebDriver driver, Duration explicitWait) { super(driver, explicitWait); }
    public HomePage open(String baseUrl) { driver.get(baseUrl); waitForDocumentReady(); waits.waitForVisible(PAGE); header.waitUntilReady(); search.waitUntilReady(); footer.waitUntilReady(); return this; }
    public SearchResultsPage searchFor(String query) { search.searchFor(query); return new SearchResultsPage(driver, actions, waits); }
    public CartPage openCart() { header.openCart(); return new CartPage(driver, actions, waits); }
    public String heading() { return waits.waitForVisible(By.cssSelector("[data-testid='home-title']")).getText(); }
}
