package qkart.utility;

import org.openqa.selenium.WebDriver;
import qkart.pages.*;

public class ObjectContext {
    private final WebDriver driver;

    public Register registration;
    public Login login;
    public Home homePage;
    public Checkout checkoutPage;
    public PrivacyPolicy privacyPolicyPage;
    public TermsOfService termsOfService;
    public Thanks thanks;
    public CommonMethods cm;

    public ObjectContext(WebDriver driver) {
        this.driver = driver;

        registration = new Register(driver);
        login = new Login(driver);
        homePage = new Home(driver);
        checkoutPage = new Checkout(driver);
        thanks = new Thanks(driver);
        privacyPolicyPage = new PrivacyPolicy(driver);
        termsOfService = new TermsOfService(driver);
        cm = new CommonMethods(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
