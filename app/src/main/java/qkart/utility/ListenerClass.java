package qkart.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass extends Base implements ITestListener {
    Logger log = LogManager.getLogger(getClass());
    String browser;

    public void onTestStart(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getName();
//        Screenshot.takeScreenshot(className, methodName, "onTestStart", "Starting");

        String classAndMethodName = result.getTestClass().getName() + " " + result.getMethod().getMethodName() + " " + result.getMethod().getDescription();

        WebDriver driver = (new DriverFactory()).getDriver(browser);
        ContextManager.init(driver, classAndMethodName);
    }

    public void onTestSuccess(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getName();
//        Screenshot.takeScreenshot(className, methodName, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getName();
//        Screenshot.takeScreenshot(className, methodName, "onTestFailure", "Failed");

        Throwable throwable = result.getThrowable();

        if (throwable instanceof AssertionError) {
            String callerInfo = "Exception occurred : " + throwable.getClass().getName() + " " + getCallerInfoMessageFromITestResult(result, "FAILED");
            System.out.println("############## Assert " + callerInfo);
            ContextManager.getContext().test.fail(callerInfo);
        }

        if (throwable != null && !(throwable instanceof AssertionError)) {
            String callerInfo = "Exception occurred : " + throwable.getClass().getName() + getCallerInfoMessageFromITestResult(result, "FAILED");
            ContextManager.getContext().test.fail(callerInfo);
        }
        logResult(result, "FAILED");
    }

    public void onTestSkipped(ITestResult result) {
        try {
            String callerInfo = getCallerInfoMessageFromITestResult(result, "SKIPPED");
            ContextManager.getContext().test.skip(callerInfo);
            logResult(result, "SKIPPED");
        } catch (Exception e) {
            System.out.println("##### Unable to call the getCallerInfo for the extent report #####");
            log.error("{} Exception while logging in the ExtentReport or in log file : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
        }
    }

    public void onStart(ITestContext context) {
        log.info("##### Test Cases Started #####");

        browser = browserName;

//        Screenshot.createScreenshotFolder();
    }

    public void onFinish(ITestContext context) {
        log.info("##### Test Cases Ended #####");
    }

    // log the result of test case if it is FAILED or SKIPPED
    private void logResult(ITestResult result, String status) {
        try {
            Throwable throwable = result.getThrowable();

            if (throwable != null) {
                for (StackTraceElement element : throwable.getStackTrace()) {
                    if (element.getClassName().equals(result.getTestClass().getName())) {
                        int lineNumber = element.getLineNumber();
                        String fileName = element.getFileName();
                        String message = fileName + " " + result.getMethod().getMethodName() + " " + lineNumber + " - " + result.getThrowable().getMessage() + " - " + status;

                        log.error(message);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Could not write test result log ", e.getMessage().split("\n")[0]);
        }
    }

    // log the result of test case if it is FAILED or SKIPPED
    private String getCallerInfoMessageFromITestResult(ITestResult result, String status) {
        try {
            Throwable throwable = result.getThrowable();

            if (throwable != null) {
                for (StackTraceElement element : throwable.getStackTrace()) {
                    if (element.getClassName().equals(result.getTestClass().getName())) {
                        int lineNumber = element.getLineNumber();
                        String fileName = element.getFileName();
                        return fileName + " " + result.getMethod().getMethodName() + " " + lineNumber + " - " + result.getThrowable().getMessage() + " - " + status;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Could not get the caller information and message from the ITestResult ", e.getMessage().split("\n")[0]);
        }
        return "";
    }
}