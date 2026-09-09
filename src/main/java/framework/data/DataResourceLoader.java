package framework.data;

import framework.exceptions.TestDataException;
import java.io.InputStream;

final class DataResourceLoader {
    private DataResourceLoader() { }
    static InputStream open(String resource) {
        InputStream input = DataResourceLoader.class.getClassLoader().getResourceAsStream(resource);
        if (input == null) throw new TestDataException("Test data resource not found: " + resource);
        return input;
    }
}
