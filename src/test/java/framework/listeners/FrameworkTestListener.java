package framework.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Logs TestNG outcomes; browser sessions are owned and closed by DriverSession. */
public final class FrameworkTestListener implements ITestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkTestListener.class);
    @Override public void onTestStart(ITestResult result) { LOGGER.info("Starting test {}", result.getMethod().getQualifiedName()); }
    @Override public void onTestSuccess(ITestResult result) { LOGGER.info("Passed test {}", result.getMethod().getQualifiedName()); }
    @Override public void onTestFailure(ITestResult result) { LOGGER.error("Failed test {}", result.getMethod().getQualifiedName(), result.getThrowable()); }
    @Override public void onTestSkipped(ITestResult result) { LOGGER.warn("Skipped test {}", result.getMethod().getQualifiedName()); }
}
