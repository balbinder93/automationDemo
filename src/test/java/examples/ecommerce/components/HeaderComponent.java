package examples.ecommerce.components;

import framework.actions.UiActions;
import framework.components.Component;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import java.util.Objects;

/** Example e-commerce header isolated from framework-core abstractions. */
public final class HeaderComponent implements Component {
    private static final By ROOT = By.cssSelector("[data-testid='site-header']");
    private static final By CART_LINK = By.cssSelector("[data-testid='cart-link']");
    private final UiWaits waits;
    private final UiActions actions;
    public HeaderComponent(UiWaits waits, UiActions actions) { this.waits = Objects.requireNonNull(waits); this.actions = Objects.requireNonNull(actions); }
    @Override public void waitUntilReady() { waits.waitForVisible(ROOT); }
    public void openCart() { actions.click(CART_LINK); }
}
