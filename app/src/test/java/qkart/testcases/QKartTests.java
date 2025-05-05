package qkart.testcases;

import static org.testng.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import qkart.pages.*;
import qkart.utility.CommonMethods;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.utility.DriverFactory;

public class QKartTests extends CommonMethods {

    static WebDriver driver;
    public static String lastGeneratedUserName;
    static int i;
    Register registration;
    Login login;
    Home homePage;
    Checkout checkoutPage;
    PrivacyPolicy privacyPolicyPage;
    TermsOfService termsOfService;
    Thanks thanks;

    @BeforeSuite(alwaysRun = true)
    public void createDriver() {
        driver = DriverFactory.getDriver();

        registration = new Register();
        login = new Login();
        homePage = new Home();
        checkoutPage = new Checkout();
        thanks = new Thanks();
        privacyPolicyPage = new PrivacyPolicy();
        termsOfService = new TermsOfService();
    }

    @Test(enabled = true, description = "Verify a new user can successfully register", priority = 1, groups = {"Sanity_test"})
    @Parameters({"username", "password"})
    public void TestCase01(String username, String password) {

        // Visit the Registration page and register a new user
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the registered user
        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        // Visit the home page and log out the logged-in user
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify re-registering an already registered user fails", priority = 2, groups = {"Sanity_test"})
    @Parameters({"username", "password"})
    public void TestCase02(String username, String password) {

        // Visit the Registration page and register a new user
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.verifyOnRegisterPage(), "User is not on registration page");

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        assertFalse(registration.registerUser(lastGeneratedUserName, password, false), "Test Case 02: Verify re-registering an already registered user fails : FAIL");
    }

    @Test(enabled = true, description = "Verify the search box functionality ", priority = 3, groups = {"Sanity_test"})
    public void TestCase03() {

        // Visit the home page
        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        String productName = "yonex";
        // Search for the "yonex" product
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName + " product");

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        assertNotEquals(searchResults.size(), 0, "There were no results for the " + productName + " product");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultElement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultElement.getTitleOfResult();

