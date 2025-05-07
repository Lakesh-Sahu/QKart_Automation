package qkart.testcases;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Set;

import qkart.pages.*;
import qkart.utility.CommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.utility.DriverFactory;

public class TestsA {

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

    @Test(enabled = true, description = "Verify that a new user can add multiple products in to the cart and checkout", priority = 5, groups = {"Sanity_test"})
    @Parameters({"usernameA", "password", "productName1", "productName2", "address"})
    public void TestCase05(String username, String password, String productName1, String productName2, String address) {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        // Go to the Register page
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        // Go to the login page
        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

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

    @Test(enabled = true, description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = {"Sanity_test"})
    @Parameters({"usernameB", "password"})
    public void TestCase11(String username, String password) throws InterruptedException {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

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

        assertTrue(cm.switchToIFrameByIndex(0), "User is unable to switch to the first advertisement iframe");
        assertTrue(thanks.clickBuyNowBtn(), "User is unable to click Buy Now button inside first advertisement iframe");
        assertTrue(checkoutPage.verifyOnCheckoutPage(), "User is not redirected to checkout page after clicking Buy Now button in first advertisement iframe");
        assertTrue(cm.navigateBack(), "User is unable to navigate back ");
        Thread.sleep(2000);

        assertTrue(cm.switchToIFrameByIndex(1), "User is unable to switch to the second advertisement iframe");
        assertTrue(thanks.clickBuyNowBtn(), "User is unable to click Buy Now button inside second advertisement iframe");
        assertTrue(checkoutPage.verifyOnCheckoutPage(), "User is not redirected to checkout page after clicking Buy Now button in first advertisement iframe");

        assertTrue(homePage.navigateToHome(), "User is unable to navigate to home page");
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}