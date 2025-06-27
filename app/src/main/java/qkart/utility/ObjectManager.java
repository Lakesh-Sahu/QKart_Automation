package qkart.utility;

import org.openqa.selenium.WebDriver;

public class ObjectManager {
    private static final ThreadLocal<ObjectCreator> context = new ThreadLocal<>();

    public static void init(WebDriver driver, String className, String methodName, String description) {
        context.set(new ObjectCreator(driver, className, methodName, description));
    }

    public static ObjectCreator getContext() {
        return context.get();
    }

    public static void remove() {
        context.remove();
    }
}