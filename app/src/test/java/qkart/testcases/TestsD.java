package qkart.testcases;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import qkart.pages.*;
import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;

import static org.testng.Assert.*;

public class TestsD {

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

    @Test(enabled = true, description = "Verify a new user can successfully register", priority = 1, groups = {"Sanity_test"})
    @Parameters({"usernameA", "password"})
    public void TestCase01(String username, String password) {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        // Visit the Registration page and register a new user
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        // Visit the login page and login with the registered user
        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

        // Visit the home page and log out the logged-in user
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @Test(enabled = true, description = " Verify the quantity of items in cart can be updated", priority = 6, groups = {"Regression_Test"})
    @Parameters({"usernameA", "password", "productName3", "productName4", "address"})
    public void TestCase06(String username, String password, String productName3, String productName4, String address) {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");

        // Register a new user
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

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

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}