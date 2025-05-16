package qkart.utility;

import org.openqa.selenium.WebDriver;

public class ContextManager {
    private static final ThreadLocal<ObjectContext> context = new ThreadLocal<>();

    public static void init(WebDriver driver) {
        context.set(new ObjectContext(driver));
    }

    public static ObjectContext getContext() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}