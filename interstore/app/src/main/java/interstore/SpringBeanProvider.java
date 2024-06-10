package interstore;

import com.google.inject.Provider;
public class SpringBeanProvider<T> implements Provider<T> {
    private Class<T> type;

    public SpringBeanProvider(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        return ApplicationContextProvider.getApplicationContext().getBean(type);
    }
}

