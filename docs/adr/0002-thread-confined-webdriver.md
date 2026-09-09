# ADR 0002: Use thread-confined WebDriver sessions

* **Status:** Accepted
* **Context:** Parallel execution requires browser isolation without global mutable
  driver state.
* **Decision:** Store one WebDriver in `ThreadLocal` via `DriverManager`; the TestNG
  listener clears it after pass, fail, and skip outcomes.
* **Consequences:** Tests must create sessions through the composition boundary and
  must not cache drivers across threads.
