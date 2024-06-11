
/* 
package interstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = App.class) // Adjust if you have a specific test configuration
public class TestConfig {

    @Bean
    public ApplicationContextProvider applicationContextProvider(ApplicationContext applicationContext) {
     
        ApplicationContextProvider.setApplicationContext((ConfigurableApplicationContext) applicationContext);
        //ApplicationContextProvider.setApplicationContext(applicationContext);
        return new ApplicationContextProvider();
    }
}


*/ 