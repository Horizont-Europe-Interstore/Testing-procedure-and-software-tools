package interstore;
import interstore.Types.TimeType;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import javax.jmdns.*;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceEvent;
import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
@Scope("singleton")
public class Exapp {
    //   private MessageToPublish messageToPublish;
    //   private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
      private UIControleHandler uiControleHandler;
      private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    //   private JmDNS jmdns;
    //   private final String serverSfdi = "392440693763";  // Your server's SFDI
    //   private final String serverLfdi = "92320FA007FFE32AFA35088DA87F7E83048A270F" ;
                     

    public Exapp( String natsUrl) throws Exception
    {
        // this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        // this.messageToPublish = new MessageToPublish(natsUrl, serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
    }
    
    public Exapp() throws Exception 
    {

    }
    
    public void start(String natsUrl) throws Exception
    {    LOGGER.info("the nats url is " + natsUrl);
        // this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        // this.messageToPublish = new MessageToPublish(natsUrl, this.serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
        this.uiControleHandler.setupBridge();
        LOGGER.info("everything is intailized ");    
    }


     private static void displayServerInfo(ApplicationContext context) {
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8443");
        
        System.out.println();
        System.out.println("✅ IEEE 2030.5 Server Started Successfully!");
        System.out.println("📡 HTTPS Listener: https://" + getLocalIPAddress() + ":" + port);
        System.out.println("🔗 NATS Integration: ACTIVE");
        System.out.println("🔒 TLS Client Auth: REQUIRED");
        System.out.println();
        System.out.println("🎯 Configure InsightHome Gateway:");
        System.out.println("   Server IP: " + getLocalIPAddress());
        System.out.println("   Server Port: " + port);
        System.out.println("   Protocol: HTTPS with client certificate");
        System.out.println();
        System.out.println("⏳ Waiting for gateway connections...");
        System.out.println("Press Ctrl+C to stop server");
        System.out.println("==========================================");
    }

    private static String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
   
    public static void main(String[] args) throws Exception {
        String natsUrl = "nats://nats-server:4222";
        ApplicationContext context = SpringApplication.run(App.class);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = (App)context.getBean("app");
        mainApp.start(natsUrl);
        displayServerInfo(context);
       
    }
    
}
       

