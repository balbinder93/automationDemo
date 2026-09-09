package framework.data;

import framework.exceptions.TestDataException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/** Reads header-based CSV fixtures into caller-supplied typed records. */
public final class CsvDataReader {
    public <T> List<T> read(String resource, Function<CSVRecord, T> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        try (var input = DataResourceLoader.open(resource);
             var reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             var parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            List<T> result = new ArrayList<>();
            for (CSVRecord record : parser) result.add(mapper.apply(record));
            return List.copyOf(result);
        } catch (IOException | IllegalArgumentException exception) {
            throw new TestDataException("Unable to read CSV data: " + resource, exception);
        }
    }
}
