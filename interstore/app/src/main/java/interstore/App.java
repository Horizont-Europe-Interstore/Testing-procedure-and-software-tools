
package interstore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories( {"interstore.DeviceCapability", "interstore.Identity"
, "interstore.EndDevice", "interstore.Types", "interstore.Registration", "interstore.DER", "interstore.FunctionSetAssignments"
        ,"interstore.DERProgram", "interstore.Time", "interstore.DERCurve", "interstore.Events", "interstore.DERControl", "interstore.TestResults"})
@EntityScan(basePackages = "interstore")
@ComponentScan(basePackages = "interstore")
@Repository
@Scope("singleton")
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public App(String natsUrl) throws Exception {
    }
    
    public App() throws Exception {
    }

    public void start(String natsUrl) throws Exception {    
        LOGGER.info("the nats url is " + natsUrl);
        
        // Initialize NATS subscriber only
        NatsSubscriber subscriber = ApplicationContextProvider.getApplicationContext().getBean(NatsSubscriber.class);
        
        subscriber.initializeConnection(natsUrl);
        subscriber.subscribe("ieee2030.requests");

        LOGGER.info("Everything is initialized including IEEE 2030.5 NATS subscriber");
    }

    public static void main(String[] args) throws Exception {
        String natsUrl = "nats://18.232.7.53:4222";
        ApplicationContext context = SpringApplication.run(App.class, args);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = context.getBean(App.class);
        System.out.println("🚀 Application started with Modern UI");
        mainApp.start(natsUrl);
       
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔌 Shutting down IEEE 2030.5 server...");
            System.out.println("✅ Server shutdown complete");
        }));
    }
}
