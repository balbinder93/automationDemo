# Locator Guidelines

## Preferred order

1. `data-testid` or `data-test` attributes owned by the application test contract.
2. A stable, unique `id`.
3. A semantic or accessibility attribute such as a stable ARIA role/name.
4. A concise CSS selector.
5. XPath only when the preceding options cannot express the relationship.

Never use an absolute XPath, positional locator tied to page layout, generated CSS
class, or text that changes with localization unless the assertion is explicitly
about that text.

## E-commerce example contract

The example pages under `src/test/java/examples/ecommerce` deliberately use
`data-testid` selectors such as `home-page`, `search-input`, `product-card`,
`product-name`, and `cart-item`. These represent the collaboration contract to
agree with an application team; they are not framework-core locators.

## Review checklist

* Is the locator stable across visual redesigns?
* Is it scoped to a component where duplicate elements are possible?
* Does it avoid absolute XPath and generated class names?
* Can the application expose a `data-testid` instead of making the test infer DOM
  structure?
