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

public class AboutUs extends Base {

    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Thanks page url
    String url = "https://crio-qkart-qa.vercel.app/aboutus";

    public AboutUs(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    public boolean verifyOnAboutUsPage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying on About Us Page");
            log.error("{} Exception while verifying on About Us Page : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public String getAboutUsHeadingText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/h2"))).getText();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting About Us Heading Text");
            log.error("{} Exception while getting About Us Heading Text : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }
}