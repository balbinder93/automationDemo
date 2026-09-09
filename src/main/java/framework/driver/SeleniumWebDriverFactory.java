package framework.driver;

import framework.config.BrowserOptions;
import framework.config.DriverConfig;
import framework.exceptions.DriverLifecycleException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Creates configured local or remote Selenium sessions without global driver state. */
public final class SeleniumWebDriverFactory implements WebDriverFactory {
    @Override
    public WebDriver create(DriverConfig config) {
        try {
            WebDriver driver = switch (config.executionMode()) {
                case LOCAL -> local(config);
                case REMOTE, GRID -> new RemoteWebDriver(remoteUrl(config), capabilities(config));
            };
            configure(driver, config);
            return driver;
        } catch (RuntimeException exception) {
            throw new DriverLifecycleException("Unable to create " + config.browser() + " " + config.executionMode() + " browser session", exception);
        }
    }
    private URL remoteUrl(DriverConfig config) {
        try { return config.remoteUrl().orElseThrow().toURL(); }
        catch (MalformedURLException exception) { throw new DriverLifecycleException("Invalid remote URL", exception); }
    }
    private WebDriver local(DriverConfig config) {
        return switch (config.browser()) {
            case CHROME -> new ChromeDriver(chromeOptions(config.browserOptions()));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(config.browserOptions()));
            case EDGE -> new EdgeDriver(edgeOptions(config.browserOptions()));
        };
    }
    private org.openqa.selenium.Capabilities capabilities(DriverConfig config) {
        return switch (config.browser()) {
            case CHROME -> chromeOptions(config.browserOptions());
            case FIREFOX -> firefoxOptions(config.browserOptions());
            case EDGE -> edgeOptions(config.browserOptions());
        };
    }
    private void configure(WebDriver driver, DriverConfig config) {
        driver.manage().timeouts().pageLoadTimeout(config.pageLoadTimeout());
        driver.manage().timeouts().implicitlyWait(config.implicitWait());
        driver.manage().timeouts().scriptTimeout(config.scriptTimeout());
        if (config.browserOptions().hasWindowSize()) driver.manage().window().setSize(new Dimension(config.browserOptions().windowWidth(), config.browserOptions().windowHeight()));
        else if (config.browserOptions().maximizeWindow()) driver.manage().window().maximize();
    }
    private ChromeOptions chromeOptions(BrowserOptions options) {
        ChromeOptions chrome = new ChromeOptions();
        if (options.headless()) chrome.addArguments("--headless=new");
        chrome.addArguments(options.arguments());
        chrome.setExperimentalOption("prefs", chromiumDownloadPreferences(options));
        return chrome;
    }
    private EdgeOptions edgeOptions(BrowserOptions options) {
        EdgeOptions edge = new EdgeOptions();
        if (options.headless()) edge.addArguments("--headless=new");
        edge.addArguments(options.arguments());
        edge.setExperimentalOption("prefs", chromiumDownloadPreferences(options));
        return edge;
    }
    private FirefoxOptions firefoxOptions(BrowserOptions options) {
        FirefoxOptions firefox = new FirefoxOptions();
        if (options.headless()) firefox.addArguments("-headless");
        firefox.addArguments(options.arguments());
        firefox.addPreference("browser.download.dir", options.downloadDirectory().toAbsolutePath().toString());
        firefox.addPreference("browser.download.folderList", 2);
        firefox.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream,text/csv,application/pdf");
        return firefox;
    }
    private Map<String, Object> chromiumDownloadPreferences(BrowserOptions options) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("download.default_directory", options.downloadDirectory().toAbsolutePath().toString());
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.directory_upgrade", true);
        return preferences;
    }
}
