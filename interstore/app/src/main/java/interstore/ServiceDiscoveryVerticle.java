// package interstore;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.nats.client.Connection;
// import io.nats.client.Dispatcher;
// import io.nats.client.MessageHandler;
// import io.nats.client.Nats;
// import io.nats.client.Options;
// import io.vertx.core.AbstractVerticle;
// import org.json.JSONException;
// import org.json.JSONObject;
// import org.json.JSONTokener;

// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Method;
// import java.nio.charset.StandardCharsets;
// import java.util.Map;
// import java.util.logging.Logger;


// public class ServiceDiscoveryVerticle extends AbstractVerticle {
//     private MicroServiceFactory microServiceFactory; 
//     private  Connection natsConnection;
//     private  Dispatcher dispatcher;
//     private  MessageHandler messageHandler;
//     public  MessageToPublish messageToPublish;  

//     private String natsUrl; 
//     public String natsMatter; 
//     public String serviceName;
//     private static final Logger LOGGER = Logger.getLogger(ServiceDiscoveryVerticle.class.getName());

//     public ServiceDiscoveryVerticle(String natsUrl) throws Exception{  
//         this.natsUrl = natsUrl; 
        
//         // Configure NATS connection options with timeout and retry settings
//         Options options = new Options.Builder()
//             .server(natsUrl)
//             .connectionTimeout(java.time.Duration.ofSeconds(10))
//             .pingInterval(java.time.Duration.ofSeconds(2))
//             .reconnectWait(java.time.Duration.ofSeconds(1))
//             .maxReconnects(5)
//             .build();
            
//         LOGGER.info("Attempting to connect to NATS at: " + natsUrl);
        
//         try {
//             this.natsConnection = Nats.connect(options);
            
//             // Verify connection is established
//             if (this.natsConnection.getStatus() == Connection.Status.CONNECTED) {
//                 LOGGER.info("Successfully connected to NATS server at: " + natsUrl);
//             } else {
//                 LOGGER.warning("NATS connection status: " + this.natsConnection.getStatus());
//             }
            
//         } catch (Exception e) {
//             LOGGER.severe("Failed to connect to NATS server at " + natsUrl + ": " + e.getMessage());
//             throw new Exception("NATS connection failed", e);
//         }
        
//         this.microServiceFactory = new MicroServiceFactory(); 
//         this.messageToPublish = new MessageToPublish(); 
//         this.serviceName = null; 
//        }

      
       
//     public MessageHandler getMessageHandler(String natssubject)
//        {
//          return msg -> {
//                String messageContent = new String(msg.getData(), StandardCharsets.UTF_8 );
//                LOGGER.info("JAVA-BACKEND: Received message from NATS: " + messageContent);
//                try {
//                 LOGGER.info("JAVA-BACKEND: Unsubscribing from subject: " + natssubject);
//                 this.dispatcher.unsubscribe(natssubject);
//             } catch (Exception e) {
//                 LOGGER.info("JAVA-BACKEND: Error unsubscribing: " + String.valueOf(e)); 
            
//             }
//                try {
//                 LOGGER.info("JAVA-BACKEND: Processing message with vertxSetUp");
//                 this.vertxSetUp(messageContent);
//             } catch (JsonProcessingException | JSONException e) {
//                 LOGGER.severe("JAVA-BACKEND: Error processing message: " + e.getMessage());
//                 e.printStackTrace();
//             } 
//            };
        
//        }

//        public Dispatcher subscribeFromNats(String natsSubject)
//        {

//          this.dispatcher.subscribe(natsSubject);
//          LOGGER.info("Dispatcher created and subscribed to subject: " + natsSubject);
//          return this.dispatcher ; 
//        }
//        public void setupBridge(String natsSubject) 
//        {   
//            this.natsMatter(natsSubject); 
//            this.messageHandler = getMessageHandler(natsSubject);
//            this.dispatcher = natsConnection.createDispatcher(this.messageHandler);
//            subscribeFromNats(natsSubject); 
          
//        }
        
//        public void natsMatter(String natsSubject)
//        {
//           this.natsMatter =  "\"" + natsSubject + "\"";
//        }
//        public String getNatsMatter()
//        {
//          return this.natsMatter ; 
//        }

