package QKART_TESTNG.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {
    WebDriver driver;

    // Login page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/login";

    // Constructor of Login class
    public Login(WebDriver driver) {
        this.driver = driver;
    }

    // Navigating to the login page
    public void navigateToLoginPage() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogin(String Username, String Password) throws InterruptedException {
        // Find the Username Text Box
        WebElement username_txt_box = this.driver.findElement(By.id("username"));

        // Enter the username
        username_txt_box.sendKeys(Username);

        // Wait for username to be entered
        Thread.sleep(1000);

        // Find the password Text Box
        WebElement password_txt_box = this.driver.findElement(By.id("password"));

        // Enter the password
        password_txt_box.sendKeys(Password);

        // Find the Login Button
        WebElement loginBtn = driver.findElement(By.xpath("//button[contains(text(), 'Login')]"));

        // Click the login Button
        loginBtn.click();

        // Wait for login button to invisible
        // FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30))
        //         .pollingEvery(Duration.ofMillis(600)).ignoring(NoSuchElementException.class);

        // wait.until(ExpectedConditions.invisibilityOf(loginBtn));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Logout']")));

        // Return the result of login successful or not
        return this.VerifyUserLoggedIn(Username);
    }

    // Check weather login is successful or not
    public Boolean VerifyUserLoggedIn(String Username) {
        try {
            // Find the username label (present on the top right of the page)
            WebElement username_label = this.driver.findElement(By.className("username-text"));

            // Return true if Username and username_label matches
            return username_label.getText().equals(Username);
        } catch (Exception e) {
            return false;
        }

    }
}