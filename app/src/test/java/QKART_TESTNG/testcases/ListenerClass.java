package QKART_TESTNG.testcases;

// package QKART_TESTNG.testcases;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    public void onTestStart(ITestResult result) {
        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestStart", "Starting");
    }

    public void onTestSuccess(ITestResult result){
        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestSuccess", "Success");
    }

    public void onTestFailure(ITestResult result){
        QKART_Tests.takeScreenshot( QKART_Tests.driver, "onTestFailure", "Failed");
    }
}