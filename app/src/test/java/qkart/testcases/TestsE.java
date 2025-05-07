package qkart.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.pages.*;
import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;

import java.util.List;

import static org.testng.Assert.*;

public class TestsE {

    public static WebDriver driver;
    Register registration;
    Login login;
    Home homePage;
    Checkout checkoutPage;
    PrivacyPolicy privacyPolicyPage;
    TermsOfService termsOfService;
    Thanks thanks;
    CommonMethods cm;

    @BeforeSuite(alwaysRun = true)
    public void createDriver() {
        driver = DriverFactory.getDriver();
        registration = new Register(driver);
        login = new Login(driver);
        homePage = new Home(driver);
        checkoutPage = new Checkout(driver);
        thanks = new Thanks(driver);
        privacyPolicyPage = new PrivacyPolicy(driver);
        termsOfService = new TermsOfService(driver);
        cm = new CommonMethods(driver);
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
            SearchResult resultElement = new SearchResult(driver, webElement);

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

    @Test(enabled = true, description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = {"Sanity_test"})
    @Parameters({"usernameB", "password", "productName5", "quantity", "address"})
    public void TestCase07(String username, String password, String productName5, int quantity, String address) {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");

        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

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

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}