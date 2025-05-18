package qkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qkart.utility.CommonMethods;
import qkart.utility.Base;

import java.time.Duration;

public class TermsOfService extends Base {
    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Privacy policy page url
    String url = "https://crio-qkart-qa.vercel.app/terms-of-service";

    public TermsOfService(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    // Navigate to checkout page
    public boolean navigateToPTermsOfServicePage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while navigating to Terms of Service Page");
            log.error("{} Exception while navigating to Terms of Service Page : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public String getTermsOfServiceHeadingText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/h2"))).getText();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting Terms of Service Heading Text");
            log.error("{} Exception while getting Terms of Service Heading Text : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }
}