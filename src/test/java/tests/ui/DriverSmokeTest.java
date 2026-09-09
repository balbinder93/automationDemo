package tests.ui;

import framework.config.FrameworkConfiguration;
import framework.driver.DriverManager;
import framework.driver.DriverSession;
import framework.driver.SeleniumWebDriverFactory;
import org.testng.SkipException;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/** Opt-in smoke check for a real local browser; CI enables it on a browser-capable runner. */
public class DriverSmokeTest {
    @Test(groups = "ui-smoke")
    void startsAndUsesAConfiguredBrowserSession() {
        if (!Boolean.getBoolean("framework.ui-smoke.enabled")) throw new SkipException("Set -Dframework.ui-smoke.enabled=true to run browser smoke tests");
        try (DriverSession session = new DriverSession(new DriverManager(), new SeleniumWebDriverFactory())) {
            session.start(FrameworkConfiguration.system().load());
            session.driver().get("data:text/html,<title>Driver smoke</title><h1>ready</h1>");
            assertThat(session.driver().getTitle()).isEqualTo("Driver smoke");
        }
    }
}
