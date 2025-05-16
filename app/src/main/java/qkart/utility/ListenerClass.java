package qkart.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass extends Base implements ITestListener {
    Logger log = LogManager.getLogger(getClass());

//    protected ExtentSparkReporter sparkReporter;
//    protected ExtentReports reports;
//    protected ExtentTest test;

    public void onTestStart(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getMethod().getMethodName();
//        Screenshot.takeScreenshot(className, methodName, "onTestStart", "Starting");
    }

    public void onTestSuccess(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getMethod().getMethodName();
//        Screenshot.takeScreenshot(className, methodName, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result) {
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        Screenshot.takeScreenshot(className, methodName, "onTestFailure", "Failed");

        logResult(result, "FAILED");
    }

    public void onTestSkipped(ITestResult result) {
        logResult(result, "SKIPPED");
        // throw new UnsupportedOperationException("Unimplemented method 'onTestSkipped'");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestFailedButWithinSuccessPercentage'");
    }

    public void onStart(ITestContext context) {
        log.info("##### Test Cases Started #####");

        // throw new UnsupportedOperationException("Unimplemented method 'onStart'");
        Screenshot.createScreenshotFolder();

//        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/extent reports/report.html");
//
//        sparkReporter.config().setDocumentTitle("Automation Report");
//        sparkReporter.config().setReportName("Functional Testing");
//        sparkReporter.config().setTheme(Theme.DARK); //OR Theme.STANDARD
//
//        reports = new ExtentReports();
//
//        reports.attachReporter(sparkReporter);
//
//        reports.setSystemInfo("Computer Name", "Lakesh-Laptop");
//        reports.setSystemInfo("Environment Name", "Production");
//        reports.setSystemInfo("Tester Name", "Lakesh Sahu");
//        reports.setSystemInfo("OS", "Windows 11 Home Edition");
//        reports.setSystemInfo("Browser", "Chrome");
//
//        test = reports.createTest("QKart Automation Test");
    }

    public void onFinish(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onFinish'");
        log.info("##### Test Cases Ended #####");
    }

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
}