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
            log.error("Exception while clicking : {}", getMessage(e));
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
            log.error("{} Exception while sending keys {} : {}", logCallerInfo(Thread.currentThread().getStackTrace()), keyToSend, getMessage(e));
            return false;
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            log.error("{} Exception while getting current url : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
            log.error("{} Exception while generating dynamic user name for username {} : {}", logCallerInfo(Thread.currentThread().getStackTrace()), username, getMessage(e));
            return "";
        }
    }

    public boolean compareString(String s1, String s2) {
        try {
            return s1.replaceAll(" ", "").equalsIgnoreCase(s2.replaceAll(" ", ""));
        } catch (Exception e) {
            log.error("{} Exception while comparing two strings : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public String getMessage(Exception e) {
        try {
            return e.getMessage().split("\n")[0];
        } catch (Exception i) {
            return "";
        }
    }

    public boolean navigateBack() {
        try {
            driver.navigate().back();
            return true;
        } catch (Exception e) {
            log.error("{} Exception while navigating back : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean closeWindow() {
        try {
            driver.close();
            return true;
        } catch (Exception e) {
            log.error("{} Exception while closing window : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
                log.error("{} Exception while finding element from parent by xpath : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
                log.error("{} Exception while finding element from parent by className : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
                log.error("{} Exception while finding element from parent by tagName : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
            log.error("{} Exception while finding elements from parent by xpath : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
            log.error("{} Exception while finding elements from parent by className : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
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
            log.error("{} Exception while finding elements from parent by tagName : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return null;
        }
    }

    public String getWindowHandle() {
        try {
            return driver.getWindowHandle();
        } catch (Exception e) {
            log.error("{} Exception while getting window handle : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return "";
        }
    }

    public Set<String> getWindowHandles() {
        try {
            return driver.getWindowHandles();
        } catch (Exception e) {
            log.error("{} Exception while getting window handles : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return new HashSet<>();
        }
    }

    public boolean switchToWindow(String windowHandel) {
        try {
            return driver.switchTo().window(windowHandel) != null;
        } catch (Exception e) {
            log.error("{} Exception while switching window : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean switchAndCloseTheWindow(String windowHandel) {
        try {
            driver.switchTo().window(windowHandel).close();
            return true;
        } catch (Exception e) {
            log.error("{} Exception while switching and closing window : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean switchToIFrameByIndex(int iframeIndex) {
        try {
            return driver.switchTo().frame(iframeIndex) != null;
        } catch (Exception e) {
            log.error("{} Exception while switching to iframe by index : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean switchToIFrameByNameOrId(String nameOrId) {
        try {
            return driver.switchTo().frame(nameOrId) != null;
        } catch (Exception e) {
            log.error("{} Exception while switching to iframe by name or id : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean switchToIFrameByWebElement(WebElement iframeWebElement) {
        try {
            return driver.switchTo().frame(iframeWebElement) != null;
        } catch (Exception e) {
            log.error("{} Exception while switching to iframe by iframe web element : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return false;
        }
    }

    public boolean switchToParentFrame() {
        try {
            return driver.switchTo().parentFrame() != null;
        } catch (Exception e) {
            log.error("{} Exception while switching to parent frame of iframe : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return true;
        }
    }

    public boolean switchToDefaultContent() {
        try {
            return driver.switchTo().defaultContent() != null;
        } catch (Exception e) {
            log.error("{} Exception while switching to default content of iframe : {}", logCallerInfo(Thread.currentThread().getStackTrace()), getMessage(e));
            return true;
        }
    }
}