# Repository Assessment — Stage 0

**Assessment date:** 2026-09-08  
**Scope:** Repository contents present on the `work` branch. This is an assessment
only; no framework implementation or existing source was changed.

## 1. Current architecture

The repository is a single Maven JAR project (`code.api.automation:automationDemo`)
with all handwritten Java under `src/main/java`. It has three top-level package
areas:

* `automationDemo` contains executable tutorial-style API examples and one
  TestNG-annotated class.
* `files` contains inline JSON payload factories and one JSON parsing helper.
* `pojo` contains basic course-response model classes.

There is no `src/test/java`, no `src/test/resources`, no test suite XML in source
control, and no web page-object, driver lifecycle, or framework-layer package.
`target/` and `test-output/` are checked in and contain a previously built JAR,
compiled classes, and historical TestNG HTML/XML reports.

## 2. Current technology stack

| Area | Verified finding |
| --- | --- |
| Build | Maven project, packaging defaults to `jar`; no `<build>` section, Maven wrapper, or Maven configuration is present. |
| Java | Source/target release is **UNKNOWN** because the POM does not configure it. The current environment uses OpenJDK 25.0.2. The checked-in JAR manifest reports it was built with JDK 11.0.10. |
| Web UI | Selenium Java 3.141.59 is a direct dependency. Only `Oauth.java` instantiates `ChromeDriver`. |
| API | REST Assured JSON Path, JSON Schema Validator, XML Path, Spring Mock MVC, and Scala support are declared at 4.4.0. |
| Serialization | Jackson Databind 2.12.1 and Gson 2.8.5 are declared. |
| Test | TestNG is used in source and historical reports, but is not declared directly in the POM. |
| Reporting | TestNG’s generated HTML/XML report artifacts are checked in; no report-plugin configuration exists. |
| CI/CD, Docker, scripts, documentation | No CI workflow, Dockerfile/Compose file, shell script, README, or existing documentation was found. |

## 3. Existing automation capabilities

* A REST Assured example adds, updates, and retrieves a place against an external
  `rahulshettyacademy.com` endpoint and asserts the updated address.
* A TestNG data-provider method posts and deletes three books against an external
  HTTP endpoint. The historical `test-output/testng-results.xml` records three
  passing invocations on 2021-05-25; this does **not** prove they remain runnable.
* A JSON parsing example calculates course-purchase totals from an inline payload.
* POJO classes model API, mobile, and web-automation course entries, although the
  top-level `GetCourse` POJO is empty and its intended use is incomplete.
* A Selenium/OAuth proof of concept opens Chrome but has an empty navigation URL
  and requires a hard-coded Windows ChromeDriver path.

No verified application-agnostic web automation capability exists: there are no
page objects/components, explicit waits, browser options, cross-browser support,
parallel execution controls, configuration abstraction, or test-data abstraction.

## 4. Existing problems

* The project could not execute `mvn test` in this environment because Maven was
  unable to download `maven-resources-plugin:3.3.1` from Central (HTTP 403).
  Consequently, current compilation and test execution are **UNKNOWN**.
* Test code is located in `src/main/java`; Maven’s default test discovery does not
  treat it as normal test source. No Surefire/TestNG suite configuration is present.
* The existing tests call mutable third-party systems directly, use live
  credentials/query keys in source, and offer no environment selection or cleanup
  resilience.
* `Oauth.java` includes a client secret, hard-coded local executable path, an empty
  URL, and a misspelled `clent_secret` request parameter. It also does not close
  the browser.
* A direct TestNG dependency is absent despite TestNG imports and annotations.
* No compiler release, encoding, plugin versions, or dependency-management policy
  is declared; reproducibility is therefore weak.
* Generated build and report outputs are version controlled and have no `.gitignore`.

## 5. Technical debt

* Package names and class names do not follow conventional Java naming consistently
  (`automationDemo`, `files`, `ReuseableMethods`).
* Examples mix orchestration, endpoint URLs, credentials/keys, assertions, payload
  strings, logging, and console output in one class.
