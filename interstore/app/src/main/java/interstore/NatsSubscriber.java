package interstore;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

public class NatsSubscriber {
    private Connection nc;

    public NatsSubscriber(String natsUrl) throws Exception {
        // Connect to NATS server
        nc = Nats.connect(natsUrl);
    }

    public void subscribe(String subject) {
        // Create a dispatcher for handling messages
        Dispatcher dispatcher = nc.createDispatcher((msg) -> {
            String message = new String(msg.getData());
            System.out.println("Received on [" + msg.getSubject() + "]: " + message);
            // You can add logic to process the message here
        });

        dispatcher.subscribe(subject);
        System.out.println("Subscribed to: " + subject);
    }

    // Optionally, a close method if you want to gracefully disconnect
    public void close() throws InterruptedException {
        if (nc != null) {
            nc.close();
        }
    }
}
