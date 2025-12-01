package interstore;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.intuit.karate.Runner;

@Component
public class NatsSubscriber {
    private static final Logger LOGGER = Logger.getLogger(NatsSubscriber.class.getName());
    private Connection nc;
    private String lastMessage; 
    private ArrayList<String> subjects;
    
    @Autowired
    private HttpRequestHandler httpRequestHandler;
    
    public NatsSubscriber() {}
    
    public NatsSubscriber(String natsUrl) throws Exception {
        initializeConnection(natsUrl);
    }
    
    public void initializeConnection(String natsUrl) throws Exception {
        // Connect to NATS server
        nc = Nats.connect(natsUrl);
        subjects = new ArrayList<>();
        setSubjects(subjects);
        LOGGER.info("NATS connection initialized to: " + natsUrl);
    }

    public void subscribe(String subject) {
        // Create a dispatcher for handling messages
        Dispatcher dispatcher = nc.createDispatcher((msg) -> {
            String message = new String(msg.getData());
            this.lastMessage = message;
            LOGGER.info("NATS_RECEIVED: " + message);
            
            // Parse HTTP request from NATS message and forward to backend
            handleHttpRequest(message);
        });

        dispatcher.subscribe(subject);
        LOGGER.info("Subscribed to: " + subject);
    }
    
    private void handleHttpRequest(String message) {
        try {
            // Parse XML message from Raspberry Pi
            String correlationId = extractCorrelationId(message);
            String method = extractXmlValue(message, "method");
            String path = extractXmlValue(message, "path");
            String body = extractXmlValue(message, "body");
            String contentType = extractXmlValue(message, "contentType");
            String lfdi = extractXmlValue(message, "lfdi");
            String sfdi = extractXmlValue(message, "sfdi");
            
            LOGGER.info("PROCESSING_REQUEST: " + method + " " + path + " [" + correlationId + "]");
            
            // Forward to Java backend and publish response
            String response = httpRequestHandler.forwardToBackend(method, path, body, contentType, lfdi, sfdi, correlationId);
            
            LOGGER.info("Response sent back via NATS");
            
        } catch (Exception e) {
            LOGGER.severe("Error handling HTTP request: " + e.getMessage());
        }
    }
    
    private String extractCorrelationId(String xml) {
        return extractXmlValue(xml, "correlationId");
    }
    
    private String extractXmlValue(String xml, String tag) {
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int start = xml.indexOf(startTag);
        int end = xml.indexOf(endTag);
        if (start != -1 && end != -1) {
            return xml.substring(start + startTag.length(), end);
        }
        return "";
    }

    public String getLastMessage() {
        return lastMessage;
    }
    
    public void setSubjects(ArrayList<String> subjects)
    {
        this.subjects = subjects;
    }
  
     public ArrayList<String> getSubjects()
     {
        return this.subjects;
     }


    private String extractXmlRootElement(String xmlMessage) {
        int startIndex = xmlMessage.indexOf('<');
        int endIndex = xmlMessage.indexOf(' ', startIndex);
        if (endIndex == -1) endIndex = xmlMessage.indexOf('>', startIndex);
        return startIndex != -1 && endIndex != -1 ? xmlMessage.substring(startIndex + 1, endIndex) : null;
    }
    
    private void runTestForXmlElement(String xmlElement) {
        String featureFile = mapXmlElementToFeature(xmlElement);
        if (featureFile != null) {
            System.out.println(" Karate testing disabled - would have run: " + featureFile);
            // Karate execution disabled for now
            /*
            try {
                // Set the message as system property for Karate to access
                System.setProperty("nats.message", lastMessage);
                Runner.path("file:/app/src/test/resources/" + featureFile).parallel(1);
            } catch (Exception e) {
                System.err.println("Failed to run test: " + e.getMessage());
            }
            */
        }
    }
    
    private String mapXmlElementToFeature(String xmlElement) {
        System.out.println("XML Element: " + xmlElement);
        
        if (xmlElement != null) {
            // Direct mapping: XML element name + .feature
            // e.g., "DeviceCapability" -> "DeviceCapability.feature"
            String featureFileName = xmlElement + ".feature";
            System.out.println("Mapped to feature file: " + featureFileName);
            return featureFileName;
        }
        
        System.out.println("No XML element found");
        return null;
    }

    public void close() throws InterruptedException {
        if (nc != null) {
            nc.close();
        }
    }
}
