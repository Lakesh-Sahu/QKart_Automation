package QKART_TESTNG.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Home {
    WebDriver driver;

    // Home page url
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    // Constructor of Home class
    public Home(WebDriver driver) {
        this.driver = driver;
    }

    // Navigating to the home page
    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    // Performing logout
    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Locating the logout button
            WebElement logoutBtn = driver.findElement(By.className("MuiButton-text"));

            // Clicking on the Logout Button
            logoutBtn.click();

            // Creating object of WebDriverWait class
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Waiting for the invisibility of Logout button
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[text()='Logout']")));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Returns Boolean if searching for the given product name occurs without any
    // errors
    public Boolean searchForProduct(String product) {
        try {
            // Locating the search box
            WebElement searchBox = driver.findElement(By.xpath("//input[@name='search'][1]"));

            // Clearing the search box
            searchBox.clear();

            // Entering the product name to the search bar
            searchBox.sendKeys(product);

            // Creating object of WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Waiting for the product name to be visible on product card or for "No
            // products found"
            wait.until(ExpectedConditions.or(
                    ExpectedConditions
                            .presenceOfElementLocated(By.xpath(String.format("//p[contains(text(),'%s')]", product))),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[@id='root']//h4[contains(text(), 'No products found')]"))));

            // Waiting for all elements or products to load
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    // Returns ArrayList of all the card content of search result
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<>();
        try {
            // Finds all the result card content of each of search results and return if no
            // result is found then it returns empty ArrayList
            searchResults = driver.findElements(By.className("css-sycj1h"));
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    // Returns Boolean based on if the "No products found" text is displayed
    public Boolean isNoResultFound() {
        boolean status = false;
        try {
            // Checks the presence of "No products found" text in the web page
            status = driver.findElement(By.xpath("//*[@id='root']//h4[contains(text(),'No products found')]"))
                    .isDisplayed();
            return status;
        } catch (Exception e) {
            return false;
        }
    }

    // Return Boolean if add product to cart is successful
    public Boolean addProductToCart(String productName) {
        try {
            // Iterate through each product on the page to find the WebElement corresponding
            // to the matching productName
            List<WebElement> cardContents = getSearchResults();
            for (WebElement card : cardContents) {
                if (productName.contains(card.findElement(By.className("css-yg30e6")).getText())) {
                    card.findElement(By.tagName("button")).click();

                    // Waiting for the product to show on cart section
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            String.format("//*[@class='MuiBox-root css-1gjj37g']/div[1][text()='%s']", productName))));

                    // return true if product is add to the cart
                    return true;
                }
            }

            // If product is not found in the result cards then return false
            System.out.println("Unable to find the given product: " + productName);
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    // Return Boolean denoting the status of clicking on the checkout button
    public Boolean clickCheckout() {
        try {
            // Locate and click on the Checkout button, if successful return true
            WebElement checkoutBtn = driver.findElement(By.className("checkout-btn"));
            checkoutBtn.click();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return false;
        }
    }

    // Return Boolean denoting the status of change quantity of product in cart
    // operation
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // Quantity is reached (Note: Keep a look-out when then input quantity is 0 or
            // less than 0, here we need to remove the item completely from the cart)

            // Locating the cart section
            WebElement cartParent = driver.findElement(By.className("cart"));

            // Locating the products in the cart section through above
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            int currentQty;

            // Iterating through each product in the card and matching its title content the
            // required product name or not
            for (WebElement item : cartContents) {
                // Find the matching product from the cart items
                if (productName.contains(
                        item.findElement(By.xpath("//*[@class='MuiBox-root css-1gjj37g']/div[1]")).getText())) {

                    // Getting the current product quantity
                    currentQty = Integer.parseInt(item.findElement(By.className("css-olyig7")).getText());

                    // Clicking on the + or - buttons appropriately to set the correct quantity of
                    // the product
                    while (currentQty != quantity) {
                        if (currentQty < quantity) {
                            // increase quantity
                            item.findElements(By.tagName("button")).get(1).click();

                        } else {
                            // decrease quantity
                            item.findElements(By.tagName("button")).get(0).click();
                        }

                        // Resolve the synchronization issue for multiple thread, synchronized block
                        // locks the driver object
                        synchronized (driver) {
                            driver.wait(2000);
                        }

                        // Updating the current product quantity
                        currentQty = Integer
                                .parseInt(item.findElement(By.xpath("//div[@data-testid='item-qty']")).getText());
                    }
                    // Return true when current quantity becomes equals to the required quantity
                    return true;
                }
            }
            // Return false if no product name matches
            return false;
        } catch (Exception e) {
            // If required quantity is 0 or less than 0 then it comes in this block
            if (quantity <= 0)
                return true;
            System.out.println(("exception occurred when updating cart" + e.getMessage()));
            return false;
        }
    }

    // Return Boolean denoting if the cart contains items as expected
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // Get all the cart items as an array of webelements

            // Iterate through expectedCartContents and check if item with matching product
            // name is present in the cart

            WebElement cartParent = driver.findElement(By.xpath("//div[@class='cart MuiBox-root css-0']"));
            List<WebElement> cartContents = cartParent.findElements(By.xpath(".//div[@class='MuiBox-root css-0']"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                // To trim as getText() trims cart item title
                if (!actualCartContents.contains(expected.trim())) {
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