            assertTrue(elementText.toLowerCase().contains(productName.toLowerCase()), "Test Results does not contains expected values");
        }

        productName = "Gesundheit";
        // Search for product
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName);

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        assertEquals(searchResults.size(), 0, "Result found for invalid product name " + productName);

        assertTrue(homePage.isNoResultFound(), "\"No product found\" message is not displayed for the invalid product name " + productName);
    }

    @Test(enabled = true, description = "Verify the existence of size chart for certain items and validate contents of size chart", priority = 4, groups = {"Regression_Test"})
    public void TestCase04() {

        // Visit home page
        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        String productName = "Running Shoes";
        // Search for product and get card content element of search results
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName);

        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            assertTrue(result.verifySizeChartExists(), "Size Chart button does not exist for " + productName);
            assertTrue(result.verifyExistenceOfSizeDropdown(), "Size drop down does not exist for " + productName);
            assertTrue(result.openSizeChart(), "User is unable to open size chart for " + productName);
            assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody), "Actual and Expected size chart values does not match for " + productName);
            assertTrue(result.closeSizeChart(), "User is unable to close size chart for " + productName);
        }
    }

    @Test(enabled = true, description = "Verify that a new user can add multiple products in to the cart and checkout", priority = 5, groups = {"Sanity_test"})
    @Parameters({"username", "password", "productName1", "productName2", "address"})
    public void TestCase05(String username, String password, String productName1, String productName2, String address) {
        // Go to the Register page
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        // Go to the home page
        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        // Find required products by searching and add them to the user's cart
        assertTrue(homePage.searchForProduct(productName1), "User is unable to search for " + productName1);
        assertTrue(homePage.addProductToCart(productName1), "User is unable to add product in cart for " + productName1);
        assertTrue(homePage.searchForProduct(productName2), "User is unable to search for " + productName2);
        assertTrue(homePage.addProductToCart(productName2), "User is unable to add product in cart for " + productName2);

        // Click on the checkout button
        assertTrue(homePage.clickCheckout(), "User is unable to click checkout button");

        // Add a new address on the Checkout page and select it
        assertTrue(checkoutPage.addNewAddress(address), "User is unable to add address " + address);
        assertTrue(checkoutPage.selectAddress(address), "User is unable to select address " + address);

        // Place the order
        assertTrue(checkoutPage.clickPlaceOrderBtn(), "User is unable to click place order button for selected address " + address);

        assertTrue(thanks.verifyOnThanksPage(), "User is not on thanks page after click place order button");

        // Go to the home page
        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        // Log out the user
        assertTrue(homePage.performLogout(), "User is unable to perform logout from the home page");
    }

    @Test(enabled = true, description = " Verify the quantity of items in cart can be updated", priority = 6, groups = {"Regression_Test"})
    @Parameters({"username", "password", "productName3", "productName4", "address"})
    public void TestCase06(String username, String password, String productName3, String productName4, String address) {

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");
        assertTrue(homePage.searchForProduct(productName3), "User is unable to search for " + productName3);
        assertTrue(homePage.addProductToCart(productName3), "User is unable to add product in cart for " + productName3);
        assertTrue(homePage.searchForProduct(productName4), "User is unable to search for " + productName4);
        assertTrue(homePage.addProductToCart(productName4), "User is unable to add product in cart for " + productName4);

        // update productName3 quantity to 2
        assertTrue(homePage.changeProductQuantityInCart(productName3, 5), "User is unable to change the product quantity for " + productName3);

        // update table lamp quantity to 0
        assertTrue(homePage.changeProductQuantityInCart(productName4, 0), "User is unable to change the product quantity for " + productName4);

        // update watch quantity again to 1
        assertTrue(homePage.changeProductQuantityInCart(productName3, 1), "User is unable to change the product quantity for " + productName3);

        assertTrue(homePage.clickCheckout(), "User is unable to click checkout button");

        assertTrue(checkoutPage.addNewAddress(address), "User is unable to add address " + address);
        assertTrue(checkoutPage.selectAddress(address), "User is unable to select address " + address);
        assertTrue(checkoutPage.clickPlaceOrderBtn(), "User is unable to click place order button for selected address " + address);

        assertTrue(thanks.verifyOnThanksPage(), "User is not on thanks page after click place order button");

        // Go to the home page
        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        // Log out the user
        assertTrue(homePage.performLogout(), "User is unable to perform logout from the home page");
    }

    @Test(enabled = true, description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = {"Sanity_test"})
    @Parameters({"username", "password", "productName5", "quantity", "address"})
    public void TestCase07(String username, String password, String productName5, int quantity, String address) {

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");

        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        assertTrue(homePage.searchForProduct(productName5), "User is unable to search for " + productName5);
        assertTrue(homePage.addProductToCart(productName5), "User is unable to add product in cart for " + productName5);
        assertTrue(homePage.changeProductQuantityInCart(productName5, quantity), "User is unable to change the product quantity for " + productName5);
        assertTrue(homePage.clickCheckout(), "User is unable to click checkout button");

        assertTrue(checkoutPage.addNewAddress(address), "User is unable to add address " + address);
        assertTrue(checkoutPage.selectAddress(address), "User is unable to select address " + address);
        assertTrue(checkoutPage.clickPlaceOrderBtn(), "User is unable to click place order button for selected address " + address);

        assertTrue(checkoutPage.verifyInsufficientBalanceMessage(), "\"You do not have enough balance in your wallet for this purchase\" is not displayed");

        // Go to the home page
        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        // Log out the user
        assertTrue(homePage.performLogout(), "User is unable to perform logout from the home page");
    }

    @Test(enabled = true, description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = {"Regression_Test"})
    @Parameters({"username", "password"})
    public void TestCase08(String username, String password) throws InterruptedException {

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");

        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        assertTrue(homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is not visible after successful login");
        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        String productName = "YONEX Smash Badminton Racquet";
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName);
        assertTrue(homePage.addProductToCart(productName), "User is unable to add product in cart for " + productName);

        assertTrue(homePage.clickPrivacyPolicyBtn(), "User is unable to click Privacy Policy button");

        Set<String> handles = driver.getWindowHandles();

        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to new tab");
        Thread.sleep(5000);
        assertTrue(homePage.navigateToHome(), "User is  unable to navigate to home page after switch to new tab");

        List<String> expectedResult = List.of("YONEX Smash Badminton Racquet");
        assertTrue(homePage.verifyCartContents(expectedResult), "Expected item is not present in the cart of new tab");

        driver.close();

        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window");

        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify that privacy policy and about us links are working fine", priority = 9, groups = {"Regression_Test"})
    @Parameters({"username", "password"})
    public void TestCase09(String username, String password) throws InterruptedException {

        assertTrue(registration.navigateToRegisterPage(),  "User is unable to navigate to register page");
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        assertTrue(homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is not visible after successful login");
        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        assertTrue(homePage.clickPrivacyPolicyBtn(), "User is unable to click Privacy Policy button");
        assertEquals(getCurrentUrl(), homePage.getHomePageUrl(), "User is not on home page after clicking Privacy Policy button");

        Set<String> handles = driver.getWindowHandles();
        Thread.sleep(1500);
        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to new Privacy Policy window");

        assertEquals(privacyPolicyPage.getPrivacyPolicyHeadingText().trim(), "Privacy Policy", "Privacy Policy heading does not match");

        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window");

        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        assertTrue(homePage.clickTermsOfServiceBtn(), "User is unable to click Terms of Service button");

        handles = driver.getWindowHandles();

        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[2]), "User is unable to switch to new Terms of Service window");

        assertEquals(termsOfService.getTermsOfServiceHeadingText().trim(), "Terms of Service", "Terms of Service heading does not match");

        driver.close();
        assertTrue(switchAndCloseTheWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to Privacy Policy window and close it");
        assertTrue(switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window and close it");
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify that the contact us dialog works fine", priority = 10, groups = {"Regression_Test"})
    public void TestCase10() {

        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");

        assertTrue(homePage.clickContactUsBtn(), "User is unable to click Contact Us button");

        String name = "crio user";
        String email = "criouser@gmail.com";
        String message = "Testing the contact us page";

        assertTrue(homePage.sendKeysToNameTextbox(name), "User is unable to enter " + name + " in name text box");
        assertTrue(homePage.sendKeysToEmailTextbox(email), "User is unable to enter " + email + " in email text box");
        assertTrue(homePage.sendKeysToMessageTextbox(message), "User is unable to enter " + message + " in message text box");

        assertTrue(homePage.clickContactNowBtn(), "User is unable to click Contact Now button after fill all details");

        assertTrue(homePage.waitForInvisibilityOfContactNowBtn(), "Contact Now button is still visible after clicking it");
    }

    @Test(enabled = true, description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = {"Sanity_test"})
    @Parameters({"username", "password"})
    public void TestCase11(String username, String password) throws InterruptedException {

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.registerUser(username, password, true), "User is unable to register with new username and password");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(lastGeneratedUserName, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(lastGeneratedUserName), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        String productName = "YONEX Smash Badminton Racquet";
        int quantity = 4;
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName);
        assertTrue(homePage.addProductToCart(productName), "User is unable to add product in cart for " + productName);
        assertTrue(homePage.changeProductQuantityInCart(productName, quantity), "User is unable to change the product quantity for " + productName);
        assertTrue(homePage.clickCheckout(), "User is unable to click checkout button");

        String address = "Addr line 1  addr Line 2  addr line 3";

        assertTrue(checkoutPage.addNewAddress(address), "User is unable to add address " + address);
        assertTrue(checkoutPage.selectAddress(address), "User is unable to select address " + address);
        assertTrue(checkoutPage.clickPlaceOrderBtn(), "User is unable to click place order button for selected address " + address);

        assertTrue(thanks.verifyOnThanksPage(), "User is not on thanks page after click place order button");

        List<WebElement> advertisements = thanks.getAdvertisementIFrame();
        assertEquals(advertisements.size(), 3, "Advertisement IFrame is more 3");

        assertTrue(switchToIFrameByIndex(0), "User is unable to switch to the first advertisement iframe");
        assertTrue(thanks.clickBuyNowBtn(), "User is unable to click Buy Now button inside first advertisement iframe");
        assertTrue(checkoutPage.verifyOnCheckoutPage(), "User is not redirected to checkout page after clicking Buy Now button in first advertisement iframe");
        assertTrue(navigateBack(), "User is unable to navigate back ");
        Thread.sleep(2000);

        assertTrue(switchToIFrameByIndex(1), "User is unable to switch to the second advertisement iframe");
        assertTrue(thanks.clickBuyNowBtn(), "User is unable to click Buy Now button inside second advertisement iframe");
        assertTrue(checkoutPage.verifyOnCheckoutPage(), "User is not redirected to checkout page after clicking Buy Now button in first advertisement iframe");

        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @AfterSuite
    public static void quitDriver() {
        driver.quit();
    }

    public static void createScreenshotFolder() {
        try {
            for (i = 1; i <= 50; i++) {
                File theDir = new File("screenshots/ss" + i);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                    break;
                }
            }
        } catch (Exception e) {
            assertFalse(false, "Creating Screenshot/ss folder : Fail\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll(".:", "");
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/ss" + i + "/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
        }
    }
}