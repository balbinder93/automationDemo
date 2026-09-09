package examples.ecommerce.components;

import framework.actions.UiActions;
import framework.components.Component;
import framework.waits.UiWaits;
import org.openqa.selenium.By;
import java.util.Objects;

public final class SearchComponent implements Component {
    private static final By ROOT = By.cssSelector("[data-testid='search']");
    private static final By INPUT = By.cssSelector("[data-testid='search-input']");
    private static final By SUBMIT = By.cssSelector("[data-testid='search-submit']");
    private final UiWaits waits;
    private final UiActions actions;
    public SearchComponent(UiWaits waits, UiActions actions) { this.waits = Objects.requireNonNull(waits); this.actions = Objects.requireNonNull(actions); }
    @Override public void waitUntilReady() { waits.waitForVisible(ROOT); }
    public void searchFor(String query) { actions.clearAndType(INPUT, query); actions.click(SUBMIT); }
}
