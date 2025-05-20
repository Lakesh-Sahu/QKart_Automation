package qkart.testcases;

import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.pages.SearchResult;
import qkart.utility.ContextManager;
import qkart.utility.ObjectContext;
import qkart.utility.Base;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

public class TestsA extends Base {

    @Test(enabled = true, description = "Verify a new user can successfully register", priority = 1, groups = {"Sanity_test"})
    @Parameters({"usernameA", "password"})
    public void TestCase01(String username, String password) {

        ObjectContext oc = ContextManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        // Visit the Registration page and register a new user
        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");

        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        // Visit the login page and login with the registered user
        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");
        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        // Visit the home page and log out the logged-in user
        assertTrue(oc.homePage.performLogout(), "User is able to logout after login from home page");
    }

    @Test(enabled = true, description = "Verify re-registering an already registered user fails", priority = 2, groups = {"Sanity_test", "T2"})
    @Parameters({"usernameA", "password"})
    public void TestCase02(String username, String password) {

        ObjectContext oc = ContextManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        // Visit the Registration page and register a new user
        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");


        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");
        assertTrue(oc.registration.verifyOnRegisterPage(), "User is on registration page");
        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        assertFalse(oc.registration.registerUser(usernameDynamic, password, password), "User is not able to re-registering an already registered user");
    }

    @Test(enabled = true, description = "Verify the search box functionality ", priority = 3, groups = {"Sanity_test", "T3"})
    public void TestCase03() {

        ObjectContext oc = ContextManager.getContext();

        // Visit the home page
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        String productName = "yonex";
        // Search for the "yonex" product
        assertTrue(oc.homePage.searchForProduct(productName), "User is able to search for " + productName + " product");

        // Fetch the search results
        List<WebElement> searchResults = oc.homePage.getSearchResults();

        assertTrue(!searchResults.isEmpty(), "There were results for the " + productName + " product");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultElement = new SearchResult(oc.getDriver(), webElement);

            // Verify that all results contain the searched text
            String elementText = resultElement.getTitleOfResult();

            assertTrue(elementText.toLowerCase().contains(productName.toLowerCase()), "Test Results contains expected values");
        }

        productName = "Gesundheit";
        // Search for product
        assertTrue(oc.homePage.searchForProduct(productName), "User is able to search for " + productName);

        // Verify no search results are found
        searchResults = oc.homePage.getSearchResults();
        assertTrue(searchResults.isEmpty(), "Result not found for invalid product name " + productName);

