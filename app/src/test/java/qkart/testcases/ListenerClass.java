package qkart.testcases;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    public void onTestStart(ITestResult result) {
//        QKartTests.takeScreenshot( QKartTests.driver, "onTestStart", "Starting");
    }

    public void onTestSuccess(ITestResult result){
//        QKartTests.takeScreenshot( QKartTests.driver, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result){
        QKartTests.takeScreenshot( QKartTests.driver, "onTestFailure", "Failed");
    }

    public void onTestSkipped(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestSkipped'");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestFailedButWithinSuccessPercentage'");
    }

    public void onStart(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onStart'");
        QKartTests.createScreenshotFolder();
    }

    public void onFinish(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onFinish'");
    }
}