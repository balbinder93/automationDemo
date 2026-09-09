package examples.ecommerce.assertions;

import examples.ecommerce.pages.CartPage;
import examples.ecommerce.pages.HomePage;
import examples.ecommerce.pages.ProductPage;
import examples.ecommerce.pages.SearchResultsPage;
import static org.assertj.core.api.Assertions.assertThat;

/** Business-level assertions keep page internals and WebElements out of test classes. */
public final class EcommerceAssertions {
    private EcommerceAssertions() { }
    public static void homeWelcomes(HomePage page, String expectedHeading) { assertThat(page.heading()).as("home page heading").isEqualTo(expectedHeading); }
    public static void searchShows(SearchResultsPage page, String query) { assertThat(page.resultSummary()).as("search result summary").containsIgnoringCase(query); }
    public static void productIs(ProductPage page, String expectedName) { assertThat(page.productName()).as("product name").isEqualTo(expectedName); }
    public static void cartContains(CartPage page, String expectedProduct) { assertThat(page.itemCount()).as("cart item count").isPositive(); assertThat(page.itemName(1)).as("first cart product").isEqualTo(expectedProduct); }
}
