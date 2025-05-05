package qkart.pages;

import java.util.ArrayList;
import java.util.List;

import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Home extends CommonMethods {
    WebDriver driver;
    WebDriverWait wait;

    // Home page url
    String url = "https://crio-qkart-frontend-qa.vercel.app/";

    // Constructor of Home class
    public Home() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean verifyOnHomePage() {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            return false;
        }
    }

    // Navigating to the home page
    public boolean navigateToHome() {
        try {
            driver.get(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getHomePageUrl() {
        try {
            return url;
        } catch (Exception e) {
            return "";
        }
    }

    public boolean waitForVisibilityOfLoggedInSuccessfullyBanner() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Logged in successfully']"))) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForInvisibilityOfLoggedInSuccessfullyBanner() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Logged in successfully']")));
        } catch (Exception e) {
            return false;
        }
    }

    // Performing logout
    public boolean performLogout() {
        try {
            // Locate and click on the Logout Button
            boolean status = click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiButton-text"))));

            // Waiting for the invisibility of Logout button
            return status && wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[text()='Logout']")));
        } catch (Exception e) {
            return false;
        }
    }

    // Returns Boolean if searching for the given product name occurs without any errors
    public boolean searchForProduct(String product) {
        try {
            // Locating the search box
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='search'])[1]")));

            // Entering the product name to the search bar
            sendKeys(searchBox, product);

            String productNameInLower = product.toLowerCase();
            // Waiting for the product name to be visible on product card or for "No products found"
            boolean productOrNoProductFoundStatus = wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//p[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]", productNameInLower))), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']//h4[contains(text(), 'No products found')]"))));

            // Waiting for all elements or products to load
            Thread.sleep(3000);
            return productOrNoProductFoundStatus;
        } catch (Exception e) {
            return false;
        }
    }

    // Finds all the result card content of each of search results and return; if no result is found then it returns empty ArrayList
    public List<WebElement> getSearchResults() {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("css-sycj1h")));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Returns boolean based on if the "No products found" text is displayed
    public boolean isNoResultFound() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']//h4[contains(text(),'No products found')]"))) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean if add product to cart is successful
    public boolean addProductToCart(String productName) {
        try {
            // Iterate through each product on the page to find the WebElement corresponding to the matching productName
            List<WebElement> cardContents = getSearchResults();
            for (WebElement card : cardContents) {
                if (productName.contains(findElementFromParentByClassName(wait, card, "css-yg30e6").getText())) {
                    click(card.findElement(By.tagName("button")));

                    // Wait for the product to show on cart section and return true if product is added to the cart
                    return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(String.format("//*[@class='MuiBox-root css-1gjj37g']/div[1][normalize-space()='%s']", productName)))) != null;
                }
            }
            // If product is not found in the result cards then return false
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Locate and click on the Checkout button, if successful return true
    public boolean clickCheckout() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("checkout-btn"))));
        } catch (Exception e) {
            return false;
        }
    }

    // Return boolean denoting the status of change quantity of product in cart operation
    public boolean changeProductQuantityInCart(String productName, int quantity) {
        try {
            // If quantity is reached 0 or less than 0 then remove the item completely from the cart

            // Locating the cart section
            WebElement cartParent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart")));

            // Locating the products in the cart section through above
//            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));
            List<WebElement> cartContents = findElementsFromParentByClassName(wait, cartParent, "css-zgtx0t");

            int currentQty;

            // Iterating through each product in the card and matching its title content the
            // required product name or not
            for (WebElement item : cartContents) {
                // Find the matching product from the cart items
                if (productName.contains(findElementFromParentByXPath(wait, item, ".//*[@class='MuiBox-root css-1gjj37g']/div[1]").getText())) {

                    // Getting the current product quantity
                    currentQty = Integer.parseInt(findElementFromParentByClassName(wait, item, "css-olyig7").getText());

                    // Clicking on the + or - buttons appropriately to set the correct quantity of the product
                    while (currentQty != quantity) {
                        if (currentQty < quantity) {
                            // increase quantity
                            click(findElementsFromParentByTagName(wait, item, "button").get(1));
                        } else {
                            // decrease quantity
                            click(findElementsFromParentByTagName(wait, item, "button").get(0));
                        }
                        // Resolve the synchronization issue for multiple thread, synchronized block
                        // locks the driver object
                        Thread.sleep(2000);
                        // Updating the current product quantity
                        currentQty = Integer.parseInt(findElementFromParentByXPath(wait, item, ".//div[@data-testid='item-qty']").getText());
                    }
                    // Return true when current quantity becomes equals to the required quantity
                    return true;
                }
            }
            // Return false if no product name matches
            return false;
        } catch (Exception e) {
            // If required quantity is 0 or less than 0 then it comes in this block
            return quantity <= 0;
        }
    }

    // Return boolean denoting if the cart contains items as expected
    public boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // Get all the cart items as an array of webelements
            // Iterate through expectedCartContents and check if item with matching product name is present in the cart

            WebElement cartParent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='cart MuiBox-root css-0']")));
            List<WebElement> cartContents = findElementsFromParentByXPath(wait, cartParent, ".//div[@class='MuiBox-root css-0']");

            ArrayList<String> actualCartContents = new ArrayList<>() {
            };

            for (WebElement cartItem : cartContents) {
                actualCartContents.add(findElementFromParentByXPath(wait, cartItem, "(//div[contains(@class,'css-1gjj37g')]/div)[position() mod 2 = 1]").getText().trim());
            }

            for (String expected : expectedCartContents) {
                // To trim as getText() trims cart item title
                if (!actualCartContents.contains(expected.trim())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickPrivacyPolicyBtn() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Privacy policy"))));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickTermsOfServiceBtn() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Terms of Service"))));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickContactUsBtn() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Contact us']"))));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendKeysToNameTextbox(String text) {
        try {
            return sendKeys(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Name']"))), text);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendKeysToEmailTextbox(String text) {
        try {
            return sendKeys(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']"))), text);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendKeysToMessageTextbox(String text) {
        try {
            return sendKeys(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Message']"))), text);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickContactNowBtn() {
        try {
            return click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Contact Now']"))));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForInvisibilityOfContactNowBtn() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[normalize-space()='Contact Now']")));
        } catch (Exception e) {
            return false;
        }
    }
}