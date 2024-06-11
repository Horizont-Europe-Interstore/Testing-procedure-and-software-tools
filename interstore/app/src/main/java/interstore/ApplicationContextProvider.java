package interstore;

import org.springframework.context.ApplicationContext;

public class ApplicationContextProvider {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
