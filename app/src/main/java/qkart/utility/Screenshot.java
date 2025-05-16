package qkart.utility;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.Arrays;

import static org.testng.Assert.assertFalse;

public class Screenshot extends Base {
    static int i;

    public static void createScreenshotFolder() {
        try {
            for (i = 1; i <= 100; i++) {
                File theDir = new File("screenshots/ss" + i);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                    break;
                }
            }
        } catch (Exception e) {
            assertFalse(false, "Creating Screenshot/ss folder : Fail\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    // screenshot method to track the test
    public static void takeScreenshot(String className, String methodName, String screenshotType, String description) {
        try {
            WebDriver driver = getDriverForScreenshot(className);
            if (driver != null) {
                String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll(".:", "");
                String fileName = String.format("%s_%s_%s_%s_%s.png", timestamp, className, methodName, screenshotType, description);
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
                File DestFile = new File("screenshots/ss" + i + "/" + fileName);
                FileUtils.copyFile(SrcFile, DestFile);
            }
        } catch (Exception e) {
            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
        }
    }

//    // takes the screenshot to attach in the Extent Report
//    public static String capture(String className, String methodName, String screenshotType, String description) {
//        String fileName = "";
//        try {
//            WebDriver driver = getDriverForScreenshot(className);
//
//            if (driver != null) {
//                String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll(".:", "");
//                fileName = String.format("%s_%s_%s_%s_%s.png", timestamp, className, methodName, screenshotType, description);
//                TakesScreenshot scrShot = ((TakesScreenshot) driver);
//                File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
//                File DestFile = new File("/extent reports/screenshots" + "/" + fileName);
//                FileUtils.copyFile(SrcFile, DestFile);
//            }
//        } catch (Exception e) {
//            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
//        }
//        return "screenshots/" + fileName;
//    }

    // takes the screenshot to attach in the Extent Report
    public static String capture() {
        String fileName = "";
        try {
            WebDriver driver = getDriverForScreenshot();

            if (driver != null) {
                String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll(".:", "");
                fileName = String.format("%s.png", timestamp);
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
                File DestFile = new File("/extent reports/screenshots" + "/" + fileName);
                FileUtils.copyFile(SrcFile, DestFile);
            }
        } catch (Exception e) {
            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
        }
        return "screenshots/" + fileName;
    }

    public static WebDriver getDriverForScreenshot() {
        return ContextManager.getContext().getDriver();
    }

    public static WebDriver getDriverForScreenshot(String className) {
        return ContextManager.getContext().getDriver();
    }
}
