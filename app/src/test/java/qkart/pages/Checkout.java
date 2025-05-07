package qkart.pages;

import java.util.List;
import qkart.utility.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Checkout {
    WebDriver driver;
    WebDriverWait wait;
    CommonMethods cm;

    // Checkout page url
    private final String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        cm = new CommonMethods(this.driver);
    }

    // Navigate to checkout page
    public boolean navigateToCheckout() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyOnCheckoutPage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean denoting the status of Adding new address in checkout page
    public boolean addNewAddress(String addresString) {
        try {
            // Click on the "Add new address" button, enter the address String in the
            // address text box and click on the "ADD" button to save the address
            WebElement addNewAddressBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-btn")));
            cm.click(addNewAddressBtn);

            WebElement addressBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiOutlinedInput-input")));
            cm.sendKeys(addressBox, addresString);
            WebElement addBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'shipping-container')]//button[text()='Add']")));
            cm.click(addBtn);

            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//*[@class='MuiTypography-root MuiTypography-body1 css-yg30e6' and text()='%s']", addresString)))) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean denoting the status of Selecting an available address
    public boolean selectAddress(String addressToSelect) {
        try {
            // Get the parent section of addresses
            WebElement parentBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Shipping']/following-sibling::div[contains(@class,'css-0')]")));
            // Get all the address text WebElement
            List<WebElement> allBoxes = cm.findElementsFromParentByClassName(wait, parentBox, "address-item");

            // Select the matching address
            for (WebElement box : allBoxes) {
                if (cm.findElementFromParentByClassName(wait, box, "css-yg30e6").getText().replaceAll(" ", "").equals(addressToSelect.replaceAll(" ", ""))) {
                    return cm.click(cm.findElementFromParentByTagName(wait, box, "input"));
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean denoting the status of place order action
    public boolean clickPlaceOrderBtn() {
        try {
            // Find the "PLACE ORDER" button and click on it
            return cm.click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='PLACE ORDER']"))));
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean denoting the insufficient balance message is displayed or not
    public boolean verifyInsufficientBalanceMessage() {
        try {
            //Find alert message showing "You do not have enough balance in your wallet for this purchase"
            WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notistack-snackbar")));
            return alertMessage.getText().trim().equals("You do not have enough balance in your wallet for this purchase");
        } catch (Exception e) {
            return false;
        }
    }
}