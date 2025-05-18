package qkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qkart.utility.CommonMethods;
import qkart.utility.Base;

import java.time.Duration;
import java.util.List;

public class SearchResult extends Base {
    WebDriver driver;
    WebDriverWait wait;
    WebElement parentElement;
    CommonMethods cm;

    public SearchResult(WebDriver driver, WebElement SearchResultElement) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        parentElement = SearchResultElement;
        cm = new CommonMethods(this.driver);
    }

    //  Return title of the parentElement denoting the card content section of a search result
    public String getTitleOfResult() {
        try {
            return cm.findElementFromParentByClassName(wait, parentElement, "css-yg30e6").getText();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting title of product from parent result web element");
            log.error("{} Exception while getting title of product from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }

    // Return boolean denoting if the open size chart operation was successful
    public boolean openSizeChart() {
        try {
            // Find the link of size chart in the parentElement and click on it
            boolean status = cm.click(cm.findElementFromParentByXPath(wait, parentElement, ".//button[text()='Size chart']"));
            Thread.sleep(3000);
            return status;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while opening size chart from parent result web element");
            log.error("{} Exception while opening size chart from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    // Return boolean denoting if the close size chart operation was successful
    public boolean closeSizeChart() {
        try {
            Actions action = new Actions(driver);

            // Clicking on "ESC" key closes the size chart modal
            action.sendKeys(Keys.ESCAPE).perform();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("MuiDialog-paperScrollPaper")));
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while closing size chart from parent result web element");
            log.error("{} Exception while closing size chart from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    // Return boolean based on if the size chart exists
    public boolean verifySizeChartExists() {
        try {
            WebElement element = cm.findElementFromParentByTagName(wait, parentElement, "button");
            return element.getText().trim().equalsIgnoreCase("SIZE CHART");
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying size chart exists from parent result web element");
            log.error("{} Exception while verifying size chart exists from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    // Return boolean if the table headers and body of the size chart matches the expected values
    public boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody) {
        try {
            WebElement sizeChartParent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiDialog-paperScrollPaper")));
            WebElement tableElement = cm.findElementFromParentByTagName(wait, sizeChartParent, "table");
            List<WebElement> tableHeader = cm.findElementsFromParentByXPath(wait, tableElement, "//thead//th");

            // Check table headers match
            String tempHeaderValue;
            for (int i = 0; i < expectedTableHeaders.size(); i++) {
                tempHeaderValue = tableHeader.get(i).getText().trim();

                if (!expectedTableHeaders.get(i).equals(tempHeaderValue)) {
                    return false;
                }
            }

            List<WebElement> tableBodyRows = cm.findElementsFromParentByXPath(wait, tableElement, "//tbody//tr");

            // Check table body match
            List<WebElement> tempBodyRow;
            for (int i = 0; i < expectedTableBody.size(); i++) {
                tempBodyRow =  cm.findElementsFromParentByTagName(wait, tableBodyRows.get(i), "td");

                for (int j = 0; j < expectedTableBody.get(i).size(); j++) {
                    tempHeaderValue = tempBodyRow.get(j).getText();

                    if (!expectedTableBody.get(i).get(j).equals(tempHeaderValue.trim())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while validating size chart content from parent result web element");
            log.error("{} Exception while validating size chart content from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    // Return boolean based on if the Size drop down exists
    public boolean verifyExistenceOfSizeDropdown() {
        try {
            return cm.findElementFromParentByClassName(wait, parentElement, "css-13sljp9") != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while verifying size dropdown from parent result web element");
            log.error("{} Exception while verifying size dropdown from parent result web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }
}