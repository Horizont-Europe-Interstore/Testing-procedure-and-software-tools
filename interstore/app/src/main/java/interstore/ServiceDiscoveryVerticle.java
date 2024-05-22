package interstore;
import io.vertx.core.AbstractVerticle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.core.JsonProcessingException; 


public class ServiceDiscoveryVerticle extends AbstractVerticle {
    private MicroServiceFactory microServiceFactory; 
    private  Connection natsConnection;
    private  Dispatcher dispatcher;
    private  MessageHandler messageHandler;
    public  MessageToPublish messageToPublish;  

    private String natsUrl; 
    public String natsMatter; 
    public String serviceName;
    private static final Logger LOGGER = Logger.getLogger(ServiceDiscoveryVerticle.class.getName());

    public ServiceDiscoveryVerticle(String natsUrl) throws Exception{  
        this.natsUrl = natsUrl; 
        this.natsConnection = Nats.connect(natsUrl);
        this.microServiceFactory = new MicroServiceFactory(); 
        this.messageToPublish = new MessageToPublish(); 
        this.serviceName = null; 
       }

       public ServiceDiscoveryVerticle() throws Exception{  
        this.natsUrl = System.getenv("NATS_URL"); 
        this.natsConnection = Nats.connect(natsUrl);
        this.microServiceFactory = new MicroServiceFactory(); 
        this.messageToPublish = new MessageToPublish(); 
        this.serviceName = null; 
       }

       @PostConstruct
       public void postConstruct(){

       }
     
    public MessageHandler getMessageHandler(String natssubject)
       {
         return msg -> {
               String messageContent = new String(msg.getData(), StandardCharsets.UTF_8 );
               LOGGER.info("Received message from NATS" + messageContent);
               try {
                this.vertxSetUp(messageContent);
            } catch (JsonProcessingException | JSONException e) {
                
                e.printStackTrace();
            } 
           };
       }

       public Dispatcher subscribeFromNats(String natsSubject)
       {

         this.dispatcher.subscribe(natsSubject);
         LOGGER.info("Dispatcher created and subscribed to subject: " + natsSubject);
         return this.dispatcher ; 
       }
       public void setupBridge(String natsSubject) 
       {   
           this.natsMatter(natsSubject); 
           this.messageHandler = getMessageHandler(natsSubject);
           this.dispatcher = natsConnection.createDispatcher(this.messageHandler);
           subscribeFromNats(natsSubject); 
          
       }
        
       public void natsMatter(String natsSubject)
       {
          this.natsMatter =  "\"" + natsSubject + "\"";
       }
       public String getNatsMatter()
       {
         return this.natsMatter ; 
       }

        public MessageHandler responseMessageHandler(String serviceName)
       {
           return msg -> {
               String messageContent = new String(msg.getData(), StandardCharsets.UTF_8);
               this.messageToPublish.responseToSender(serviceName, messageContent); 
           };
       }
       
       public void setupBridgeForResponse(String natsSubject , String serviceName)
       {     
           this.dispatcher = natsConnection.createDispatcher(this.responseMessageHandler(serviceName));
           subscribeFromNats(natsSubject);
           
       }

       public void setServiceName(String serviceName)
       {
          this.serviceName =   serviceName ;    
       }

       public String getServiceName()
       {
           return this.serviceName;
       } 

       public String extractService(String natsmsg) throws JSONException
       {
            LOGGER.info(natsmsg);
            JSONTokener parser = new JSONTokener(natsmsg);
            JSONObject msg = new JSONObject(parser);
           String serviceName = msg.getString("servicename");
           setServiceName(serviceName);
           return serviceName;
       }

      


    public Object getMicroservice(Map<String, Object> microservice, String serviceName)
    {
        Object serviceObject = microservice.get(serviceName); 
        return serviceObject; 
    }
    

    public void vertxSetUp(String natsmsg) throws JsonProcessingException, JSONException
    {
        
         Object microServiceObject = getMicroservice(this.microServiceFactory.getMicroservices(), extractService(natsmsg)); 
       
         try {
                Class<?> microServiceClass = microServiceObject.getClass();
                Method method = microServiceClass.getMethod("chooseMethod_basedOnAction", String.class);
                Object response = method.invoke(microServiceObject, natsmsg);
                this.sendResponseToNats( natsmsg,response);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                 LOGGER.warning("Error invoking method: " + e.getMessage());   
             } 
            
            }
    
    
    public void sendResponseToNats(String natsSubject ,Object response) throws JsonProcessingException {
               ObjectMapper objectMapper = new ObjectMapper();
                String responseMessage = objectMapper.writeValueAsString(response);
                //String responseMessage = response.toString();
                //LOGGER.info("Response message in  service verticle : " + responseMessage);
                this.messageToPublish.reSubscribeMessage(getNatsMatter(), this.natsUrl , getServiceName()); 
                // Use  NATS connection to publish the response
                natsConnection.publish(getNatsMatter(), responseMessage.getBytes(StandardCharsets.UTF_8));
              
            }

    
  
    }


    