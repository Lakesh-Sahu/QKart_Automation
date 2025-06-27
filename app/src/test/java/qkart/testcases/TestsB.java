package qkart.testcases;

import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.utility.ObjectManager;
import qkart.utility.ObjectCreator;
import qkart.utility.Base;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Set;

public class TestsB extends Base {

    @Test(enabled = true, description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = {"Sanity_test"})
    @Parameters({"usernameB", "password", "productName5", "quantity", "address"})
    public void TestCase07(String username, String password, String productName5, int quantity, String address) {

        ObjectCreator oc = ObjectManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");

        // Register a new user
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");

        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");

        assertTrue(oc.homePage.searchForProduct(productName5), "User is able to search for " + productName5);
        assertTrue(oc.homePage.addProductToCart(productName5), "User is able to add product in cart for " + productName5);
        assertTrue(oc.homePage.changeProductQuantityInCart(productName5, quantity), "User is able to change the product quantity for " + productName5);
        assertTrue(oc.homePage.clickCheckout(), "User is able to click checkout button");

        assertTrue(oc.checkoutPage.addNewAddress(address), "User is able to add address " + address);
        assertTrue(oc.checkoutPage.selectAddress(address), "User is able to select address " + address);
        assertTrue(oc.checkoutPage.clickPlaceOrderBtn(), "User is able to click place order button for selected address " + address);

        assertTrue(oc.checkoutPage.verifyInsufficientBalanceMessage(), "\"You do not have enough balance in your wallet for this purchase\" is displayed");

        // Go to the home page
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        // Log out the user
        assertTrue(oc.homePage.performLogout(), "User is able to perform logout from the home page");
    }

