package framework.data;

import framework.models.CheckoutData;
import framework.models.ProductData;
import org.testng.annotations.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class DataReaderTest {
    @Test
    void readsTypedJsonRecords() {
        List<ProductData> products = new JsonDataReader().readList("testdata/products.json", ProductData.class);
        assertThat(products).extracting(ProductData::productName).containsExactly("Wireless Headphones", "Insulated Travel Mug");
    }
    @Test
    void readsHeaderBasedCsvRecords() {
        List<ProductData> products = new CsvDataReader().read("testdata/products.csv", row -> new ProductData(row.get("query"), row.get("productName"), row.get("expectedHomeHeading")));
        assertThat(products).hasSize(2).allSatisfy(product -> assertThat(product.expectedHomeHeading()).isEqualTo("Welcome"));
    }
    @Test
    void readsTypedYamlRecords() {
        CheckoutData checkout = new YamlDataReader().read("testdata/checkout.yaml", CheckoutData.class);
        assertThat(checkout).isEqualTo(new CheckoutData("United States", "90210", true));
    }
    @Test
    void generatesCollisionResistantData() {
        assertThat(UniqueTestDataFactory.user("example.test").email())
                .isNotEqualTo(UniqueTestDataFactory.user("example.test").email())
                .endsWith("@example.test");
    }
}
