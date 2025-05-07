package qkart.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import qkart.pages.*;
import qkart.utility.CommonMethods;
import qkart.utility.DriverFactory;
import qkart.utility.Setup;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class TestsB {

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

    @Test(enabled = true, description = "Verify re-registering an already registered user fails", priority = 2, groups = {"Sanity_test"})
    @Parameters({"usernameA", "password"})
    public void TestCase02(String username, String password) {
        // Generated dynamic username
        String usernameDynamic = cm.generateDynamicUserName(username);

        // Visit the Registration page and register a new user
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.registerUser(usernameDynamic, password), "User is unable to register with new username and password");


        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        assertTrue(registration.navigateToRegisterPage(), "User is unable to navigate to register page");
        assertTrue(registration.verifyOnRegisterPage(), "User is not on registration page");

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        assertFalse(registration.registerUser(usernameDynamic, password), "Test Case 02: Verify re-registering an already registered user fails : FAIL");
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
            SearchResult result = new SearchResult(driver, webElement);

            assertTrue(result.verifySizeChartExists(), "Size Chart button does not exist for " + productName);
            assertTrue(result.verifyExistenceOfSizeDropdown(), "Size drop down does not exist for " + productName);
            assertTrue(result.openSizeChart(), "User is unable to open size chart for " + productName);
            assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody), "Actual and Expected size chart values does not match for " + productName);
            assertTrue(result.closeSizeChart(), "User is unable to close size chart for " + productName);
        }
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

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}