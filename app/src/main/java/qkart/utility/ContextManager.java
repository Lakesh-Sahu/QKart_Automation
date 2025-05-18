package qkart.utility;

import org.openqa.selenium.WebDriver;

public class ContextManager {
    private static final ThreadLocal<ObjectContext> context = new ThreadLocal<>();

    public static void init(WebDriver driver, String callerInfo) {
        context.set(new ObjectContext(driver, callerInfo));
    }

    public static ObjectContext getContext() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}