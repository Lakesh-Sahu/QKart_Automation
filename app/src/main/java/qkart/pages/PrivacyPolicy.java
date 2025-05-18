package qkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qkart.utility.CommonMethods;
import qkart.utility.Base;

import java.time.Duration;

public class PrivacyPolicy extends Base {
    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Privacy policy page url
    private final String url = "https://crio-qkart-qa.vercel.app/privacy-policy";

    public PrivacyPolicy(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    // Navigate to checkout page
    public boolean navigateToPrivacyPolicyPage() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while navigating to Privacy Policy");
            log.error("{} Exception while navigating to Privacy Policy : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public String getPrivacyPolicyHeadingText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/h2[text()='Privacy Policy']"))).getText();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting Privacy Policy Heading Text");
            log.error("{} Exception while getting Privacy Policy Heading Text : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }
}
