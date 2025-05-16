package qkart.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class Base {

    protected Logger log = LogManager.getLogger(getClass());

    protected ExtentSparkReporter sparkReporter;
    protected ExtentReports reports;
    protected static ExtentTest test;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(String browser) {
        WebDriver driver = (new DriverFactory()).getDriver(browser);
        ContextManager.init(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (ContextManager.getContext() != null && ContextManager.getContext().getDriver() != null) {
            ContextManager.getContext().getDriver().quit();
            ContextManager.remove();
        }
    }

    @BeforeSuite
    public void extentReportInitializer() {
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/extent reports/report.html");

        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK); //OR Theme.STANDARD

        reports = new ExtentReports();

        reports.attachReporter(sparkReporter);

        reports.setSystemInfo("Computer Name", "Lakesh-Laptop");
        reports.setSystemInfo("Environment Name", "Production");
        reports.setSystemInfo("Tester Name", "Lakesh Sahu");
        reports.setSystemInfo("OS", "Windows 11 Home Edition");
        reports.setSystemInfo("Browser", "Chrome");

        test = reports.createTest("QKart Automation Test");
    }


    // Used to get the class, method name, and line number from which any other method is called, used for logging
    public String logCallerInfo(StackTraceElement[] stackTraceArr) {
        try {
            // Index 0 = getStackTrace, 1 = logCallerInfo, 2 = actual caller
            StackTraceElement caller = stackTraceArr[2];

            String callerClassName = caller.getClassName();
            String callerMethodName = caller.getMethodName();
            int callerLine = caller.getLineNumber();

            return (callerClassName + " " + callerMethodName + " " + callerLine);
        } catch (Exception e) {
            return "";
        }
    }


}