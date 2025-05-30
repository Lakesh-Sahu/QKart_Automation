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

public class Register extends Base {
    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Registration page url
    String url = "https://crio-qkart-qa.vercel.app/register";

    public Register(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    // Opening the registration page
    public boolean navigateToRegisterPage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while navigating to Register Page");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while navigating to Register Page", e, Level.WARN);
            return false;
        }
    }

    public boolean verifyOnRegisterPage() {
        try {
            return driver.getCurrentUrl().equals(url);
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying on Register Page");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while verifying on Register Page", e, Level.WARN);
            return false;
        }
    }

    public boolean registerUser(String usernameToEnter, String passwordToEnter, String confirmPassword) {
        try {
            // Locating the Username Text Box
            WebElement username_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            // Find the password Text Box
            WebElement password_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            // Find the confirm password Text Box
            WebElement confirm_password_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));
            // Find the register now button
            WebElement register_now_Btn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("button")));

            // Type the last generated username in the username field; Enter the Password value; Enter the Confirm Password Value; Click the register now button
            boolean status = cm.sendKeys(username_txt_box, usernameToEnter) && cm.sendKeys(password_txt_box, passwordToEnter) && cm.sendKeys(confirm_password_txt_box, confirmPassword) && cm.click(register_now_Btn);
            return status && wait.until(ExpectedConditions.urlToBe("https://crio-qkart-qa.vercel.app/login"));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while registering user with username " + usernameToEnter + " and password " + passwordToEnter + " and confirm password " + confirmPassword);
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while registering user with username " + usernameToEnter + " and password " + passwordToEnter + " and confirm password " + confirmPassword, e, Level.WARN);
            return false;
        }
    }
}