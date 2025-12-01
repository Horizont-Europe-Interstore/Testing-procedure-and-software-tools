package interstore;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class NatsPublisher {
    private static final Logger LOGGER = Logger.getLogger(NatsPublisher.class.getName());
    private Connection nc;
    
    public void initializeConnection(String natsUrl) throws Exception {
        nc = Nats.connect(natsUrl);
        LOGGER.info("NATS publisher connected to: " + natsUrl);
    }
    
    public void publish(String subject, String message) {
        try {
            nc.publish(subject, message.getBytes());
            LOGGER.info("NATS_PUBLISHED: " + message);
        } catch (Exception e) {
            LOGGER.severe("Error publishing message: " + e.getMessage());
        }
    }
    
    public void close() throws InterruptedException {
        if (nc != null) {
            nc.close();
        }
    }
}