package lakeshsahu22.Module1;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
// import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    WebDriver driver;
    WebDriverWait wait;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // SLEEP_STMT_10: Wait for Logout to complete
            // Wait for Logout to Complete
            Thread.sleep(5000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            driver.findElement(By.xpath("//input[@name='search']")).clear();
            driver.findElement(By.xpath("//input[@name='search']")).sendKeys(product);
            wait.until(ExpectedConditions.or(
                    ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//button[text()='Add to cart']")),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//h4[text()=' No products found ']"))));
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            Thread.sleep(3000);
            searchResults = driver.findElements(By.xpath(
                    "//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"));
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            driver.findElement(By.xpath("//h4[text()=' No products found ']"));
            status = true;
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the
             * matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            List<WebElement> results = getSearchResults();
            for (WebElement result : results) {
                SearchResult sr = new SearchResult(result);
                if (sr.getTitleofResult().equalsIgnoreCase(productName)) {
                    sr.parentElement.findElement(By.xpath(".//button[text()='Add to cart']"))
                            .click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(text(),'" + productName + "')]")));
                    return true;
                }
            }
            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            driver.findElement(By.xpath("//button[text()='Checkout']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Add new address']")));
            status = true;
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TO DO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(
                    (By.xpath("//div[@class='MuiBox-root css-0']//div[contains(text(),'" + productName + "')]"))));
            WebElement reduce = parent.findElement(By.xpath(
                    "./following-sibling::div//*[name()='svg' and @data-testid='RemoveOutlinedIcon']/parent::button"));
            WebElement increase = parent.findElement(By.xpath(
                    "./following-sibling::div//*[name()='svg' and @data-testid='AddOutlinedIcon']/parent::button"));
            int itemQuan = Integer.valueOf(parent
                    .findElement(By.xpath("./following-sibling::div//div[@data-testid='item-qty']"))
                    .getText());
            // Find the item on the cart with the matching productName
            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)
            int tempItemQuanDisplay = itemQuan;
            if (quantity < itemQuan) {
                for (int i = 0; i < itemQuan - quantity; i++) {
                    reduce.click();
                    // Thread.sleep(4000);
                    wait.until(ExpectedConditions.textToBe(
                            By.xpath("//div[@class='MuiBox-root css-0']//div[text()='" + productName
                                    + "']//following-sibling::div//div[@data-testid='item-qty']"),
                            String.valueOf(tempItemQuanDisplay - 1)));
                    tempItemQuanDisplay -= 1;
                }
            } else if (quantity > itemQuan) {
                for (int i = 0; i < quantity - itemQuan; i++) {
                    increase.click();
                    // Thread.sleep(4000);
                    wait.until(ExpectedConditions.textToBe(
                            By.xpath("//div[@class='MuiBox-root css-0']//div[text()='" + productName
                                    + "']//following-sibling::div//div[@data-testid='item-qty']"),
                            String.valueOf(tempItemQuanDisplay + 1)));
                    tempItemQuanDisplay += 1;
                }
            }

            return true;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(
                        cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
