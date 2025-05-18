package qkart.utility;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.Arrays;

import static org.testng.Assert.assertFalse;

public class Screenshot extends Base {

    // takes the screenshot to attach in the Extent Report
    public static String capture(String callerInfo) {
        try {
            WebDriver driver = ContextManager.getContext().getDriver();

            if (driver != null) {
                String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll("[.:]", "");
                String relativePath = "/extent_reports/" + singleTimeStamp + "/" + timestamp + "_" + callerInfo + ".png";
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
                File DestFile = new File(System.getProperty("user.dir") + relativePath);
                FileUtils.copyFile(SrcFile, DestFile);
                return relativePath;
            }
        } catch (Exception e) {
            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
        }
        return "";
    }


//    public static void createScreenshotFolder() {
//        try {
//            File theDir = new File("screenshots/" + singleTimeStamp);
//                if (!theDir.exists()) {
//                    theDir.mkdirs();
//                }
//        } catch (Exception e) {
//            assertFalse(false, "Creating screenshot/" + singleTimeStamp + " folder : Fail\n" + Arrays.toString(e.getStackTrace()));
//        }
//    }

//    // takes the screenshot to track the test
//    public static void takeScreenshot(String className, String methodName, String screenshotType, String description) {
//        try {
//            WebDriver driver = ContextManager.getContext().getDriver();
//            if (driver != null) {
//                String timestamp = String.valueOf(java.time.LocalDateTime.now()).replaceAll("[.:]", "");
//                String fileName = String.format("%s_%s_%s_%s_%s.png", timestamp, className, methodName, screenshotType, description);
//                String relativePath = "/screenshots/" + singleTimeStamp + "/" + fileName;
//                TakesScreenshot scrShot = ((TakesScreenshot) driver);
//                File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
//                File DestFile = new File(System.getProperty("user.dir") + relativePath);
//                FileUtils.copyFile(SrcFile, DestFile);
//            }
//        } catch (Exception e) {
//            assertFalse(false, "Taking screenshot : Fail" + Arrays.toString(e.getStackTrace()));
//        }
//    }
}