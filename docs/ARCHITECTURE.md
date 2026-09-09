# Architecture

## Foundation boundaries

The `framework` package is application-agnostic. `config` creates immutable run
settings, `driver` owns thread-confined Selenium sessions, and `exceptions`
provides actionable framework failures. The TestNG listener is test infrastructure.
Future tests and page/component modules depend on these abstractions; the core has
no e-commerce locators, URLs, or business workflows.

`DriverManager` uses an instance-owned `ThreadLocal` so every parallel TestNG worker
can own exactly one browser without exposing one test's WebDriver to another. This
is required because WebDriver is not thread-safe. It is not static global state:
`DriverSession` composes a manager with a factory and closes the session using
try-with-resources. `SeleniumWebDriverFactory` is a composable factory, not a base
class, and tests never construct browser drivers directly.

## Configuration and driver execution

`FrameworkConfiguration` resolves settings in this strict order: **system
properties**, **environment variables**, the selected **environment profile**, then
`config/framework.properties` safe defaults. `env` selects one of `local`, `dev`,
`qa`, or `staging`; profile resources provide only non-secret defaults. Example:

```text
-Denv=qa -Dbrowser=chrome -Dheadless=true -Dexecution=local -Dthreads=4
```

`DriverConfig` validates browser, execution mode, URL, timeouts, windows, browser
arguments, download directory, and remote URL requirements before driver creation.
Chrome, Firefox, and Edge run locally through Selenium Manager; `remote` and `grid`
use `RemoteWebDriver`. Page-load and script timeouts are configured for every
session. Implicit wait defaults to zero, because mixing it with explicit waits
creates unpredictable compounded delays. `ExplicitWaitFactory` centralizes the
configured explicit timeout.

## UI automation core

`UiWaits` is the sole condition-based synchronization layer: it waits for visible
or clickable elements, text, URL changes, document readiness, and disappearance.
It never uses fixed sleeps. `UiActions` composes those waits for click, typing,
hover, scroll, select, upload, window/frame switching, and alerts, logging each
operation and attaching the current URL to failures.

The generic `BasePage` deliberately contains only driver/action/wait composition.
Application pages and component objects live outside `framework` under
`src/test/java/examples/ecommerce`; this keeps Home, Search Results, Product, and
Cart workflows application-specific. Page methods return domain pages or values,
and `EcommerceAssertions` keeps AssertJ business assertions and DOM details out of
test classes. See [locator guidelines](LOCATOR_GUIDELINES.md).

## Test data

The framework data layer uses typed JSON, CSV, and YAML readers rather than maps.
Application data records, generated collision-resistant values, and runtime secret
lookups are separated from page/test logic. Details and the parallel-isolation
strategy are documented in [test-data architecture](TEST_DATA_ARCHITECTURE.md).

## Dependency decisions

The POM targets Java 21 and pins Selenium 4.38.0, TestNG 7.11.0, AssertJ 3.27.6,
SLF4J 2.0.17, and Logback 1.5.18 as Java-21-capable releases. Maven Central was
inaccessible from this environment (HTTP 403), so artifact availability and
transitive dependency compatibility could not be verified here and must be
revalidated in CI before production adoption.

Existing REST Assured examples remain temporarily through REST Assured 5.5.6; they
are now isolated under `src/test/java` and are not part of the new framework core.
The insecure legacy OAuth proof of concept was removed rather than preserving its
embedded client secret and local-driver configuration.

## Planned packages

Package markers establish `pages`, `components`, `waits`, `actions`, `assertions`,
`api`, `network`, `data`, `models`, `reporting`, and `utilities` as deliberate
extension boundaries. Concrete implementations are added only when a real consumer
requires them.

## Execution boundary

`src/test/resources/testng.xml` is the explicit foundation suite. It runs only
framework unit tests plus an opt-in, data-URI driver smoke test; it does not invoke
the legacy live-service examples. CI enables the smoke test on its browser-capable
Java 21 runner. Docker performs the non-browser foundation build. Browser-backed
application suites will be added only after a controlled target environment and
test data contract are available.
