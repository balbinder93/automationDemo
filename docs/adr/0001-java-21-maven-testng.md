# ADR 0001: Use Java 21, Maven, and TestNG

* **Status:** Accepted
* **Context:** The existing project is Maven-based and uses TestNG annotations but
  has no declared Java release or direct TestNG dependency.
* **Decision:** Compile with Java 21 using Maven Compiler Plugin and declare TestNG
  directly for test execution.
* **Consequences:** Builds require JDK 21+. Legacy examples are isolated under
  `src/test/java`, allowing TestNG to remain test scoped.
