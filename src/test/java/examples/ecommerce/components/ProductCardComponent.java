package examples.ecommerce.components;

import framework.actions.UiActions;
import framework.components.Component;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import java.util.Objects;

public final class ProductCardComponent implements Component {
    private final String productName;
    private final UiWaits waits;
    private final UiActions actions;
    public ProductCardComponent(String productName, UiWaits waits, UiActions actions) { this.productName = Objects.requireNonNull(productName); this.waits = Objects.requireNonNull(waits); this.actions = Objects.requireNonNull(actions); }
    private By root() { return By.cssSelector("[data-testid='product-card'][data-product-name='" + productName + "']"); }
    private By openProduct() { return By.cssSelector("[data-testid='product-card'][data-product-name='" + productName + "'] [data-testid='product-link']"); }
    @Override public void waitUntilReady() { waits.waitForVisible(root()); }
    public void open() { actions.click(openProduct()); }
}
