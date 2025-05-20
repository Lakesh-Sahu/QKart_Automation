package qkart.utility;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listener extends Base implements ITestListener {
    Logger log = LogManager.getLogger(getClass());
    String browser;

    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getName();
        String methodName = result.getName();
        String description = result.getMethod().getDescription();

        WebDriver driver = (new DriverFactory()).getDriver(browser);
        ContextManager.init(driver, className, methodName, description);
    }

    public void onTestSuccess(ITestResult result) {
        String callerInfo = getCallerInfoFromITestResult(result, "PASSED");

        ContextManager.getContext().test.pass(callerInfo, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());
    }

    public void onTestFailure(ITestResult result) {
        String callerInfo = getCallerInfoFromITestResult(result, "FAILED");
        String throwableCallerInfoMessage = "Error occurred : " + getThrowableCallerInfoMessageFromITestResult(result, "FAILED");

        ContextManager.getContext().test.fail(throwableCallerInfoMessage, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());

        logResultInLog(result, "FAILED");
    }

    public void onTestSkipped(ITestResult result) {
        try {
            String callerInfo = getCallerInfoFromITestResult(result, "SKIPPED");
            String throwableCallerInfoMessage = "Error occurred : " + getThrowableCallerInfoMessageFromITestResult(result, "SKIPPED");

            ContextManager.getContext().test.skip(throwableCallerInfoMessage, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture(callerInfo)).build());

            logResultInLog(result, "SKIPPED");
        } catch (Exception e) {
            System.out.println("##### Unable to call the getCallerInfo for the extent report #####");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Exception while logging in the ExtentReport or in log file", e, Level.WARN);
        }
    }

    public void onStart(ITestContext context) {
        log.info("##### Test Cases Started #####");

        browser = browserName;
    }

    public void onFinish(ITestContext context) {
        log.info("##### Test Cases Ended #####");
    }

    // log the result of test case if it is FAILED or SKIPPED
    private void logResultInLog(ITestResult result, String status) {
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
            logWarningInExtentReport(e, "Could not write test result log in log");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Could not write test result log in log", e, Level.WARN);
        }
    }

    // log the result of test case if it is FAILED or SKIPPED
//    private void logResult(ITestResult result, Level level, String status) {
//        try {
//            Throwable throwable = result.getThrowable();
//            if (throwable != null) {
//                for (StackTraceElement element : throwable.getStackTrace()) {
//                    if (element.getClassName().equals(result.getTestClass().getName())) {
//                        int lineNumber = element.getLineNumber();
//                        String fileName = element.getFileName();
//                        String message = fileName + " " + result.getMethod().getMethodName() + " " + lineNumber + " - " + result.getThrowable().getMessage() + " - " + status;
//
//                        log.error(message);
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//
//        try {
//            String className = result.getTestClass().getName();
//            String methodName = result.getName();
//            StringBuilder sb = new StringBuilder(className + " " + methodName);
//            StackTraceElement parent = null;
//            Throwable th = result.getThrowable();
//            if (th != null) {
//                for (StackTraceElement current : th.getStackTrace()) {
//                    if (current.getClassName().equals(className)) {
//                        sb.append(" ").append(current.getLineNumber()).append(" ").append(" : ").append(getMessageFromException(th));
//                        break;
//                    }
//                    parent = current;
//                }
//                if (parent != null) {
//                    sb.insert(0, " ").insert(0, parent.getLineNumber()).insert(0, " ").insert(0, parent.getMethodName()).insert(0, " ").insert(0, parent.getClassName());
//                }
//            }
//            sb.append(" - ").append(status);
//
//            log.log(level, sb.toString());
//        } catch (Exception e) {
//              logWarningInExtentReport(e, "Could not write test log for " + result.getTestClass().getName() + " " +  result.getName());
//              logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()),"Could not write test log for " + result.getTestClass().getName() + " " +  result.getName(), e, Level.WARN);
//        }
//    }

    private String getCallerInfoFromITestResult(ITestResult result, String status) {
        try {
            Throwable throwable = result.getThrowable();
            String className = result.getTestClass().getName();
            String methodName = result.getName();

            if (throwable != null) {
                for (StackTraceElement element : throwable.getStackTrace()) {
                    if (element.getClassName().equals(result.getTestClass().getName())) {
                        int lineNumber = element.getLineNumber();
                        return className + " " + methodName + " " + lineNumber + " - " + status;
                    }
                }
            } else {
                return className + " " + methodName + " - " + status;
            }
        } catch (Exception e) {
            logWarningInExtentReport(e, "Could not get the caller information from the ITestResult");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Could not get the caller information from the ITestResult", e, Level.WARN);
        }
        return "";
    }

    private String getThrowableCallerInfoMessageFromITestResult(ITestResult result, String status) {
        try {
            String className = result.getTestClass().getName();
            String methodName = result.getName();

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                for (StackTraceElement element : throwable.getStackTrace()) {
                    if (element.getClassName().equals(result.getTestClass().getName())) {

                        String throwableClassName = throwable.getClass().getName();
                        int lineNumber = element.getLineNumber();
                        String throwableMessage = throwable.getMessage();

                        return throwableClassName + " : " + className + " " + methodName + " " + lineNumber + " - " + throwableMessage + " - " + status;
                    }
                }
            } else {
                return className + " " + methodName + " - " + status;
            }
        } catch (Exception e) {
            logWarningInExtentReport(e, "Could not get the caller information and throwable message from the ITestResult");
            logExceptionInLog(getCallerInfoFromStackTrace(Thread.currentThread().getStackTrace()), "Could not get the caller information and throwable message from the ITestResult", e, Level.WARN);
        }
        return "";
    }
}