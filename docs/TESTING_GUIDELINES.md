# Testing Guidelines

* Put UI tests in `src/test/java/tests/ui`, API tests in `tests/api`, integration
  tests in `tests/integration`, and contract tests in `tests/contract` as added.
* Tests must not share browser sessions, mutable data, credentials, or execution
  order. Create a `DriverSession` in try-with-resources; its instance-owned
  `DriverManager` is thread-confined and releases the browser deterministically.
* Prefer page/component composition and explicit assertions over test inheritance.
* Use AssertJ for assertions and SLF4J for diagnostic logging. Never log secrets,
  authorization headers, or personally identifiable test data.
* Set URLs and non-secret options with documented `-D` properties such as `-Denv`,
  `-Dbase-url`, and `-Dbrowser`. Inject secrets through CI
  secret storage/environment; never commit them.
* Unit-test framework code without a browser. UI tests should use a controlled
  environment and produce screenshots/logs when reporting is added.
* Tests must not instantiate `ChromeDriver`, `FirefoxDriver`, `EdgeDriver`, or
  `RemoteWebDriver` directly. Use `DriverSession` and `SeleniumWebDriverFactory`.
* Never use `Thread.sleep`. Express the expected browser state through `UiWaits`
  before performing an action or assertion.
* Keep test classes at workflow level. Place selectors and DOM interactions in
  page/component objects and use business assertions rather than exposing
  `WebElement` instances to tests.
* Keep datasets in typed resource fixtures and supply scenarios through TestNG data
  providers. Generate unique users, orders, and other mutable entities per test
  invocation to avoid collisions in parallel execution.
* Add a class to the TestNG suite only when it has deterministic data and no
  dependence on a public mutable service. Keep exploratory legacy examples outside
  the suite until they are migrated or retired.
