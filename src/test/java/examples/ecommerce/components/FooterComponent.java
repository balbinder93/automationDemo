package examples.ecommerce.components;

import framework.components.Component;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import java.util.Objects;

public final class FooterComponent implements Component {
    private static final By ROOT = By.cssSelector("[data-testid='site-footer']");
    private final UiWaits waits;
    public FooterComponent(UiWaits waits) { this.waits = Objects.requireNonNull(waits); }
    @Override public void waitUntilReady() { waits.waitForVisible(ROOT); }
}
