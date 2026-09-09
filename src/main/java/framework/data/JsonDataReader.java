package framework.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.exceptions.TestDataException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/** Reads typed JSON fixtures from the classpath. */
public final class JsonDataReader {
    private final ObjectMapper mapper;
    public JsonDataReader() { this(new ObjectMapper().findAndRegisterModules()); }
    JsonDataReader(ObjectMapper mapper) { this.mapper = Objects.requireNonNull(mapper, "mapper must not be null"); }
    public <T> List<T> readList(String resource, Class<T> itemType) {
        try (var input = DataResourceLoader.open(resource)) {
            return mapper.readValue(input, mapper.getTypeFactory().constructCollectionType(List.class, itemType));
        } catch (IOException exception) { throw new TestDataException("Unable to read JSON data: " + resource, exception); }
    }
    public <T> T read(String resource, Class<T> type) {
        try (var input = DataResourceLoader.open(resource)) { return mapper.readValue(input, type); }
        catch (IOException exception) { throw new TestDataException("Unable to read JSON data: " + resource, exception); }
    }
}
