package qkart.utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory extends Base {

    public DriverFactory() {
    }

    public WebDriver getDriver(String browser) {
        WebDriver driver = switch (browser) {
            case "Chrome" -> new ChromeDriver(getChromeOptions());
            case "Edge" -> new EdgeDriver(getEdgeOptions());
            case "Firefox" -> new FirefoxDriver(getFireFoxOptions());
            case "Safari" -> new SafariDriver(getSafariOptions());
            default -> null;
        };

        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
            );
        }
        return driver;
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
//        op.addArguments("--headless=new");
//        op.addArguments("--incognito");

        options.addArguments("start-maximized");  // open Chrome browser in maximized mode
        options.addArguments("--disable-extensions"); // disable all the pre-installed or third party installed extensions
        options.addArguments("--disable-popup-blocking");    // disable blocking of popups by Chrome browser mechanism

        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // hides the "Chrome is being controlled by automated software" banner
        options.setExperimentalOption("useAutomationExtension", false); // turns off Selenium's automation extension to reduce detection

//         Disable Chrome's password manager
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);   // disables login service or disables Chrome’s credential saving service
        prefs.put("profile.password_manager_enabled", false); // disables password manager UI prompt
        options.setExperimentalOption("prefs", prefs);

//        op.addArguments("user-data-dir=D:/Selenium Projects/Selenium Profile");  // Using a default profile
//        op.addArguments("profile-directory=Profile 8");   // changing the use of default profile into Profile 8
        return options;
    }

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    private static FirefoxOptions getFireFoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized"); // may not work on all OS
        options.addPreference("signon.rememberSignons", false); // disables password saving
        options.addPreference("credentials_enable_service", false); // disable login prompt
        options.addPreference("dom.disable_open_during_load", false); // allow popups

        // options.setHeadless(true);
        return options;
    }

    private static SafariOptions getSafariOptions() {
        SafariOptions options = new SafariOptions();

        // Set automatic permission to use media streams, geolocation, etc.
        options.setCapability("safari.options", Map.of("automaticInspection", false, "automaticProfiling", false
        ));
        return options;
    }
}