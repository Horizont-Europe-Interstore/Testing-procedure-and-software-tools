package interstore;
import java.util.logging.Logger;

import io.nats.client.*;
import java.nio.charset.StandardCharsets;

public class MessageToPublish {
    private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
    private ServiceDiscoveryVerticle serviceDiscovery;
    private  Connection natsConnection;  
    private static final Logger LOGGER = Logger.getLogger(MessageToPublish.class.getName());
    


    public MessageToPublish() {
    this.serviceDiscovery = serviceDiscoveryVerticle; 
    } 
    
    public MessageToPublish(String natsUrl,ServiceDiscoveryVerticle serviceDiscoveryVerticle ) {

        this.serviceDiscoveryVerticle = serviceDiscoveryVerticle;
        LOGGER.info("Servcie discovery verticle is : " + this.serviceDiscoveryVerticle);
        try
        {
            this.natsConnection = Nats.connect(natsUrl);
        } catch(Exception e)
        {
            LOGGER.severe("Error connecting to nats server: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
       
       
    }

    
   /** 
     * This method publish the message using natsConnection.publish 
     * Things to take into consideration before publishing the message is 
     * validate the nats subject if the nats subject is inappropiate and invalid 
     * the message will not be published and the check the payload if the payload is
     * as expected then the message will be published ; to validate these thigns we need a 
     * method called validatePayload() and validateSubject() before publishing the message 
     * @param natSubject
     * @param message
     */
    public void PublishToSubject(String natSubject, String message)
    
    {   
        
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        natsConnection.publish(natSubject, messageBytes);
    }
        
    public void closeConnection() throws Exception 
    {
       this.natsConnection.close();
   }

  
   public void subscribeMessage(String natsSubject)
   { 
    try
     {
        this.serviceDiscoveryVerticle.setupBridge(natsSubject); 
      
     } catch(Exception e)
     {
        LOGGER.severe("Error subscribing to message: " + e.getMessage());
        e.printStackTrace();
     }
   }
  
   public void reSubscribeMessage(String natsSubject, String natsUrl, String serviceName )
   {
    try
     {
        ServiceDiscoveryVerticle msg = new ServiceDiscoveryVerticle(natsUrl);
        msg.setupBridgeForResponse(natsSubject, serviceName);
        
     } catch(Exception e)
     {
        LOGGER.severe("Error subscribing to message: " + e.getMessage());
        e.printStackTrace();
     }
   }


   // based on the servicename received it should return to the class it sends 
   public String responseToSender(String serviceName, String responseMessage)
   { 
      MessageFactory messageFactory = new MessageFactory(); 
      LOGGER.info("Response message giving back to test class  : " + responseMessage); 
      messageFactory.selfDeviceEndDeviceTests(serviceName, responseMessage);
      // messageFactory.selfTimeEndDevice()
      return responseMessage; 
   } 

   public void newStart(String natsSubject, String payLoad) throws InterruptedException {
      subscribeMessage(natsSubject);
      Thread.sleep(100); 
      PublishToSubject(natsSubject,  payLoad);
   }

}


// while creating each end devices there should be  corresponding  link for each enddevice . 
// while performing the get request to the above mentioned enddevice link . the link should return 
// SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI . 