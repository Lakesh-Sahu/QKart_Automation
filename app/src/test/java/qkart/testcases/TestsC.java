package qkart.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.pages.*;
import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

public class TestsC {

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

    @Test(enabled = true, description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = {"Regression_Test"})
    @Parameters({"usernameB", "password"})
    public void TestCase08(String username, String password) throws InterruptedException {

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

        assertTrue(homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is not visible after successful login");
        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        String productName = "YONEX Smash Badminton Racquet";
        assertTrue(homePage.searchForProduct(productName), "User is unable to search for " + productName);
        assertTrue(homePage.addProductToCart(productName), "User is unable to add product in cart for " + productName);

        assertTrue(homePage.clickPrivacyPolicyBtn(), "User is unable to click Privacy Policy button");

        Set<String> handles = driver.getWindowHandles();

        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to new tab");
        Thread.sleep(5000);
        assertTrue(homePage.navigateToHome(), "User is  unable to navigate to home page after switch to new tab");

        List<String> expectedResult = List.of("YONEX Smash Badminton Racquet");
        assertTrue(homePage.verifyCartContents(expectedResult), "Expected item is not present in the cart of new tab");

        driver.close();

        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window");

        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify that privacy policy and about us links are working fine", priority = 9, groups = {"Regression_Test"})
    @Parameters({"usernameB", "password"})
    public void TestCase09(String username, String password) throws InterruptedException {

        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");

        assertTrue(login.verifyOnLoginPage(), "User is not redirected to login page");
        assertTrue(login.performLogin(usernameDynamic, password), "User is unable to login with registered user credentials");
        assertTrue(login.verifyUserLoggedIn(usernameDynamic), "Login successful but user is not logged in");

        assertTrue(homePage.verifyOnHomePage(), "User is not redirected to home page");

        assertTrue(homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is not visible after successful login");
        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        assertTrue(homePage.clickPrivacyPolicyBtn(), "User is unable to click Privacy Policy button");
        assertEquals(cm.getCurrentUrl(), homePage.getHomePageUrl(), "User is not on home page after clicking Privacy Policy button");

        Set<String> handles = driver.getWindowHandles();
        Thread.sleep(1500);
        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to new Privacy Policy window");

        assertEquals(privacyPolicyPage.getPrivacyPolicyHeadingText().trim(), "Privacy Policy", "Privacy Policy heading does not match");

        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window");

        assertTrue(homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is still visible");

        assertTrue(homePage.clickTermsOfServiceBtn(), "User is unable to click Terms of Service button");

        handles = driver.getWindowHandles();

        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[2]), "User is unable to switch to new Terms of Service window");

        assertEquals(termsOfService.getTermsOfServiceHeadingText().trim(), "Terms of Service", "Terms of Service heading does not match");

        driver.close();
        assertTrue(cm.switchAndCloseTheWindow(handles.toArray(new String[handles.size()])[1]), "User is unable to switch to Privacy Policy window and close it");
        assertTrue(cm.switchToWindow(handles.toArray(new String[handles.size()])[0]), "User is unable to switch to original window and close it");
        assertTrue(homePage.performLogout(), "User is unable to logout after login from home page");
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}