        assertTrue(oc.homePage.isNoResultFound(), "\"No product found\" message is displayed for the invalid product name " + productName);
    }

    @Test(enabled = true, description = "Verify the existence of size chart for certain items and validate contents of size chart", priority = 4, groups = "Regression_Test")
    public void TestCase04() {
        ObjectContext oc = ContextManager.getContext();

        // Visit home page
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        String productName = "Running Shoes";
        // Search for product and get card content element of search results
        assertTrue(oc.homePage.searchForProduct(productName), "User is able to search for " + productName);

        List<WebElement> searchResults = oc.homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(oc.getDriver(), webElement);

            assertTrue(result.verifySizeChartExists(), "Size Chart button exist for " + productName);
            assertTrue(result.verifyExistenceOfSizeDropdown(), "Size drop down exist for " + productName);
            assertTrue(result.openSizeChart(), "User is able to open size chart for " + productName);
            assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody), "Actual and Expected size chart values match for " + productName);
            assertTrue(result.closeSizeChart(), "User is able to close size chart for " + productName);
        }
    }

    @Test(enabled = true, description = "Verify that a new user can add multiple products in to the cart and checkout", priority = 5, groups = {"Sanity_test"})
    @Parameters({"usernameA", "password", "productName1", "productName2", "address"})
    public void TestCase05(String username, String password, String productName1, String productName2, String address) {
        ObjectContext oc = ContextManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        // Go to the Register page
        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");

        // Register a new user
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        // Go to the login page
        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");

        // Login with the newly registered user's credentials
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");
        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");

        // Go to the home page
        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");

        // Find required products by searching and add them to the user's cart
        assertTrue(oc.homePage.searchForProduct(productName1), "User is able to search for " + productName1);
        assertTrue(oc.homePage.addProductToCart(productName1), "User is able to add product in cart for " + productName1);
        assertTrue(oc.homePage.searchForProduct(productName2), "User is able to search for " + productName2);
        assertTrue(oc.homePage.addProductToCart(productName2), "User is able to add product in cart for " + productName2);

        // Click on the checkout button
        assertTrue(oc.homePage.clickCheckout(), "User is able to click checkout button");

        // Add a new address on the Checkout page and select it
        assertTrue(oc.checkoutPage.addNewAddress(address), "User is able to add address " + address);
        assertTrue(oc.checkoutPage.selectAddress(address), "User is able to select address " + address);

        // Place the order
        assertTrue(oc.checkoutPage.clickPlaceOrderBtn(), "User is able to click place order button for selected address " + address);

        assertTrue(oc.thanks.verifyOnThanksPage(), "User is redirected to thanks page after click place order button");

        // Go to the home page
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        // Log out the user
        assertTrue(oc.homePage.performLogout(), "User is able to perform logout from the home page");
    }

    @Test(enabled = true, description = " Verify the quantity of items in cart can be updated", priority = 6, groups = {"Regression_Test"})
    @Parameters({"usernameA", "password", "productName3", "productName4", "address"})
    public void TestCase06(String username, String password, String productName3, String productName4, String address) {
        ObjectContext oc = ContextManager.getContext();

        // Generated dynamic username
        String usernameDynamic = oc.cm.generateDynamicUserName(username);

        assertTrue(oc.registration.navigateToRegisterPage(), "User is able to navigate to register page");

        // Register a new user
        assertTrue(oc.registration.registerUser(usernameDynamic, password, password), "User is able to register with new username and password");

        assertTrue(oc.login.verifyOnLoginPage(), "User is redirected to login page");
        assertTrue(oc.login.performLogin(usernameDynamic, password), "User is able to login with registered user credentials");
        assertTrue(oc.login.verifyUserLoggedIn(usernameDynamic), "Login successful and user is logged in");
        assertTrue(oc.homePage.verifyOnHomePage(), "User is redirected to home page");
        assertTrue(oc.homePage.searchForProduct(productName3), "User is able to search for " + productName3);
        assertTrue(oc.homePage.addProductToCart(productName3), "User is able to add product in cart for " + productName3);
        assertTrue(oc.homePage.searchForProduct(productName4), "User is able to search for " + productName4);
        assertTrue(oc.homePage.addProductToCart(productName4), "User is able to add product in cart for " + productName4);

        // update productName3 quantity to 2
        assertTrue(oc.homePage.changeProductQuantityInCart(productName3, 5), "User is able to change the product quantity for " + productName3);

        // update table lamp quantity to 0
        assertTrue(oc.homePage.changeProductQuantityInCart(productName4, 0), "User is able to change the product quantity for " + productName4);

        // update watch quantity again to 1
        assertTrue(oc.homePage.changeProductQuantityInCart(productName3, 1), "User is able to change the product quantity for " + productName3);

        assertTrue(oc.homePage.clickCheckout(), "User is able to click checkout button");

        assertTrue(oc.checkoutPage.addNewAddress(address), "User is able to add address " + address);
        assertTrue(oc.checkoutPage.selectAddress(address), "User is able to select address " + address);
        assertTrue(oc.checkoutPage.clickPlaceOrderBtn(), "User is able to click place order button for selected address " + address);

        assertTrue(oc.thanks.verifyOnThanksPage(), "User is redirected to thanks page after click place order button");

        // Go to the home page
        assertTrue(oc.homePage.navigateToHome(), "User is able to navigate to home page");

        // Log out the user
        assertTrue(oc.homePage.performLogout(), "User is able to perform logout from the home page");
    }
}