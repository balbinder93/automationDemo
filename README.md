# Automation Framework

This repository contains the Stage 1 foundation for an application-agnostic Java
21 Maven web automation framework. It provides configuration, browser-session
isolation, logging, exceptions, and a TestNG test foundation only. Application
page objects and e-commerce flows are intentionally not implemented yet.

## Prerequisites

* JDK 21 or later
* Maven 3.9 or later
* A locally installed browser when running future UI tests

## Run the foundation checks

```bash
./scripts/run-tests.sh
```

Configure a future UI run with system properties (or equivalent uppercase
environment variables, such as `BROWSER`):

```bash
mvn test -Denv=qa -Dbrowser=chrome -Dheadless=true -Dexecution=local -Dthreads=4
```

See the [architecture](docs/ARCHITECTURE.md), [testing guidelines](docs/TESTING_GUIDELINES.md),
and Stage 0 [repository assessment](docs/REPOSITORY_ASSESSMENT.md).

## Continuous integration

GitHub Actions runs `mvn --batch-mode clean test` with Temurin 21 on pull requests
and pushes to `main`. A Docker build uses the same Maven/JDK baseline to validate
the framework in a container.

## Browser execution

Chrome, Firefox, and Edge support local, remote, and Selenium Grid execution. Use
`-Dexecution=remote -Dremote-url=http://grid:4444` (or `execution=grid`) for a
remote session. The `ui-smoke` TestNG check is opt-in locally:

```bash
mvn test -Dframework.ui-smoke.enabled=true -Dheadless=true
```

The framework does not configure an implicit wait by default. It uses the configured
explicit-wait timeout for future wait helpers; this avoids compounded and opaque
wait behavior.

## UI architecture example

The e-commerce demonstration is isolated under `src/test/java/examples/ecommerce`.
It composes `HomePage` from header, search, and footer components; search results
compose product cards; and product/cart pages expose business operations rather
than `WebElement` values. Run the example only against an application implementing
the documented locator contract:

```bash
mvn test -Dframework.ecommerce.enabled=true -Dbase-url=https://qa.example.test
```

See [locator guidelines](docs/LOCATOR_GUIDELINES.md) for the attribute contract and
selector-review rules.

## Test data

JSON, header-based CSV, and YAML fixtures are read into Java records. The
data-driven e-commerce example reads product records through a TestNG data provider,
while unique users and order references are generated per invocation for parallel
isolation. See [test-data architecture](docs/TEST_DATA_ARCHITECTURE.md) and copy
`.env.example` only as a local variable-name template—never commit credentials.
