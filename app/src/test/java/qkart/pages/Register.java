package qkart.pages;

import java.sql.Timestamp;
import java.time.Duration;

import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Register extends CommonMethods {
    WebDriver driver;
    WebDriverWait wait;

    // Registration page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/register";

    // Creating String variable for storing last generated username
    public String lastGeneratedUsername = "";

    // Constructor of Register class
    public Register() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Opening the registration page
    public boolean navigateToRegisterPage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyOnRegisterPage() {
        try {
            return driver.getCurrentUrl().equals(url);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean registerUser(String usernameToEnter, String passwordToEnter, boolean makeUsernameDynamic) {
        try {
            // Locating the Username Text Box
            WebElement username_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            // Find the password Text Box
            WebElement password_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            // Find the confirm password Text Box
            WebElement confirm_password_txt_box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));
            // Find the register now button
            WebElement register_now_Btn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("button")));

            if (makeUsernameDynamic) {
                // Getting time stamp for generating a unique username
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                // Concatenate the timestamp to string to form unique timestamp
                lastGeneratedUsername = usernameToEnter + "_" + timestamp.getTime();
            } else {
                lastGeneratedUsername = usernameToEnter;
            }

            // Type the last generated username in the username field; Enter the Password value; Enter the Confirm Password Value; Click the register now button
            boolean status = sendKeys(username_txt_box, lastGeneratedUsername) && sendKeys(password_txt_box, passwordToEnter) && sendKeys(confirm_password_txt_box, passwordToEnter) && click(register_now_Btn);

            return status && wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));
        } catch (Exception e) {
            return false;
        }
    }
}