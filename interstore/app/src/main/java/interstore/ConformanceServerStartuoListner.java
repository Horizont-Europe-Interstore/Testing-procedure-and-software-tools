package interstore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
@Component
public class ConformanceServerStartuoListner {
     private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());
    
    @Autowired
    private Environment environment;
    
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        
        LOGGER.info("🚀 IEEE 2030.5 Conformance Test Server is ready!");
        
        // Verify HTTPS server is listening
        verifyHTTPSListener();
        
        LOGGER.info("📊 Conformance test monitoring active");
        LOGGER.info("🔍 All gateway connections will be validated for CSIP compliance");
    }
    
    @EventListener
    public void onWebServerInitialized(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        LOGGER.info("🌐 Web server initialized on port: {}", port);
        LOGGER.info("🔐 HTTPS/TLS listener active and waiting for connections");
    }

    private void verifyHTTPSListener() {
        
        String port = environment.getProperty("server.port", "8443");
        
        try {
            // Test if port is bound (should fail if server is listening)
            ServerSocket testSocket = new ServerSocket();
            testSocket.bind(new InetSocketAddress("localhost", Integer.parseInt(port)));
            testSocket.close();
            
            LOGGER.error("❌ Port {} is not bound - HTTPS listener may not have started!", port);
            
        } catch (IOException e) {
            // Port is in use (good - server is listening)
            LOGGER.info("✅ HTTPS listener confirmed active on port {}", port);
        }
    }
    
}
