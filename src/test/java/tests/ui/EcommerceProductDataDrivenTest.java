package tests.ui;

import examples.ecommerce.assertions.EcommerceAssertions;
import examples.ecommerce.pages.CartPage;
import examples.ecommerce.pages.HomePage;
import examples.ecommerce.pages.ProductPage;
import examples.ecommerce.pages.SearchResultsPage;
import framework.config.DriverConfig;
import framework.config.FrameworkConfiguration;
import framework.data.JsonDataReader;
import framework.driver.DriverManager;
import framework.driver.DriverSession;
import framework.driver.SeleniumWebDriverFactory;
import framework.models.ProductData;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** Data-driven UI example; product fixtures remain outside test logic. */
public class EcommerceProductDataDrivenTest {
    @DataProvider(name = "productData", parallel = true)
    public Object[][] productData() {
        return new JsonDataReader().readList("testdata/products.json", ProductData.class)
                .stream().map(product -> new Object[] {product}).toArray(Object[][]::new);
    }
    @Test(dataProvider = "productData", groups = "ecommerce")
    void customerCanAddConfiguredProductToCart(ProductData product) {
        if (!Boolean.getBoolean("framework.ecommerce.enabled")) throw new SkipException("Set -Dframework.ecommerce.enabled=true with a compatible base-url");
        DriverConfig config = FrameworkConfiguration.system().load();
        try (DriverSession session = new DriverSession(new DriverManager(), new SeleniumWebDriverFactory())) {
            session.start(config);
            HomePage home = new HomePage(session.driver(), config.explicitWait()).open(config.baseUrl().toString());
            EcommerceAssertions.homeWelcomes(home, product.expectedHomeHeading());
            SearchResultsPage results = home.searchFor(product.query());
            EcommerceAssertions.searchShows(results, product.query());
            ProductPage detail = results.openProduct(product.productName());
            EcommerceAssertions.productIs(detail, product.productName());
            CartPage cart = detail.addToCart();
            EcommerceAssertions.cartContains(cart, product.productName());
        }
    }
}