* Inline string-concatenated JSON is fragile and makes test data hard to reuse.
* `Oauth.java` has unused imports and commented-out implementation; `GetCourse`
  has no fields/accessors for the response it is intended to deserialize.
* A TestNG report and compiled output from 2021/older toolchains are retained as
  source artifacts, creating stale evidence of test health.

## 6. Missing capabilities

The repository has no verified support for:

* configuration profiles, secret injection, environment URLs, or typed settings;
* a WebDriver factory, browser lifecycle, waits, screenshots, downloads, or grid;
* Page Object/Screenplay abstractions and application-specific test modules;
* test tags, suites, retries, parallelization, deterministic test data, or API
  client abstraction;
* structured logging, Allure/Extent-style reporting, failure attachments, or
  result publication;
* linting, static analysis, coverage, dependency/security scanning, or quality gates;
* CI/CD, Docker execution, or contributor setup documentation.

## 7. Risks

| Risk | Evidence and impact |
| --- | --- |
| Secret exposure | OAuth client secret and API key are embedded in Java. Rotate/revoke externally before publishing or reuse. |
| Unreliable execution | Tests depend on public live services; one endpoint is HTTP, and no environment or availability handling exists. |
| Build non-reproducibility | Plugin versions and Java release are unspecified; current Maven execution is blocked by remote plugin resolution. |
| Unsupported UI stack | Selenium 3.141.59 relies on manual driver-path management and predates Selenium Manager. |
| Misleading test status | Checked-in reports show historic passes, not current CI results. |
| Migration breakage | Moving source from `main` to `test` and updating Selenium/TestNG can expose currently masked compile/runtime defects. |

## 8. Recommended target architecture

Adopt a single Maven automation platform with explicit, separated layers:

```text
src/
  main/java/.../core/        # configuration, WebDriver/API factories, waits, logging
  main/java/.../support/     # reusable non-domain utilities only
  test/java/.../tests/       # test orchestration, grouped by application/module
  test/java/.../pages/       # application-specific page objects/components
  test/java/.../api/         # application-specific API clients/assertions
  test/resources/
    config/                  # non-secret defaults and environment templates
    testdata/                # versioned deterministic fixtures
    suites/                  # TestNG suite definitions, if TestNG is retained
```

Keep the core framework application-agnostic. Put e-commerce pages, product/cart
flows, and e-commerce fixtures in a separately named test module/package. Use an
explicit Java LTS release, Maven Compiler/Surefire plugin versions, direct TestNG
declaration, Selenium 4, WebDriver lifecycle management, explicit waits, typed
configuration with environment-variable secret overrides, and CI-produced
artifacts. The exact library versions and reporting product are **UNKNOWN** until
organization compatibility, supported browser matrix, and CI constraints are set.

## 9. Proposed migration strategy

1. Establish a clean baseline: add `.gitignore`, remove generated outputs from
   version control only after confirming they are reproducible, and retain an
   archive/tag if historical reports are needed.
2. Secure the project: revoke/rotate exposed credentials, replace them with
   environment variables or a secret manager, and add example configuration with
   no secrets.
3. Make the build deterministic: choose and declare a supported Java LTS release;
   pin compiler, Surefire, and reporting plugins; declare TestNG directly; then
   restore a successful local/CI dependency-resolution path.
4. Move executable checks into `src/test/java` and fixtures/configuration into
   `src/test/resources`; introduce named suites/tags before changing test behavior.
5. Extract API helpers and payload models from tutorial classes, preserving existing
   assertions as characterization tests where external services permit.
6. Introduce the application-agnostic UI core (driver factory, configuration,
   waits, listeners, screenshots) and then add e-commerce page/component tests as
   a consumer of that core.
7. Add CI, reporting, screenshots/log collection, dependency scanning, and a
   documented execution matrix. Run the migrated suite against controlled test
   environments rather than public mutable endpoints.

## 10. Files that should be preserved

