package QKART_TESTNG.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Checkout {
    WebDriver driver;

    // Checkout page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    // Constructor of Checkout class
    public Checkout(WebDriver driver) {
        this.driver = driver;
    }

    // Navigate to checkout page
    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    // Return Boolean denoting the status of Adding new address in checkout page
    public Boolean addNewAddress(String addresString) {
        try {
            // Click on the "Add new address" button, enter the address String in the
            // address text box and click on the "ADD" button to save the address
            WebElement addNewAddressBtn = driver.findElement(By.id("add-new-btn"));
            addNewAddressBtn.click();

            WebElement addressBox = driver.findElement(By.className("MuiOutlinedInput-input"));
            addressBox.clear();
            addressBox.sendKeys(addresString);
            WebElement addBtn = driver
                    .findElement(By.xpath("//div[contains(@class,'shipping-container')]//button[text()='Add']"));
            addBtn.click();

            // Wait for the newly added address to show
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                    "//*[@class='MuiTypography-root MuiTypography-body1 css-yg30e6' and text()='%s']", addresString))));
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;
        }
    }

    // Return Boolean denoting the status of Selecting an available address
    public Boolean selectAddress(String addressToSelect) {
        try {
            // Get the parent section of addresses
            WebElement parentBox = driver
                    .findElement(By.xpath("//h4[text()='Shipping']/following-sibling::div[contains(@class,'css-0')]"));

            // Get all the address text WebElement
            List<WebElement> allBoxes = parentBox.findElements(By.className("address-item"));

            // Select the matching address
            for (WebElement box : allBoxes) {
                if (box.findElement(By.className("css-yg30e6")).getText().replaceAll(" ", "")
                        .equals(addressToSelect.replaceAll(" ", ""))) {
                    box.findElement(By.tagName("input")).click();
                    return true;
                }
            }
            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }
    }

    // Return Boolean denoting the status of place order action
    public Boolean placeOrder() {
        try {
            // Find the "PLACE ORDER" button and click on it
            WebElement placeOrderBtn = driver.findElement(By.xpath("//button[text()='PLACE ORDER']"));
            placeOrderBtn.click();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    // Return Boolean denoting the insufficient balance message is displayed or not
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            //Find alert message showing "You do not have enough balance in your wallet for this purchase"
            WebElement alertMessage = driver.findElement(By.id("notistack-snackbar"));
            if (alertMessage.isDisplayed()) {
                if (alertMessage.getText().equals("You do not have enough balance in your wallet for this purchase")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}