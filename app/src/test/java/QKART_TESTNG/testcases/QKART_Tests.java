package QKART_TESTNG.testcases;

import static org.testng.Assert.*;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

public class QKART_Tests {

        static WebDriver driver;
        public static String lastGeneratedUserName;
        static int i;

        @BeforeSuite(alwaysRun = true)
        public static void createDriver() {
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                System.out.println("createDriver()");
        }

        // Testcase01: Verify a new user can successfully register
        @Test(enabled = true, description = "Verify registration happens correctly", priority = 1, groups = { "Sanity_test" })
        @Parameters({ "username", "password" })
        public void TestCase01(String username, String password) throws InterruptedException {
                Boolean status;

                // Visit the Registration page and register a new user
                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);
                assertTrue(status, "Failed to register new user");

                // Save the last generated username
                lastGeneratedUserName = registration.lastGeneratedUsername;

                // Visit the login page and login with the previuosly registered user
                Login login = new Login(driver);
                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status, "Failed to login with registered user");

                // Visit the home page and log out the logged in user
                Home home = new Home(driver);
                status = home.PerformLogout();

                assertTrue(status, "Test Case 1: Verify user Registration : FAIL");
        }

        // Verify that an existing user is not allowed to re-register on QKart
        @Test(enabled = true, description = "Verify re-registering an already registered user fails", priority = 2, groups = {
                        "Sanity_test" })
        @Parameters({ "username", "password" })
        public void TestCase02(String username, String password) throws InterruptedException {
                Boolean status;

                // Visit the Registration page and register a new user
                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status, "Test Case 2: Verify user Registration : FAIL");

                // Save the last generated username
                lastGeneratedUserName = registration.lastGeneratedUsername;

                // Visit the Registration page and try to register using the previously
                // registered user's credentials
                registration.navigateToRegisterPage();
                status = registration.registerUser(lastGeneratedUserName, password, false);

                // If status is true, then registration succeeded, else registration has
                // failed. In this case registration failure means Success
                assertFalse(status, "Test Case 2: Verify user Registration : FAIL");
        }

        // Verify the functinality of the search text box
        @Test(enabled = true, description = "Verify re-registering an already registered user fails", priority = 3, groups = {
                        "Sanity_test" })
        @Parameters({ "username", "password" })
        public void TestCase03(String username, String password) throws InterruptedException {
                boolean status;

                // Visit the home page
                Home homePage = new Home(driver);
                homePage.navigateToHome();

                // Search for the "yonex" product
                status = homePage.searchForProduct("YONEX");

                assertTrue(status, "Test Case Failure. Unable to search for given product : FAIL");

                // Fetch the search results
                List<WebElement> searchResults = homePage.getSearchResults();

                assertFalse(searchResults.size() == 0,
                                "Test Case Failure. There were no results for the given search string : FAIL");

                for (WebElement webElement : searchResults) {
                        // Create a SearchResult object from the parent element
                        SearchResult resultelement = new SearchResult(webElement);

                        // Verify that all results contain the searched text
                        String elementText = resultelement.getTitleofResult();

                        assertTrue(elementText.toUpperCase().contains("YONEX"),
                                        "Test Case Failure. Test Results contains un-expected values: FAIL");
                }

                // Search for product
                status = homePage.searchForProduct("Gesundheit");

                assertTrue(status, "Test Case Failure. Unable to search for given product : FAIL");

                // Verify no search results are found
                searchResults = homePage.getSearchResults();
                assertEquals(searchResults.size(), 0,
                                "TestCase 3 : Test Case Fail. Expected: no results , actual: Results were available : FAIL");

                status = homePage.isNoResultFound();
                assertTrue(status,
                                "TestCase 3 : Test Case Fail. Expected: no results , actual: Results were available : FAIL");
        }

        // Verify the presence of size chart and check if the size chart content is as
        // expected
        @Test(enabled = true, description = "Verify the existence of size chart for certain items and validate contents of size chart", priority = 4, groups = {
                        "Regression_Test" })
        @Parameters({ "username", "password" })
        public void TestCase04(String username, String password) throws InterruptedException {

                boolean status = false;

                // Visit home page
                Home homePage = new Home(driver);
                homePage.navigateToHome();

                // Search for product and get card content element of search results
                status = homePage.searchForProduct("Running Shoes");
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

                        status = result.verifySizeChartExists();
                        assertTrue(status,
                                        "TestCase 4 : Test Case Fail. Size Chart Link does not exist : FAIL");

                        status = result.verifyExistenceofSizeDropdown(driver);
                        assertTrue(status, "TestCase 4: Validated presence of drop down : FAIL");

                        status = result.openSizechart();
                        assertTrue(status, "TestCase 4 : Test Case Fail. Failure to open Size Chart : FAIL");

                        status = result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
                                        driver);
                        assertTrue(status,
                                        "TestCase 4: Failure while validating contents of Size Chart Link : FAIL");

                        status = result.closeSizeChart(driver);
                        assertTrue(status, "TestCase 4: Failure to close size chart : FAIL");
                }

                assertTrue(true, "TestCase 4: End Test Case: Validated Size Chart Details : FAIL");
        }

        // Verify the complete flow of checking out and placing order for products is
        // working correctly
        @Test(enabled = true, description = "Verify that a new user can add multiple products in to the cart and Checkout", priority = 5, groups = {
                        "Sanity_test" })
        @Parameters({ "username", "password", "product1", "product2", "address" })
        public void TestCase05(String username, String password, String product1, String product2, String address)
                        throws InterruptedException {
                Boolean status;

                // Go to the Register page
                Register registration = new Register(driver);
                registration.navigateToRegisterPage();

                // Register a new user
                status = registration.registerUser(username, password, true);

                assertTrue(status, "TestCase 5 : Test Case Failure. Happy Flow Test Failed : FAIL");

                // Save the username of the newly registered user
                lastGeneratedUserName = registration.lastGeneratedUsername;

                // Go to the login page
                Login login = new Login(driver);
                login.navigateToLoginPage();

                // Login with the newly registered user's credentials
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status, "Test Case 5: Happy Flow Test Failed : FAIL");

                // Go to the home page
                Home homePage = new Home(driver);
                homePage.navigateToHome();

                // Find required products by searching and add them to the user's cart
                status = homePage.searchForProduct(product1);
                homePage.addProductToCart("YONEX Smash Badminton Racquet");
                status = homePage.searchForProduct(product2);
                homePage.addProductToCart("Tan Leatherette Weekender Duffle");

                // Click on the checkout button
                homePage.clickCheckout();

                // Add a new address on the Checkout page and select it
                Checkout checkoutPage = new Checkout(driver);
                checkoutPage.addNewAddress(address);
                checkoutPage.selectAddress(address);

                // Place the order
                checkoutPage.placeOrder();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

                // Check if placing order redirected to the Thansk page
                status = driver.getCurrentUrl().endsWith("/thanks");

                // Go to the home page
                homePage.navigateToHome();

                // Log out the user
                homePage.PerformLogout();

                assertTrue(status, "Test Case 5: Happy Flow Test Completed : FAIL");
        }

        // Verify the quantity of items in cart can be updated
        @Test(enabled = true, description = "Verify that the contents of the cart can be edited", priority = 6, groups = {
                        "Regression_Test" })
        @Parameters({ "username", "password", "product3", "product4", "address" })
        public void TestCase06(String username, String password, String product1, String product2, String address)
                        throws InterruptedException {
                Boolean status;

                Home homePage = new Home(driver);
                Register registration = new Register(driver);
                Login login = new Login(driver);

                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status, "Test Case 6:  Verify that cart can be edited : FAIL");
                lastGeneratedUserName = registration.lastGeneratedUsername;

                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status, "Test Case 6:  Verify that cart can be edited : FAIL");

                homePage.navigateToHome();
                status = homePage.searchForProduct(product1);
                homePage.addProductToCart("Xtend Smart Watch");

                status = homePage.searchForProduct(product2);
                homePage.addProductToCart("Yarine Floor Lamp");

                // update watch quantity to 2
                homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

                // update table lamp quantity to 0
                homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

                // update watch quantity again to 1
                homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

                homePage.clickCheckout();

                Checkout checkoutPage = new Checkout(driver);
                checkoutPage.addNewAddress(address);
                checkoutPage.selectAddress(address);

                checkoutPage.placeOrder();

                try {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                        wait.until(
                                        ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
                } catch (TimeoutException e) {

                        assertTrue(false, "Error while placing order in: " + e.getMessage());
                }

                status = driver.getCurrentUrl().endsWith("/thanks");

                homePage.navigateToHome();
                homePage.PerformLogout();

                assertTrue(status, "Test Case 6: Verify that cart can be edited : FAIL");
        }

        @Test(enabled = true, description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = {
                        "Sanity_test" })
        @Parameters({ "username", "password", "product5", "quantity", "address" })
        public void TestCase07(String username, String password, String product, int quantity, String address)
                        throws InterruptedException {
                Boolean status;

                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status,
                                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough : FAIL");
                lastGeneratedUserName = registration.lastGeneratedUsername;

                Login login = new Login(driver);
                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status,
                                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough : FAIL");

                Home homePage = new Home(driver);
                homePage.navigateToHome();
                status = homePage.searchForProduct(product);
                homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");

                homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", quantity);

                homePage.clickCheckout();

                Checkout checkoutPage = new Checkout(driver);
                checkoutPage.addNewAddress(address);
                checkoutPage.selectAddress(address);

                checkoutPage.placeOrder();
                Thread.sleep(3000);

                status = checkoutPage.verifyInsufficientBalanceMessage();

                assertTrue(status,
                                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough : FAIL");
        }

        @Test(enabled = true, description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = {
                        "Regression_Test" })
        @Parameters({ "username", "password" })
        public void TestCase08(String username, String password) throws InterruptedException {
                Boolean status = false;

                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status,
                                "TestCase 8: Test Case Failure. Verify that product added to cart is available when a new tab is opened : FAIL");
                lastGeneratedUserName = registration.lastGeneratedUsername;

                Login login = new Login(driver);
                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status,
                                "Test Case 8: Verify that product added to cart is available when a new tab is opened : FAIL");

                Home homePage = new Home(driver);
                homePage.navigateToHome();

                status = homePage.searchForProduct("YONEX");
                homePage.addProductToCart("YONEX Smash Badminton Racquet");

                String currentURL = driver.getCurrentUrl();

                driver.findElement(By.linkText("Privacy policy")).click();
                Set<String> handles = driver.getWindowHandles();
                driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

                driver.get(currentURL);
                Thread.sleep(5000);

                List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
                status = homePage.verifyCartContents(expectedResult);

                driver.close();

                driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

                assertTrue(status,
                                "Test Case 8: Verify that product added to cart is available when a new tab is opened : FAIL");
        }

        @Test(enabled = true, description = "Verify that privacy policy and about us links are working fine", priority = 9, groups = {
                        "Regression_Test" })
        @Parameters({ "username", "password" })
        public void TestCase09(String username, String password) throws InterruptedException {
                Boolean status = false;

                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status,
                                "TestCase 09: Test Case Failure.  Verify that the Privacy Policy, About Us are displayed correctly : FAIL");
                lastGeneratedUserName = registration.lastGeneratedUsername;

                Login login = new Login(driver);
                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status,
                                "Test Case 9:    Verify that the Privacy Policy, About Us are displayed correctly : FAIL");

                Home homePage = new Home(driver);
                homePage.navigateToHome();

                String basePageURL = driver.getCurrentUrl();

                driver.findElement(By.linkText("Privacy policy")).click();
                status = driver.getCurrentUrl().equals(basePageURL);

                assertTrue(status,
                                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly : FAIL");

                Set<String> handles = driver.getWindowHandles();
                Thread.sleep(1500);
                driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
                WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/h2[text()='Privacy Policy']"));
                status = PrivacyPolicyHeading.getText().equals("Privacy Policy");

                SoftAssert sa = new SoftAssert();
                sa.assertTrue(status,
                                "Step Failure : Verifying new tab opened has Privacy Policy page heading failed : FAIL");

                driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
                driver.findElement(By.linkText("Terms of Service")).click();

                handles = driver.getWindowHandles();
                driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
                WebElement TOSHeading = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/h2"));
                status = TOSHeading.getText().equals("Terms of Service");

                sa.assertTrue(status,
                                "Step Failure : Verifying new tab opened has Terms Of Service page heading failed : FAIL");
                driver.close();
                driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
                driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

                sa.assertAll();
        }

        @Test(enabled = true, description = "Verify that the contact us dialog works fine", priority = 10, groups = {
                        "Regression_Test" })
        @Parameters({ "username", "password" })
        public void TestCase10(String username, String password) throws InterruptedException {

                Home homePage = new Home(driver);
                homePage.navigateToHome();

                driver.findElement(By.xpath("//*[text()='Contact us']")).click();

                WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
                name.sendKeys("crio user");
                WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
                email.sendKeys("criouser@gmail.com");
                WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
                message.sendKeys("Testing the contact us page");

                WebElement contactUs = driver.findElement(By.xpath(
                                "/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

                contactUs.click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.invisibilityOf(contactUs));

                assertTrue(true,
                                "Test Case 10: Verify that contact us option is not working correctly : FAIL");
        }

        @Test(enabled = true, description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = {
                        "Sanity_test" })
        @Parameters({ "username", "password" })
        public void TestCase11(String username, String password) throws InterruptedException {
                Boolean status = false;

                Register registration = new Register(driver);
                registration.navigateToRegisterPage();
                status = registration.registerUser(username, password, true);

                assertTrue(status,
                                "TestCase 11: Test Case Failure. Ensure that the links on the QKART advertisement are clickable : FAIL");
                lastGeneratedUserName = registration.lastGeneratedUsername;

                Login login = new Login(driver);
                login.navigateToLoginPage();
                status = login.PerformLogin(lastGeneratedUserName, password);

                assertTrue(status,
                                "Test Case 11:  Ensure that the links on the QKART advertisement are clickable : FAIL");

                Home homePage = new Home(driver);
                homePage.navigateToHome();

                status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
                homePage.addProductToCart("YONEX Smash Badminton Racquet");
                homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
                homePage.clickCheckout();

                Checkout checkoutPage = new Checkout(driver);
                checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
                checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
                checkoutPage.placeOrder();
                Thread.sleep(5000);

                String currentURL = driver.getCurrentUrl();

                List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

                status = Advertisements.size() == 3;

                assertTrue(status, "Step Failure: Verify that 3 Advertisements are available : FAIL");

                WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[1]"));
                driver.switchTo().frame(Advertisement1);
                driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
                driver.switchTo().parentFrame();

                status = !driver.getCurrentUrl().equals(currentURL);

                assertTrue(status, "Step Failure: Verify that Advertisement 1 is clickable : FAIL");

                driver.get(currentURL);
                Thread.sleep(3000);

                WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[2]"));
                driver.switchTo().frame(Advertisement2);
                driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
                driver.switchTo().parentFrame();

                status = !driver.getCurrentUrl().equals(currentURL);

                assertTrue(status,
                                "Test Case 11:  Ensure that the links on the QKART advertisement are clickable : FAIL");
        }

        @AfterSuite
        public static void quitDriver() {
                System.out.println("quit()");
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
                        e.printStackTrace();
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
                        e.printStackTrace();
                }
        }
}