package qkart.pages;

import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Thanks extends CommonMethods {

    WebDriver driver;
    WebDriverWait wait;

    // Thanks page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/thanks";

    // Constructor of Thanks class
    public Thanks() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean verifyOnThanksPage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getAdvertisementIFrame() {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//iframe")));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public boolean clickBuyNowBtn() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Buy Now']"))));
        } catch (Exception e) {
            return false;
        }
    }
}