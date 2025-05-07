package qkart.utility;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    public void onTestStart(ITestResult result) {
//        String className = result.getTestClass().getName();
//        String methodName = result.getMethod().getMethodName();
//        Screenshot.takeScreenshot(className, methodName, "onTestStart", "Starting");
    }

    public void onTestSuccess(ITestResult result){
//        String className = result.getTestClass().getName();
//        String methodName = result.getMethod().getMethodName();
//        Screenshot.takeScreenshot(className, methodName, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result){
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        Screenshot.takeScreenshot(className, methodName, "onTestFailure", "Failed");
    }

    public void onTestSkipped(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestSkipped'");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // throw new UnsupportedOperationException("Unimplemented method 'onTestFailedButWithinSuccessPercentage'");
    }

    public void onStart(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onStart'");
        Screenshot.createScreenshotFolder();
    }

    public void onFinish(ITestContext context) {
        // throw new UnsupportedOperationException("Unimplemented method 'onFinish'");
    }
}