* `pom.xml` — preserve as the record of the current declared dependency set; evolve
  it incrementally rather than replacing it wholesale.
* `src/main/java/files/Payload.java` — preserve its example payload intent while
  migrating payloads to typed models or resource fixtures.
* `src/main/java/automationDemo/Basics.java`, `ComplexJsonParse.java`, and
  `DynamicJson.java` — preserve their API workflow/assertion intent as
  characterization references.
* `src/main/java/pojo/API.java`, `Courses.java`, `Mobile.java`, and
  `WebAutomation.java` — preserve as candidate response-model starting points.

## 11. Files that should be refactored

* `pom.xml` — add explicit Java/build/test-plugin configuration; rationalize and
  manage dependencies.
* `Basics.java`, `ComplexJsonParse.java`, and `DynamicJson.java` — relocate tests
  to test source and separate URLs, data, payloads, and assertions.
* `Payload.java` and `ReuseableMethods.java` — rename/restructure and replace raw
  string assembly with maintainable fixture/model handling.
* `Oauth.java` and `pojo/GetCourse.java` — refactor only if the OAuth course API
  scenario remains in scope; otherwise retire them from runnable automation after
  credential remediation.

## 12. Files that should potentially be removed

* Version-controlled `target/**` — generated Maven output; remove from source
  control after verifying a reproducible build.
* Version-controlled `test-output/**` — generated TestNG output; publish from CI
  as artifacts instead of committing it.
* `Oauth.java` — candidate for removal if OAuth browser experimentation is outside
  the framework scope. Its business need is **UNKNOWN**.

## 13. Dependencies that should be upgraded

Upgrade planning should occur after compatibility validation, but these declared
versions are materially old and should not be the target baseline:

* `org.seleniumhq.selenium:selenium-java` 3.141.59 → Selenium 4.x.
* `com.fasterxml.jackson.core:jackson-databind` 2.12.1 → a current supported
  2.x line selected with the project’s Java baseline.
* `com.google.code.gson:gson` 2.8.5 → a current supported 2.x line if retained.
* REST Assured modules 4.4.0 → a current supported line, upgraded as a compatible
  set.
* `com.beust:jcommander` 1.78 and `org.webjars:jquery` 3.5.1 → current supported
  versions only if they remain needed.

## 14. Dependencies that should be removed

No dependency should be removed without compilation and usage verification. The
following are currently unused by repository Java source and are candidates for
removal after validating transitive/runtime needs:

* `io.rest-assured:spring-mock-mvc` and `io.rest-assured:scala-support`;
* `io.rest-assured:json-schema-validator` and `io.rest-assured:xml-path`;
* `com.beust:jcommander`, `org.webjars:jquery`, and `com.google.code.gson:gson`;
* direct `jackson-databind`, if REST Assured’s managed transitive dependency is
  sufficient for the retained implementation.

Add `org.testng:testng` as an explicit test-scoped dependency rather than relying
on transitive availability.

## 15. Recommended project structure

```text
.
├── docs/
│   └── REPOSITORY_ASSESSMENT.md
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   ├── main/java/com/<organization>/automation/core/
│   ├── main/java/com/<organization>/automation/support/
│   ├── test/java/com/<organization>/automation/framework/
│   ├── test/java/com/<organization>/automation/ecommerce/
│   │   ├── pages/
│   │   ├── components/
│   │   ├── api/
│   │   └── tests/
│   └── test/resources/
│       ├── config/
│       ├── suites/
│       └── testdata/
└── .github/workflows/                 # or the organization’s chosen CI system
```

`<organization>` is intentionally a placeholder because the appropriate namespace
is **UNKNOWN** from this repository.

## Concise finding summary

This is an early API-automation learning project, not yet a web automation
framework. It contains REST Assured examples, one historically executed TestNG
data-provider test, and a non-production Selenium/OAuth proof of concept. The
highest-priority foundations are secret remediation, build reproducibility,
source/test separation, dependency modernization, and removal of generated
artifacts from version control before framework implementation begins.
