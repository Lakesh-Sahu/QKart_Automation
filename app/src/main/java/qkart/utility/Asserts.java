package qkart.utility;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.Assert;


public class Asserts extends Base {

    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertEquals(String actual, String expected, String message) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertEquals(int actual, int expected, String message) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }


    public static void assertNotEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertNotEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertNotEquals(String actual, String expected, String message) {
        try {
            Assert.assertNotEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertNotEquals(int actual, int expected, String message) {
        try {
            Assert.assertNotEquals(actual, expected);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }


    public static void assertNotNull(Object actual, String message) {
        try {
            Assert.assertNotNull(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertNotNull(String actual, String message) {
        try {
            Assert.assertNotNull(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertNotNull(int actual, String message) {
        try {
            Assert.assertNotNull(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }


    public static void assertTrue(Boolean actual, String message) {
        try {
            Assert.assertTrue(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertTrue(boolean actual, String message) {
        try {
            Assert.assertTrue(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }


    public static void assertFalse(Boolean actual, String message) {
        try {
            Assert.assertFalse(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }

    public static void assertFalse(boolean actual, String message) {
        try {
            Assert.assertFalse(actual);
        } catch (AssertionError e) {
            test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot.capture()).build());
            throw e;
        }
    }







//    public static void assertEquals(ExtentTest test, Object actual, Object expected, String message) {
//        try {
//            Assert.assertEquals(actual, expected);
//
//        } catch (AssertionError e) {
//            throw e;
//        }
//    }
//
//    public static void assertEquals(ExtentTest test, String actual, String expected, String message) {
//
//    }
//
//    public static void assertEquals(ExtentTest test, int actual, int expected, String message) {
//
//    }
//
//
//    public static void assertNotEquals(ExtentTest test, Object actual, Object expected, String message) {
//
//    }
//
//    public static void assertNotEquals(ExtentTest test, String actual, String expected, String message) {
//
//    }
//
//    public static void assertNotEquals(ExtentTest test, int actual, int expected, String message) {
//
//    }
//
//
//    public static void assertNotNull(ExtentTest test, Object actual, String message) {
//
//    }
//
//    public static void assertNotNull(ExtentTest test, String actual, String message) {
//
//    }
//
//    public static void assertNotNull(ExtentTest test, int actual, String message) {
//
//    }
//
//
//    public static void assertTrue(ExtentTest test, Boolean actual, String message) {
//
//    }
//
//    public static void assertTrue(ExtentTest test, boolean actual, String message) {
//
//    }
//
//
//    public static void assertFalse(ExtentTest test, Boolean actual, String message) {
//
//    }
//
//    public static void assertFalse(ExtentTest test, boolean actual, String message) {
//
//    }
}