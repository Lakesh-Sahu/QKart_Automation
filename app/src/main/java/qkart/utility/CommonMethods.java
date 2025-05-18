package qkart.utility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CommonMethods extends Base {
    WebDriver driver;

    public CommonMethods(WebDriver driver) {
        this.driver = driver;
    }

    public boolean click(WebElement we) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});", we);
            we.click();
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while clicking");
            log.error("Exception while clicking : {}", getMessageFromException(e));
            return false;
        }
    }

    public boolean sendKeys(WebElement we, String keyToSend) {
        try {
            we.clear();
            Thread.sleep(250);
            we.sendKeys(keyToSend);
            Thread.sleep(250);
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while sending keys " + keyToSend);
            log.error("{} Exception while sending keys {} : {}", getCallerInfo(Thread.currentThread().getStackTrace()), keyToSend, getMessageFromException(e));
            return false;
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting current url");
            log.error("{} Exception while getting current url : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }

    public String generateDynamicUserName(String username) {
        try {
            Random rm = new Random();
            int rmInt = rm.nextInt(0, 9999);
            // Getting time stamp for generating a unique username
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // Concatenate the timestamp to string to form unique timestamp
            return username + "_" + timestamp.getTime() + rmInt;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while generating dynamic user name for username " + username);
            log.error("{} Exception while generating dynamic user name for username {} : {}", getCallerInfo(Thread.currentThread().getStackTrace()), username, getMessageFromException(e));
            return "";
        }
    }

    public boolean compareString(String s1, String s2) {
        try {
            return s1.replaceAll(" ", "").equalsIgnoreCase(s2.replaceAll(" ", ""));
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while comparing two strings");
            log.error("{} Exception while comparing two strings : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean navigateBack() {
        try {
            driver.navigate().back();
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while navigating back");
            log.error("{} Exception while navigating back : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean closeWindow() {
        try {
            driver.close();
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while closing window");
            log.error("{} Exception while closing window : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    //    Waits until a single visible or enabled child element is found inside a given parent element using the specified XPath; uses findElement and checks isDisplayed().
    public WebElement findElementFromParentByXPath(WebDriverWait wait, WebElement parentElement, String childXPath) {
        return wait.until(driver -> {
            WebElement child = parentElement.findElement(By.xpath(childXPath));
            try {
                return child.isDisplayed() || child.isEnabled() ? child : null;
            } catch (Exception e) {
                logWarningInExtentReport(e, "Exception while finding element from parent by xpath");
                log.error("{} Exception while finding element from parent by xpath : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
                return null;
            }
        });
    }

    public WebElement findElementFromParentByClassName(WebDriverWait wait, WebElement parentElement, String childClassName) {
        return wait.until(driver -> {
            WebElement child = parentElement.findElement(By.className(childClassName));
            try {
                return child.isDisplayed() || child.isEnabled() ? child : null;
            } catch (Exception e) {
                logWarningInExtentReport(e, "Exception while finding element from parent by className");
                log.error("{} Exception while finding element from parent by className : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
                return null;
            }
        });
    }

    public WebElement findElementFromParentByTagName(WebDriverWait wait, WebElement parentElement, String childTagName) {
        return wait.until(driver -> {
            WebElement child = parentElement.findElement(By.tagName(childTagName));
            try {
                return child.isDisplayed() || child.isEnabled() ? child : null;
            } catch (Exception e) {
                logWarningInExtentReport(e, "Exception while finding element from parent by tagName");
                log.error("{} Exception while finding element from parent by tagName : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
                return null;
            }
        });
    }

    //    Waits until at least one matching child element is found inside a given parent element using the specified XPath; uses findElements and returns the list
    public List<WebElement> findElementsFromParentByXPath(WebDriverWait wait, WebElement parentElement, String childXPath) {
        try {
            return wait.until(driver -> {
                List<WebElement> children = parentElement.findElements(By.xpath(childXPath));
                return children.isEmpty() ? null : children;
            });
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while finding elements from parent by xpath");
            log.error("{} Exception while finding elements from parent by xpath : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return null;
        }
    }

    public List<WebElement> findElementsFromParentByClassName(WebDriverWait wait, WebElement parentElement, String childClassName) {
        try {
            return wait.until(driver -> {
                List<WebElement> children = parentElement.findElements(By.className(childClassName));
                return children.isEmpty() ? null : children;
            });
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while finding elements from parent by className");
            log.error("{} Exception while finding elements from parent by className : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return null;
        }
    }

    public List<WebElement> findElementsFromParentByTagName(WebDriverWait wait, WebElement parentElement, String childTagName) {
        try {
            return wait.until(driver -> {
                List<WebElement> children = parentElement.findElements(By.tagName(childTagName));
                return children.isEmpty() ? null : children;
            });
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while finding elements from parent by tagName");
            log.error("{} Exception while finding elements from parent by tagName : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return null;
        }
    }

    public String getWindowHandle() {
        try {
            return driver.getWindowHandle();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting window handle");
            log.error("{} Exception while getting window handle : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return "";
        }
    }

    public Set<String> getWindowHandles() {
        try {
            return driver.getWindowHandles();
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while getting window handles");
            log.error("{} Exception while getting window handles : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return new HashSet<>();
        }
    }

    public boolean switchToWindow(String windowHandel) {
        try {
            return driver.switchTo().window(windowHandel) != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching window");
            log.error("{} Exception while switching window : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean switchAndCloseTheWindow(String windowHandel) {
        try {
            driver.switchTo().window(windowHandel).close();
            return true;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching and closing window");
            log.error("{} Exception while switching and closing window : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean switchToIFrameByIndex(int iframeIndex) {
        try {
            return driver.switchTo().frame(iframeIndex) != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching to iframe by index");
            log.error("{} Exception while switching to iframe by index : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean switchToIFrameByNameOrId(String nameOrId) {
        try {
            return driver.switchTo().frame(nameOrId) != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching to iframe by name or id");
            log.error("{} Exception while switching to iframe by name or id : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean switchToIFrameByWebElement(WebElement iframeWebElement) {
        try {
            return driver.switchTo().frame(iframeWebElement) != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching to iframe by iframe web element");
            log.error("{} Exception while switching to iframe by iframe web element : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return false;
        }
    }

    public boolean switchToParentFrame() {
        try {
            return driver.switchTo().parentFrame() != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching to parent frame of iframe");
            log.error("{} Exception while switching to parent frame of iframe : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return true;
        }
    }

    public boolean switchToDefaultContent() {
        try {
            return driver.switchTo().defaultContent() != null;
        } catch (Exception e) {
            logWarningInExtentReport(e, "Exception while switching to default content of iframe");
            log.error("{} Exception while switching to default content of iframe : {}", getCallerInfo(Thread.currentThread().getStackTrace()), getMessageFromException(e));
            return true;
        }
    }
}