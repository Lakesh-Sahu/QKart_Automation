package qkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qkart.utility.CommonMethods;
import qkart.utility.Base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Thanks extends Base {

    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Thanks page url
    String url = "https://crio-qkart-qa.vercel.app/thanks";

    public Thanks(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    public boolean verifyOnThanksPage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying on Thanks Page");
            log.error("{} Exception while verifying on Thanks Page : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public List<WebElement> getAdvertisementIFrame() {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//iframe")));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting advertisement iframe");
            log.error("{} Exception while getting advertisement iframe : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return new ArrayList<>();
        }
    }

    public boolean clickBuyNowBtn() {
        try {
            return cm.click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Buy Now']"))));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while clicking Buy Now button");
            log.error("{} Exception while clicking Buy Now button : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }
}