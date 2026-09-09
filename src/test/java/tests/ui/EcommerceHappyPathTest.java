package tests.ui;

import examples.ecommerce.assertions.EcommerceAssertions;
import examples.ecommerce.pages.CartPage;
import examples.ecommerce.pages.HomePage;
import examples.ecommerce.pages.ProductPage;
import examples.ecommerce.pages.SearchResultsPage;
import framework.config.DriverConfig;
import framework.config.FrameworkConfiguration;
import framework.driver.DriverManager;
import framework.driver.DriverSession;
import framework.driver.SeleniumWebDriverFactory;
import org.testng.SkipException;
import org.testng.annotations.Test;

/** Example application workflow; enable only against an application implementing the documented data-testid contract. */
public class EcommerceHappyPathTest {
    @Test(groups = "ecommerce")
    void customerCanSearchOpenAProductAndAddItToTheCart() {
        if (!Boolean.getBoolean("framework.ecommerce.enabled")) throw new SkipException("Set -Dframework.ecommerce.enabled=true with a compatible base-url");
        DriverConfig config = FrameworkConfiguration.system().load();
        String query = System.getProperty("ecommerce.query", "wireless headphones");
        String product = System.getProperty("ecommerce.product", "Wireless Headphones");
        try (DriverSession session = new DriverSession(new DriverManager(), new SeleniumWebDriverFactory())) {
            session.start(config);
            HomePage home = new HomePage(session.driver(), config.explicitWait()).open(config.baseUrl().toString());
            EcommerceAssertions.homeWelcomes(home, System.getProperty("ecommerce.home-heading", "Welcome"));
            SearchResultsPage results = home.searchFor(query);
            EcommerceAssertions.searchShows(results, query);
            ProductPage productPage = results.openProduct(product);
            EcommerceAssertions.productIs(productPage, product);
            CartPage cart = productPage.addToCart();
            EcommerceAssertions.cartContains(cart, product);
        }
    }
}