    @Test(enabled = true, description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = {"Regression_Test"})
    @Parameters({"usernameB", "password"})
    public void TestCase08(String username, String password) throws InterruptedException {

        ObjectCreator oc = ObjectManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");

        // Register a new user
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");

        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");

        assertTrue(oc.homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is visible after successful login");
        assertTrue(oc.homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner become invisible");

        String productName = "YONEX Smash Badminton Racquet";
        assertTrue(oc.homePage.searchForProduct(productName), "User is able to search for " + productName);
        assertTrue(oc.homePage.addProductToCart(productName), "User is able to add product in cart for " + productName);

        assertTrue(oc.homePage.clickPrivacyPolicyBtn(), "User is able to click Privacy Policy button");

        Set<String> handles = oc.cm.getWindowHandles();

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[1]), "User is able to switch to new tab");
        Thread.sleep(5000);
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page after switch to new tab");

        List<String> expectedResult = List.of("YONEX Smash Badminton Racquet");
        assertTrue(oc.homePage.verifyCartContents(expectedResult), "Expected item is present in the cart of new tab");

        assertTrue(oc.cm.closeWindow(), "User is able to close the privacy policy new tab");

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[0]), "User is able to switch to original window");

        assertTrue(oc.homePage.performLogout(), "User is able to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify that Privacy Policy, Terms of Service and About Us links are working fine", priority = 9, groups = {"Regression_Test"})
    @Parameters({"usernameB", "password"})
    public void TestCase09(String username, String password) throws InterruptedException {

        ObjectCreator oc = ObjectManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");
        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");

        assertTrue(oc.homePage.waitForVisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner is visible after successful login");
        assertTrue(oc.homePage.waitForInvisibilityOfLoggedInSuccessfullyBanner(), "Logged in successfully banner become invisible");

        assertTrue(oc.homePage.clickPrivacyPolicyBtn(), "User is able to click Privacy Policy button");
        assertEquals(oc.cm.getCurrentUrl(), oc.homePage.getHomePageUrl(), "User is on home page after clicking Privacy Policy button");

        Set<String> handles = oc.cm.getWindowHandles();
        Thread.sleep(1500);
        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[1]), "User is able to switch to new Privacy Policy window");

        assertEquals(oc.privacyPolicyPage.getPrivacyPolicyHeadingText().trim(), "Privacy Policy", "Privacy Policy heading matches");

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[0]), "User is able to switch to original window");

        assertTrue(oc.homePage.clickTermsOfServiceBtn(), "User is able to click Terms of Service button");

        handles = oc.cm.getWindowHandles();

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[2]), "User is able to switch to new Terms of Service window");

        assertEquals(oc.termsOfService.getTermsOfServiceHeadingText().trim(), "Terms of Service", "Terms of Service heading matches");

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[0]), "User is able to switch to original window");

        assertTrue(oc.homePage.clickAboutUsBtn(), "User is able to click About Us button");

        handles = oc.cm.getWindowHandles();

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[3]), "User is able to switch to new About Us window");

        assertEquals(oc.aboutUs.getAboutUsHeadingText().trim(), "About Us", "About Us heading matches");

        assertTrue(oc.cm.closeWindow(), "User is able to close the About Us new tab");

        assertTrue(oc.cm.switchAndCloseTheWindow(handles.toArray(new String[0])[2]), "User is able to switch to Terms of Service window and close it");

        assertTrue(oc.cm.switchAndCloseTheWindow(handles.toArray(new String[0])[1]), "User is able to switch to Privacy Policy window and close it");

        assertTrue(oc.cm.switchToWindow(handles.toArray(new String[0])[0]), "User is able to switch to original window and close it");

        assertTrue(oc.homePage.performLogout(), "User is able to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify that the contact us dialog works fine", priority = 10, groups = {"Regression_Test"})
    public void TestCase10() {
        ObjectCreator oc = ObjectManager.getContext();

        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        assertTrue(oc.homePage.clickContactUsBtn(), "User is able to click Contact Us button");

        String name = "crio user";
        String email = "criouser@gmail.com";
        String message = "Testing the contact us page";

        assertTrue(oc.homePage.sendKeysToNameTextbox(name), "User is able to enter " + name + " in name text box");
        assertTrue(oc.homePage.sendKeysToEmailTextbox(email), "User is able to enter " + email + " in email text box");
        assertTrue(oc.homePage.sendKeysToMessageTextbox(message), "User is able to enter " + message + " in message text box");

        assertTrue(oc.homePage.clickContactNowBtn(), "User is able to click Contact Now button after fill all details");

        assertTrue(oc.homePage.waitForInvisibilityOfContactNowBtn(), "Contact Now button become invisible after clicking it");
    }

    @Test(enabled = true, description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = {"Sanity_test"})
    @Parameters({"usernameB", "password"})
    public void TestCase11(String username, String password) throws InterruptedException {
        ObjectCreator oc = ObjectManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        assertTrue(oc.login.verifyOnLoginPage(), "User is able redirected to login page");
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");
        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");

        String productName = "YONEX Smash Badminton Racquet";
        int quantity = 4;
        assertTrue(oc.homePage.searchForProduct(productName), "User is able to search for " + productName);
        assertTrue(oc.homePage.addProductToCart(productName), "User is able to add product in cart for " + productName);
        assertTrue(oc.homePage.changeProductQuantityInCart(productName, quantity), "User is able to change the product quantity for " + productName);
        assertTrue(oc.homePage.clickCheckout(), "User is able to click checkout button");

        String address = "Addr line 1  addr Line 2  addr line 3";

        assertTrue(oc.checkoutPage.addNewAddress(address), "User is able to add address " + address);
        assertTrue(oc.checkoutPage.selectAddress(address), "User is able to select address " + address);
        assertTrue(oc.checkoutPage.clickPlaceOrderBtn(), "User is able to click place order button for selected address " + address);

        assertTrue(oc.thanks.verifyOnThanksPage(), "User is redirected to thanks page after click place order button");

        List<WebElement> advertisements = oc.thanks.getAdvertisementIFrame();
        int advertisementSizeExpected = 3;
        assertEquals(advertisements.size(), advertisementSizeExpected, "Number of Advertisement IFrame is as expected " + advertisementSizeExpected);

        assertTrue(oc.cm.switchToIFrameByIndex(0), "User is able to switch to the first advertisement iframe");
        assertTrue(oc.thanks.clickBuyNowBtn(), "User is able to click Buy Now button inside first advertisement iframe");
        assertTrue(oc.checkoutPage.verifyOnCheckoutPage(), "User is redirected to checkout page after clicking Buy Now button in first advertisement iframe");
        assertTrue(oc.cm.navigateBack(), "User is able to navigate back ");
        Thread.sleep(2000);

        assertTrue(oc.cm.switchToIFrameByIndex(1), "User is able to switch to the second advertisement iframe");
        assertTrue(oc.thanks.clickBuyNowBtn(), "User is able to click Buy Now button inside second advertisement iframe");
        assertTrue(oc.checkoutPage.verifyOnCheckoutPage(), "User is redirected to checkout page after clicking Buy Now button in first advertisement iframe");

        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");
        assertTrue(oc.homePage.performLogout(), "User is able to logout after login from home page");
    }
}
