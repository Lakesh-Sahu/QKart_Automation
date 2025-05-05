package qkart.utility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CommonMethods {
    static WebDriver driver;

    static {
        driver = DriverFactory.getDriver();
    }

    public boolean click(WebElement we) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});", we);
            we.click();
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
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
            return false;
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean compareString(String s1, String s2) {
        try {
            return s1.replaceAll(" ", "").equalsIgnoreCase(s2.replaceAll(" ", ""));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean navigateBack() {
        try {
            driver.navigate().back();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //    Waits until a single visible or enabled child element is found inside a given parent element using the specified XPath; uses findElement and checks isDisplayed().
    public WebElement findElementFromParentByXPath(WebDriverWait wait, WebElement parentElement, String childXPath) {
        return wait.until(driver -> {
            WebElement child = parentElement.findElement(By.xpath(childXPath));
            try {
                return child.isDisplayed() || child.isEnabled() ? child : null;
            } catch (NoSuchElementException e) {
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
                return null;
            }
        });
    }

    //    Waits until at least one matching child element is found inside a given parent element using the specified XPath; uses findElements and returns the list
    public List<WebElement> findElementsFromParentByXPath(WebDriverWait wait, WebElement parentElement, String childXPath) {
        return wait.until(driver -> {
            List<WebElement> children = parentElement.findElements(By.xpath(childXPath));
            return children.isEmpty() ? null : children;
        });
    }

    public List<WebElement> findElementsFromParentByClassName(WebDriverWait wait, WebElement parentElement, String childClassName) {
        return wait.until(driver -> {
            List<WebElement> children = parentElement.findElements(By.className(childClassName));
            return children.isEmpty() ? null : children;
        });
    }

    public List<WebElement> findElementsFromParentByTagName(WebDriverWait wait, WebElement parentElement, String childTagName) {
        return wait.until(driver -> {
            List<WebElement> children = parentElement.findElements(By.tagName(childTagName));
            return children.isEmpty() ? null : children;
        });
    }

    public boolean switchToWindow(String windowHandel) {
        try {
            return driver.switchTo().window(windowHandel) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchAndCloseTheWindow(String windowHandel) {
        try {
            driver.switchTo().window(windowHandel).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToIFrameByIndex(int iframeIndex) {
        try {
            return driver.switchTo().frame(iframeIndex) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToIFrameByNameOrId(String nameOrId) {
        try {
            return driver.switchTo().frame(nameOrId) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToIFrameByWebElement(WebElement iframeWebElement) {
        try {
            return driver.switchTo().frame(iframeWebElement) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToParentFrame() {
        try {
            return driver.switchTo().parentFrame() != null;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean switchToDefaultContent() {
        try {
            return driver.switchTo().defaultContent() != null;
        } catch (Exception e) {
            return true;
        }
    }
}