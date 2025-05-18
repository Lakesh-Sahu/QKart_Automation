package qkart.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.io.File;

public class Base {

    protected Logger log = LogManager.getLogger(getClass());
    protected static String browserName;

    protected static ExtentSparkReporter sparkReporter;
    protected static ExtentReports reports;

    static String singleTimeStamp;

    @BeforeSuite(alwaysRun = true)
    @Parameters("browser")
    public void getBrowserName(String browser) {
        browserName = browser;
    }

    @BeforeSuite(alwaysRun = true, dependsOnMethods = "getBrowserName")
    public void extentReportInitializer() {
        singleTimeStamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll("[.:]", "");

        String filePath = System.getProperty("user.dir") + "/extent_reports/report_" + singleTimeStamp + ".html";
        File file = new File(filePath);
        if (!file.exists()) {
            sparkReporter = new ExtentSparkReporter(filePath);

            sparkReporter.config().setDocumentTitle("QKart Automation Report");
            sparkReporter.config().setReportName("Functional Testing");
            sparkReporter.config().setTheme(Theme.DARK); //OR Theme.STANDARD

            reports = new ExtentReports();

            reports.attachReporter(sparkReporter);

            reports.setSystemInfo("Computer Name", "Lakesh-Laptop");
            reports.setSystemInfo("Environment Name", "Production");
            reports.setSystemInfo("Tester Name", "Lakesh Sahu");
            reports.setSystemInfo("OS", "Windows 11 Home Edition");
            reports.setSystemInfo("Browser", browserName);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (ContextManager.getContext() != null && ContextManager.getContext().getDriver() != null) {
            ContextManager.getContext().getDriver().quit();
            ContextManager.remove();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        reports.flush();
    }


    // Used to get the class, method name, and line number from which any other method is called, used for logging
    public static String getCallerInfo(StackTraceElement[] stackTraceArr) {
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

    // Used to get the class, method name, and line number from which any other method is called, used for logging
    // Index 0 = getStackTrace, 1 = logCallerInfo, 2 = actual caller
    public static String getCallerInfo(StackTraceElement[] stackTraceArr, int index) {
        try {
            StackTraceElement caller = stackTraceArr[index];

            String callerClassName = caller.getClassName();
            String callerMethodName = caller.getMethodName();
            int callerLine = caller.getLineNumber();

            return (callerClassName + " " + callerMethodName + " " + callerLine);
        } catch (Exception e) {
            return "";
        }
    }

    public static void logWarningInExtentReport(Exception e, String message) {
        ExtentTest test = ContextManager.getContext().test;

        StackTraceElement stackTraceElementCurrent = e.getStackTrace()[2];
        StackTraceElement stackTraceElementParent = e.getStackTrace()[3];
        String callerInfo = stackTraceElementCurrent.getClassName() + " " + stackTraceElementCurrent.getMethodName() + " " + stackTraceElementCurrent.getLineNumber() + " " + stackTraceElementParent.getClassName() + " " + stackTraceElementParent.getMethodName() + " " + stackTraceElementParent.getLineNumber();

        ContextManager.getContext().test.warning(callerInfo + " " + message + " : " + getMessageFromException(e), MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());
    }

    public static void logWarningInExtentReport(Exception e, String message, int index) {
        ExtentTest test = ContextManager.getContext().test;

        StackTraceElement stackTraceElementCurrent = e.getStackTrace()[index];
        StackTraceElement stackTraceElementParent = e.getStackTrace()[index + 1];
        String callerInfo = stackTraceElementCurrent.getClassName() + " " + stackTraceElementCurrent.getMethodName() + " " + stackTraceElementCurrent.getLineNumber() + " " + stackTraceElementParent.getClassName() + " " + stackTraceElementParent.getMethodName() + " " + stackTraceElementParent.getLineNumber();

        ContextManager.getContext().test.warning(callerInfo + " " + message + " : " + getMessageFromException(e), MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());
    }

    public static String getMessageFromException(Exception e) {
        try {
            return e.getMessage().split("\n")[0];
        } catch (Exception i) {
            return "";
        }
    }
}