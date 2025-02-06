package lakeshsahu22.Module1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    WebDriver driver;
    WebDriverWait wait;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            driver.findElement(By.xpath("//button[text()='Add new address']")).click();
            WebElement addressTextarea = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Enter your complete address']")));
            addressTextarea.sendKeys(addresString);
            driver.findElement(By.xpath("//button[text()='Add']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='" + addresString + "']")));
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            driver.findElement(
                    By.xpath("//p[text()='" + addressToSelect + "']/preceding-sibling::span/input[@type='radio']"))
                    .click();
            ;
            return true;
            // System.out.println("Unable to find the given address");
            // return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it
            driver.findElement(By.xpath("//button[text()='PLACE ORDER']")).click();
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
            WebElement alert = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='notistack-snackbar']")));

            if (alert.getText().equals("You do not have enough balance in your wallet for this purchase")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
