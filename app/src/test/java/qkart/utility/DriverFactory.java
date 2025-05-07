package qkart.utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static WebDriver driver;

    private DriverFactory() {
    }

//    For Singleton WebDriver
//    public static WebDriver getDriver() {
//        if (driver == null) {
//            ChromeOptions op = getChromeOptions();
//            driver = new ChromeDriver(op);
//            ((JavascriptExecutor) driver).executeScript(
//                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
//            );
//        }
//        return driver;
//    }

    public static WebDriver getDriver() {
        ChromeOptions op = getChromeOptions();
        driver = new ChromeDriver(op);
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );
        return driver;
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions op = new ChromeOptions();
//        op.addArguments("--headless=new");
//        op.addArguments("--incognito");

        op.addArguments("start-maximized");  // open Chrome browser in maximized mode
        op.addArguments("--disable-extensions"); // disable all the pre-installed or third party installed extensions
        op.addArguments("--disable-popup-blocking");    // disable blocking of popups by Chrome browser mechanism

        op.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // hides the "Chrome is being controlled by automated software" banner
        op.setExperimentalOption("useAutomationExtension", false); // turns off Selenium's automation extension to reduce detection

//         Disable Chrome's password manager
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);   // disables login service or disables Chrome’s credential saving service
        prefs.put("profile.password_manager_enabled", false); // disables password manager UI prompt
        op.setExperimentalOption("prefs", prefs);

//        op.addArguments("user-data-dir=D:/Selenium Projects/Selenium Profile");  // Using a default profile
//        op.addArguments("profile-directory=Profile 8");   // changing the use of default profile into Profile 8
        return op;
    }
}