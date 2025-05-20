package qkart.utility;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import qkart.pages.*;

// This class is used to create the Object of classes per thread for parallel execution
public class ObjectContext extends Base{
    private final WebDriver driver;
    private final String className;
    private final String methodName;

    public Register registration;
    public Login login;
    public Home homePage;
    public Checkout checkoutPage;
    public PrivacyPolicy privacyPolicyPage;
    public TermsOfService termsOfService;
    public AboutUs aboutUs;
    public Thanks thanks;
    public CommonMethods cm;
    public ExtentTest test;

    public ObjectContext(WebDriver driver, String className, String methodName, String description) {
        this.driver = driver;
        this.className = className;
        this.methodName = methodName;

        registration = new Register(driver);
        login = new Login(driver);
        homePage = new Home(driver);
        checkoutPage = new Checkout(driver);
        privacyPolicyPage = new PrivacyPolicy(driver);
        termsOfService = new TermsOfService(driver);
        aboutUs = new AboutUs(driver);
        thanks = new Thanks(driver);
        cm = new CommonMethods(driver);

        test = reports.createTest(className + " " + methodName + " " + description);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
