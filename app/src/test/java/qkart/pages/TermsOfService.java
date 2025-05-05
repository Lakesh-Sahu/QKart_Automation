package qkart.pages;

import qkart.utility.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TermsOfService {
    WebDriver driver;
    WebDriverWait wait;

    // Privacy policy page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/terms-of-service";

    // Constructor of Checkout class
    public TermsOfService() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Navigate to checkout page
    public boolean navigateToPTermsOfServicePage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTermsOfServiceHeadingText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/h2"))).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