//         public MessageHandler responseMessageHandler(String serviceName)
//        {
//            return msg -> {
//                String messageContent = new String(msg.getData(), StandardCharsets.UTF_8);
//                this.messageToPublish.responseToSender(serviceName, messageContent); 
//                try {
//                 this.dispatcher.unsubscribe(getNatsMatter());
//             } catch (Exception e) {
//                 LOGGER.info(String.valueOf(e)); 
//             }
//            };
//        }
       
//        public void setupBridgeForResponse(String natsSubject , String serviceName)
//        {     
//            this.dispatcher = natsConnection.createDispatcher(this.responseMessageHandler(serviceName));
//            subscribeFromNats(natsSubject);
           
//        }

//        public void setServiceName(String serviceName)
//        {
//           this.serviceName =   serviceName ;    
//        }

//        public String getServiceName()
//        {
//            return this.serviceName;
//        } 

//        public String extractService(String natsmsg) throws JSONException
//        {
//             LOGGER.info(natsmsg);
//             JSONTokener parser = new JSONTokener(natsmsg);
//             JSONObject msg = new JSONObject(parser);
//            String serviceName = msg.getString("servicename");
//            setServiceName(serviceName);
//            return serviceName;
//        }

      


//     public Object getMicroservice(Map<String, Object> microservice, String serviceName)
//     {
//         Object serviceObject = microservice.get(serviceName); 
//         return serviceObject; 
//     }
    

//     public void vertxSetUp(String natsmsg) throws JsonProcessingException, JSONException
//     {
        
//          Object microServiceObject = getMicroservice(this.microServiceFactory.getMicroservices(), extractService(natsmsg)); 
       
//          try {
//                 Class<?> microServiceClass = microServiceObject.getClass();
//                 Method method = microServiceClass.getMethod("chooseMethod_basedOnAction", String.class);
//                 LOGGER.info("the nats message in the service discovery verticle is " + natsmsg);
//                 Object response = method.invoke(microServiceObject, natsmsg);
//                 this.sendResponseToNats( natsmsg,response);
//             } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                  LOGGER.warning("Error invoking method: " + e.getMessage());   
//              } 
            
//             }
    
    
//     public void sendResponseToNats(String natsSubject ,Object response) throws JsonProcessingException {
//                ObjectMapper objectMapper = new ObjectMapper();
//                 String responseMessage = objectMapper.writeValueAsString(response);
//                 LOGGER.info("Response message in  service verticle : " + responseMessage);
                
//                 // Check connection health before publishing
//                 if (!isNatsConnected()) {
//                     LOGGER.warning("NATS connection lost, attempting to reconnect...");
//                     try {
//                         reconnectToNats();
//                     } catch (Exception e) {
//                         LOGGER.severe("Failed to reconnect to NATS: " + e.getMessage());
//                         throw new RuntimeException("NATS connection failed", e);
//                     }
//                 }
                
//                 this.messageToPublish.reSubscribeMessage(getNatsMatter(), this.natsUrl , getServiceName()); 
//                 // Use  NATS connection to publish the response
//                 natsConnection.publish(getNatsMatter(), responseMessage.getBytes(StandardCharsets.UTF_8));
              
//             }
            
//     public boolean isNatsConnected() {
//         return this.natsConnection != null && 
//                this.natsConnection.getStatus() == Connection.Status.CONNECTED;
//     }
    
//     public void reconnectToNats() throws Exception {
//         if (this.natsConnection != null && !this.natsConnection.getStatus().equals(Connection.Status.CLOSED)) {
//             this.natsConnection.close();
//         }
        
//         Options options = new Options.Builder()
//             .server(this.natsUrl)
//             .connectionTimeout(java.time.Duration.ofSeconds(10))
//             .pingInterval(java.time.Duration.ofSeconds(2))
//             .reconnectWait(java.time.Duration.ofSeconds(1))
//             .maxReconnects(5)
//             .build();
            
//         this.natsConnection = Nats.connect(options);
//         LOGGER.info("Reconnected to NATS server at: " + this.natsUrl);
//     }

    
  
//     }


    

//     /*
//      *

//      * 
//      */