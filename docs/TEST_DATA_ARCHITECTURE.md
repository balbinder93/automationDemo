# Test Data Architecture

## Data formats and typed boundaries

`JsonDataReader` reads typed JSON records for structured fixtures. `CsvDataReader`
uses header names and a caller-supplied record mapper, so CSV never leaks as a map.
`YamlDataReader` is included because concise, human-authored checkout/configuration
fixtures are easier to review in YAML; it maps directly to typed records.

The demonstration models are `ProductData` and `CheckoutData`. Test logic receives
these records through TestNG data providers rather than embedding datasets in test
methods. Fixtures live in `src/test/resources/testdata`.

## Configuration and sensitive values

Runtime values resolve from system properties before environment variables. Use
`RuntimeValueResolver.required("test.username", "TEST_USERNAME")` for credentials
or tokens supplied by a CI secret store. `.env.example` documents names only;
`.env` is ignored and real credentials must never be committed.

## Parallel isolation

Each data-provider invocation receives an immutable record. `UniqueTestDataFactory`
uses a UUID per invocation for users and order references, preventing collisions
between parallel workers. Prefer generated users/orders and controlled cleanup over
shared records. Do not mutate resource fixtures during a test.

## Adding a fixture

1. Define a Java record in `framework.models`.
2. Add the smallest representative fixture under `src/test/resources/testdata`.
3. Read it through the appropriate typed reader.
4. Add a reader/model unit test and use a TestNG data provider for scenario variants.
