package qkart.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.io.File;
import java.util.Arrays;

public class Base {

    protected static Logger log = LogManager.getLogger(Base.class);
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
        try {
            if (ContextManager.getContext() != null && ContextManager.getContext().getDriver() != null) {
                ContextManager.getContext().getDriver().quit();
                ContextManager.remove();
            }
        } catch (Exception e) {
            logWarningInExtentReport(e, "Unable to tearDown");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Unable to tearDown", e, Level.WARN);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        try {
            reports.flush();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Unable to flush the extent report");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Unable to flush the extent report", e, Level.WARN);
        }
    }

    // Used to get the class, method name, and line number from which any other method is called, used for logging
    public static String getCallerInfoFromStackTrace(StackTraceElement[] stackTraceArr) {
        try {
            // Index 0 = getStackTrace, 1 = logCallerInfo, 2 = actual caller
            StackTraceElement caller = stackTraceArr[2];

            String callerClassName = caller.getClassName();
            String callerMethodName = caller.getMethodName();
            int callerLine = caller.getLineNumber();

            return (callerClassName + " " + callerMethodName + " " + callerLine);
        } catch (Exception e) {
            logWarningInExtentReport(e, "Unable to get the caller info from StackTraceElement[]");
            logExceptionInLog(Arrays.toString(e.getStackTrace()), "Unable to get the caller info from StackTraceElement[]", e, Level.WARN);
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
            logWarningInExtentReport(e, "Unable to get the caller info from StackTraceElement[]");
            logExceptionInLog(Arrays.toString(e.getStackTrace()), "Unable to tearDown", e, Level.WARN);
            return "";
        }
    }

    public static void logWarningInExtentReport(Exception e, String message) {
        try {
            ObjectContext oc = ContextManager.getContext();
            String className = oc.getClassName();
            StringBuilder sb = new StringBuilder();
            StackTraceElement parent = null;

            for (StackTraceElement current : e.getStackTrace()) {
                if (current.getClassName().equals(className)) {
                    sb.append(current.getClassName()).append(" ").append(current.getMethodName()).append(" ").append(current.getLineNumber());
                    break;
                }
                parent = current;
            }
            if (parent != null) {
                sb.insert(0, " ").insert(0, parent.getLineNumber()).insert(0, " ").insert(0, parent.getMethodName()).insert(0, " ").insert(0, parent.getClassName());
            }
            String callerInfo = sb.toString();
            sb.append(" ").append(message).append(" : ").append(getMessageFromException(e)).append(" - WARNING");
            ContextManager.getContext().test.warning(sb.toString(), MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo + " - WARN")).build());

        } catch (Exception ei) {
            String callerInfo = getCallerInfoFromStackTrace(ei.getStackTrace());
            ContextManager.getContext().test.warning("Unable to log warning in extent report", MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo + " - WARN")).build());
            logExceptionInLog(Arrays.toString(e.getStackTrace()), "Unable to log warning in extent report", e, Level.WARN);
        }
    }
//    public static void logWarningInExtentReport(Exception e, String message, int index) {
//        StackTraceElement stackTraceElementCurrent = e.getStackTrace()[index];
//        StackTraceElement stackTraceElementParent = e.getStackTrace()[index + 1];
//        String callerInfo = stackTraceElementCurrent.getClassName() + " " + stackTraceElementCurrent.getMethodName() + " " + stackTraceElementCurrent.getLineNumber() + " " + stackTraceElementParent.getClassName() + " " + stackTraceElementParent.getMethodName() + " " + stackTraceElementParent.getLineNumber();
//
//        ContextManager.getContext().test.warning(callerInfo + " " + message + " : " + getMessageFromException(e), MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());
//    }

    public static String getMessageFromException(Throwable e) {
        try {
            return e.getMessage().split("\n")[0];
        } catch (Exception i) {
            return "";
        }
    }

    // log the result of test case if it is FAILED or SKIPPED
    public static void logExceptionInLog(String callerInfo, String message, Exception e, Level level) {
        try {
            StringBuilder sb = new StringBuilder();
            log.log(level, sb.append(callerInfo).append(" : ").append(message).append(" : ").append(getMessageFromException(e)).toString());
        } catch (Exception ei) {
            log.warn("Could not write test log for {} : {}", callerInfo, ei.getMessage().split("\n")[0]);
        }
    }
}