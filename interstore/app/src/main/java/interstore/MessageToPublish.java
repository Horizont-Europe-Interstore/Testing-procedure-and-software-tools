// package interstore;

// import io.nats.client.Connection;
// import io.nats.client.Nats;
// import io.nats.client.Options;

// import java.nio.charset.StandardCharsets;
// import java.util.logging.Logger;

// public class MessageToPublish {
//     private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
//     private ServiceDiscoveryVerticle serviceDiscovery;
//     private  Connection natsConnection;  
//     private static final Logger LOGGER = Logger.getLogger(MessageToPublish.class.getName());
    


//     public MessageToPublish() {
//     this.serviceDiscovery = serviceDiscoveryVerticle; 
//     } 
    
//     public MessageToPublish(String natsUrl,ServiceDiscoveryVerticle serviceDiscoveryVerticle ) {

//         this.serviceDiscoveryVerticle = serviceDiscoveryVerticle;
//         LOGGER.info("Service discovery verticle is : " + this.serviceDiscoveryVerticle);
        
//         // Configure NATS connection options
//         Options options = new Options.Builder()
//             .server(natsUrl)
//             .connectionTimeout(java.time.Duration.ofSeconds(10))
//             .pingInterval(java.time.Duration.ofSeconds(2))
//             .reconnectWait(java.time.Duration.ofSeconds(1))
//             .maxReconnects(5)
//             .build();
            
//         LOGGER.info("MessageToPublish attempting to connect to NATS at: " + natsUrl);
        
//         try {
//             this.natsConnection = Nats.connect(options);
            
//             if (this.natsConnection.getStatus() == Connection.Status.CONNECTED) {
//                 LOGGER.info("MessageToPublish successfully connected to NATS server at: " + natsUrl);
//             } else {
//                 LOGGER.warning("MessageToPublish NATS connection status: " + this.natsConnection.getStatus());
//             }
            
//         } catch(Exception e) {
//             LOGGER.severe("MessageToPublish failed to connect to NATS server at " + natsUrl + ": " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("NATS connection failed in MessageToPublish", e);
//         }
//     }

    
//    /** 
//      * This method publish the message using natsConnection.publish 
//      * Things to take into consideration before publishing the message is 
//      * validate the nats subject if the nats subject is inappropiate and invalid 
//      * the message will not be published and the check the payload if the payload is
//      * as expected then the message will be published ; to validate these thigns we need a 
//      * method called validatePayload() and validateSubject() before publishing the message 
//      * @param natSubject
//      * @param message
//      */
//     public void PublishToSubject(String natSubject, String message)
    
//     {   
//         // Check connection health before publishing
//         if (this.natsConnection == null || this.natsConnection.getStatus() != Connection.Status.CONNECTED) {
//             LOGGER.severe("NATS connection is not available for publishing. Status: " + 
//                          (this.natsConnection != null ? this.natsConnection.getStatus() : "null"));
//             throw new RuntimeException("NATS connection not available");
//         }
        
//         LOGGER.info("Publishing message to subject: " + natSubject + ", message: " + message);
//         byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
//         natsConnection.publish(natSubject, messageBytes);
//         LOGGER.info("Message published successfully to: " + natSubject);
//     }
        
//     public void closeConnection() throws Exception 
//     {
//        this.natsConnection.close();
//    }

  
//    public void subscribeMessage(String natsSubject)
//    { 
//     try
//      {
//         this.serviceDiscoveryVerticle.setupBridge(natsSubject); 
      
//      } catch(Exception e)
//      {
//         LOGGER.severe("Error subscribing to message: " + e.getMessage());
//         e.printStackTrace();
//      }
//    }
  
//    public void reSubscribeMessage(String natsSubject, String natsUrl, String serviceName )
//    {
//     try
//      {
//         ServiceDiscoveryVerticle msg = new ServiceDiscoveryVerticle(natsUrl);
//         msg.setupBridgeForResponse(natsSubject, serviceName);
        
//      } catch(Exception e)
//      {
//         LOGGER.severe("Error subscribing to message: " + e.getMessage());
//         e.printStackTrace();
//      }
//    }


//    // based on the servicename received it should return to the class it sends 
//    public String responseToSender(String serviceName, String responseMessage)
//    { 
//       MessageFactory messageFactory = new MessageFactory(); 
//       LOGGER.info("Response message giving back to test class  : " + responseMessage + "Service name" + serviceName);
//       messageFactory.selfDeviceEndDeviceTests(serviceName, responseMessage);
//       // messageFactory.selfTimeEndDevice()
//       return responseMessage; 
//    } 

//    public void newStart(String natsSubject, String payLoad) throws InterruptedException {
//       subscribeMessage(natsSubject);
//       Thread.sleep(100); 
//       PublishToSubject(natsSubject,  payLoad);
//    }

// }


// // while creating each end devices there should be  corresponding  link for each enddevice . 
// // while performing the get request to the above mentioned enddevice link . the link should return 
// // SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI . 