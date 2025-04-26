package QKART_TESTNG.testcases;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    public void onTestStart(ITestResult result) {
//        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestStart", "Starting");
    }

    public void onTestSuccess(ITestResult result){
//        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result){
        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestFailure", "Failed");
    }

    public void onTestSkipped(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestSkipped'");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestFailedButWithinSuccessPercentage'");
    }

    public void onStart(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onStart'");
        QKART_Tests.createScreenshotFolder();
    }

    public void onFinish(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onFinish'");
    }
}