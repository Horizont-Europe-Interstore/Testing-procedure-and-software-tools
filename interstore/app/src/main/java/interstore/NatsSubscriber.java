package interstore;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.intuit.karate.Runner;

public class NatsSubscriber {
    private Connection nc;
    private String lastMessage; 
    private ArrayList<String> subjects; 
    public NatsSubscriber(String natsUrl) throws Exception {
        // Connect to NATS server
        nc = Nats.connect(natsUrl);
        subjects = new ArrayList<>();
        setSubjects(subjects);
    }

    public void subscribe(String subject) {
        // Create a dispatcher for handling messages
        Dispatcher dispatcher = nc.createDispatcher((msg) -> {
            String message = new String(msg.getData());
            this.lastMessage = message;
            System.out.println("Received on [" + msg.getSubject() + "]: " + message);
            
            // Extract XML root element and run corresponding test
            String xmlRootElement = extractXmlRootElement(message);
            runTestForXmlElement(xmlRootElement);
        });

        dispatcher.subscribe(subject);
        System.out.println("Subscribed to: " + subject);
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
