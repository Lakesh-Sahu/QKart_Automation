package QKART_SANITY_LOGIN.Module4;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.time.Duration;
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

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

public class QkartSanityV2 {

    // Creating static variable for storing the last generated user name
    public static String lastGeneratedUserName;

    // To create WebDriver Object
    public static WebDriver createDriver() throws MalformedURLException {
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    // To print log message
    public static void logStatus(String type, String message, String status) {
        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    // To take screentshot of WebPage
    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            // Creating File object
            File theDir = new File("/screenshots");

            // If the given directory doesn't exist then create a new directory
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            // Storing the time
            String timestamp = String.valueOf(java.time.LocalDateTime.now());

            // Replacing all the ":" with "-" so that it can be vaild according to windows
            // file storing formate
            timestamp = timestamp.replaceAll(":", "-");

            // Storing the file name
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);

            // Casting the driver to TakesScreenshot interface and result is stored in the
            // scrShot variable
            TakesScreenshot scrShot = ((TakesScreenshot) driver);

            // Taking the screenshot and storing it as a file
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

            // File object is created with argument
            File DestFile = new File("screenshots/" + fileName);

            // Copying the content from the source file to the destination file
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {

            // Printing the exception
            e.printStackTrace();
        }
    }

    // TestCase01: Verify the functionality of Login button on the Home page
    public static Boolean TestCase01(WebDriver driver) throws InterruptedException {

        // Creating a boolean variable
        Boolean status;

        // Calling the logStatus method
        logStatus("Start TestCase", "TestCase01: Verify User Registration", "DONE");

        // Naviagte to the Registration page and register a new user
        // Create object of Register class
        Register registration = new Register(driver);

        // Open the registration page
        registration.navigateToRegisterPage();

        // Register the user with dynamic and store the result
        status = registration.registerUser("testUser", "abc@123", true);

        // Check if registration fails
        if (!status) {
            logStatus("TestCase01", "TestCase Fail. User Registration Fail", "FAIL");
            logStatus("End TestCase", "TestCase01: Verify user Registration : ", status ? "PASS" : "FAIL");

            // Return false as the test case Fails
            return false;
        } else {
            logStatus("TestCase01", "TestCase Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Create object of login class
        Login login = new Login(driver);

        // Navigate to login page
        login.navigateToLoginPage();

        // Perform login
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        // Log the status of login
        logStatus("TestStep", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "TestCase01: Verify user Registration : ", status ? "PASS" : "FAIL");
            return false;
        }

        // Create object of Home class
        Home home = new Home(driver);

        // Perform logout
        status = home.PerformLogout();
        logStatus("End TestCase", "TestCase01: Verify user Registration : ", status ? "PASS" : "FAIL");

        return status;
    }

    // TestCase02: Verify that an existing user is not allowed to re-register on
    // QKart
    public static Boolean TestCase02(WebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start Testcase", "TestCase02: Verify User Registration with an existing username ", "DONE");

        // Navigate to registration page and registrating with new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        // Logging the status of registration
        logStatus("TestStep", "User Registration : ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "TestCase02: Verify user Registration : ", status ? "PASS" : "FAIL");
            return false;

        }

        // Get the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Navigate to the Registration page and try to register using the
        // previously registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has failed.
        // In this case registration failure means Success
        logStatus("End TestCase", "TestCase02: Verify user Registration : ", status ? "FAIL" : "PASS");
        return !status;
    }

    // TestCase03: Verify the functinality of the search text box
    public static Boolean TestCase03(WebDriver driver) throws InterruptedException {
        logStatus("Start TestCase", "TestCase03: Verify functionality of search box ", "DONE");
        boolean status;

        // Navigate to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");
        if (!status) {
            logStatus("TestCase03", "TestCase Failure. Unable to search for given product", "FAIL");
            return false;
        }

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase03", "TestCase Failure. There were no results for the given search string", "FAIL");
            return false;
        }

        // Iterating thorugh all card content of search result
        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase03", "TestCase Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
                return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        if (status) {
            logStatus("TestCase03", "TestCase Pass. Search Successful", "Pass");
        }

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success", "Successfully validated that no products found message is displayed", "PASS");
            }
            logStatus("TestCase03", "TestCase PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase03", "TestCase Fail. Expected: no results , actual: Results were available", "FAIL");
            return false;
        }

        return true;
    }

    // TestCase04: Verify the presence of size chart and check if the size chart
    // content is as expected
    public static Boolean TestCase04(WebDriver driver) throws InterruptedException {
        logStatus("Start TestCase", "TestCase04: Verify the presence of size Chart", "DONE");
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

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link", "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        logStatus("Step Success", "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure", "Failure while validating contents of Size Chart Link", "FAIL");
                        status = false;
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase04", "TestCase Fail. Failure to open Size Chart", "FAIL");
                    return false;
                }

            } else {
                logStatus("TestCase04", "TestCase Fail. Size Chart Link does not exist", "FAIL");
                return false;
            }
        }
        logStatus("TestCase04", "End TestCase: Validated Size Chart Details", status ? "PASS" : "FAIL");
        return status;
    }

    // TestCase05: Verify the complete flow of checking out and placing order for
    // products is
    // working correctly
    public static Boolean TestCase05(WebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "TestCase05: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase05", "TestCase Failure. Happy Flow Test Failed", "FAIL");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "TestCase05: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

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

        logStatus("End TestCase", "TestCase05: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        return status;
    }

    // TestCase06: Verify the quantity of items in cart can be updated
    public static Boolean TestCase06(WebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "TestCase06: Verify that cart can be edited", "DONE");

        // Navigate to registration page, register and login
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Register Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "TestCase06:  Verify that cart can be edited: ", status ? "PASS" : "FAIL");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "TestCase06:  Verify that cart can be edited: ", status ? "PASS" : "FAIL");
            return false;
        }

        homePage.navigateToHome();

        // Search for products and add to cart
        status = homePage.searchForProduct("Xtend");
        homePage.addProductToCart("Xtend Smart Watch");

        status = homePage.searchForProduct("Yarine");
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        // Click checkout button
        homePage.clickCheckout();

        // Add address and select address in the checkout page
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Click place order
        checkoutPage.placeOrder();

        // Wait for the current url to be on "thanks" page
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            status = wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());
            return false;
        }

        // Naviagte to home page and logout
        homePage.navigateToHome();
        homePage.PerformLogout();

        logStatus("End TestCase", "TestCase06: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        return status;
    }

    // TestCase07: Verify that insufficient balance error is thrown when the wallet
    // balance is not enough
    public static Boolean TestCase07(WebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase",
                "TestCase07: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        // Navigate to registration page, register and login
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "TestCase07: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "TestCase07: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
            return false;
        }

        // Navigate to home page, search for product, add the product to cart, increase
        // the quantity of product, click checkout button
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");
        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 10);
        homePage.clickCheckout();

        // Enter address, select address, place order
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        // Verify insufficient balance error is shown
        status = checkoutPage.verifyInsufficientBalanceMessage();

        logStatus("End TestCase",
                "TestCase07: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");

        return status;
    }

    public static Boolean TestCase08(WebDriver driver) throws InterruptedException {
        Boolean status = false;

        logStatus("Start TestCase",
                "TestCase08: Verify that product added to cart is available when a new tab is opened",
                "DONE");
        takeScreenshot(driver, "Start TestCase", "TestCase09");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase08",
                    "TestCase Failure. Verify that product added to cart is available when a new tab is opened",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase9");
            logStatus("End TestCase",
                    "TestCase08:   Verify that product added to cart is available when a new tab is opened",
                    status ? "PASS" : "FAIL");
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        logStatus("End TestCase",
                "TestCase08: Verify that product added to cart is available when a new tab is opened",
                status ? "PASS" : "FAIL");
        takeScreenshot(driver, "End TestCase", "TestCase08");

        return status;
    }

    public static Boolean TestCase09(WebDriver driver) throws InterruptedException {
        Boolean status = false;

        logStatus("Start TestCase",
                "TestCase09: Verify that the Privacy Policy, About Us are displayed correctly ",
                "DONE");
        takeScreenshot(driver, "Start TestCase", "TestCase09");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase09",
                    "TestCase Failure.  Verify that the Privacy Policy, About Us are displayed correctly ",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "TestCase09:    Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);

        if (!status) {
            logStatus("Step Failure", "Verifying parent page url didn't change on privacy policy link click failed",
                    status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "TestCase09: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        if (!status) {
            logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading failed",
                    status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "TestCase09: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        if (!status) {
            logStatus("Step Failure", "Verifying new tab opened has Terms Of Service page heading failed",
                    status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "TestCase09: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        logStatus("End TestCase",
                "TestCase09: Verify that the Privacy Policy, About Us are displayed correctly ",
                "PASS");
        takeScreenshot(driver, "End TestCase", "TestCase09");

        return status;
    }

    public static Boolean TestCase10(WebDriver driver) throws InterruptedException {
        logStatus("Start TestCase",
                "TestCase10: Verify that contact us option is working correctly ",
                "DONE");
        takeScreenshot(driver, "Start TestCase", "TestCase10");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

        logStatus("End TestCase",
                "TestCase10: Verify that contact us option is working correctly ",
                "PASS");

        takeScreenshot(driver, "End TestCase", "TestCase10");

        return true;
    }

    public static Boolean TestCase11(WebDriver driver) throws InterruptedException {
        Boolean status = false;
        logStatus("Start TestCase",
                "TestCase11: Ensure that the links on the QKART advertisement are clickable",
                "DONE");
        takeScreenshot(driver, "Start TestCase", "TestCase11");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase11",
                    "Test Case Failure. Ensure that the links on the QKART advertisement are clickable",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase11");
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase11");
            logStatus("End TestCase",
                    "TestCase11:  Ensure that the links on the QKART advertisement are clickable",
                    status ? "PASS" : "FAIL");
        }

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
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step ", "Verify that Advertisement 2 is clickable ", status ? "PASS" : "FAIL");

        logStatus("End TestCase",
                "TestCase11:  Ensure that the links on the QKART advertisement are clickable",
                status ? "PASS" : "FAIL");
        return status;
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        int totalTests = 0;
        int passedTests = 0;
        Boolean status;
        WebDriver driver = createDriver();

        // Maximizing the browser window
        driver.manage().window().maximize();

        // Applying implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // Execute TestCase01
            totalTests += 1;
            status = TestCase01(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase02
            totalTests += 1;
            status = TestCase02(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase03
            totalTests += 1;
            status = TestCase03(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase04
            totalTests += 1;
            status = TestCase04(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase05
            totalTests += 1;
            status = TestCase05(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase06
            totalTests += 1;
            status = TestCase06(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase07
            totalTests += 1;
            status = TestCase07(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase08
            totalTests += 1;
            status = TestCase08(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase09
            totalTests += 1;
            status = TestCase09(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase10
            totalTests += 1;
            status = TestCase10(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute TestCase11
            totalTests += 1;
            status = TestCase11(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

        } catch (Exception e) {
            throw e;
        } finally {
            // quit Chrome Driver
            driver.quit();

            System.out.println(String.format("%s out of %s test cases Passed ", Integer.toString(passedTests),
                    Integer.toString(totalTests)));
        }

    }
}