package framework.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import framework.exceptions.TestDataException;
import java.io.IOException;
import java.util.Objects;

/** Reads typed YAML fixtures when a concise human-authored data format is beneficial. */
public final class YamlDataReader {
    private final ObjectMapper mapper;
    public YamlDataReader() { this(new ObjectMapper(new YAMLFactory()).findAndRegisterModules()); }
    YamlDataReader(ObjectMapper mapper) { this.mapper = Objects.requireNonNull(mapper, "mapper must not be null"); }
    public <T> T read(String resource, Class<T> type) {
        try (var input = DataResourceLoader.open(resource)) { return mapper.readValue(input, type); }
        catch (IOException exception) { throw new TestDataException("Unable to read YAML data: " + resource, exception); }
    }
}
