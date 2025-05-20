package qkart.pages;

import org.apache.logging.log4j.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qkart.utility.CommonMethods;
import qkart.utility.Base;

import java.time.Duration;

public class Login extends Base {
    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Login page url
    private final String url = "https://crio-qkart-qa.vercel.app/login";

    public Login(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    // Navigating to the login page
    public boolean navigateToLoginPage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while navigating to Login Page");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while navigating to Login Page", e, Level.WARN);
            return false;
        }
    }

    public boolean verifyOnLoginPage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying on Login Page");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while verifying on Login Page", e, Level.WARN);
            return false;
        }
    }

    public boolean performLogin(String username, String password) {
        try {
            // Locating the Username Text Box
            WebElement username_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            // Find the password Text Box
            WebElement password_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            // Find the Login Button
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Login')]")));

            // Enter the username; Enter the password; Click the login Button
            boolean status = cm.sendKeys(username_txt_box, username) && cm.sendKeys(password_txt_box, password) && cm.click(loginBtn);

            // Return the result of login successful or not
            return status && verifyUserLoggedIn(username);
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while performing Login with username " + username + " and password " + password);
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while performing Login with username " + username + " and password " + password, e, Level.WARN);
            return false;
        }
    }

    // Check weather login is successful or not
    public boolean verifyUserLoggedIn(String username) {
        try {
            // Find the username label (present on the top right of the page)
            WebElement username_label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("username-text")));
            WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Logout']")));

            // Return true if Username and username_label matches and logout button is displayed
            return cm.compareString(username_label.getText(), username) && logoutBtn != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying user is Logged in for username " + username);
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while verifying user is Logged in for username " + username, e, Level.WARN);
            return false;
        }
    }
}