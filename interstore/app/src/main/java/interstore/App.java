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
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication()
@EnableJpaRepositories( {"interstore.DeviceCapability", "interstore.Identity"
, "interstore.EndDevice", "interstore.Types", "interstore.Registration", "interstore.DER", "interstore.FunctionSetAssignments", "interstore.DERProgram", "interstore.Time"})
@EntityScan(basePackages = "interstore")
@ComponentScan(basePackages = "interstore")
@Repository
@Scope("singleton")
public class App {
      private MessageToPublish messageToPublish;
      private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
      private UIControleHandler uiControleHandler;
      private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public App( String natsUrl) throws Exception
    {
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
    }
    
    public App() throws Exception 
    {

    }

    public Map<String , String> postPayLoad()
    {
        Map<String, String> payload = new HashMap<>(); 
        payload.put("lfdi", "3E4F45");
        payload.put("deviceCategory", "0");
        payload.put("sfdi", "16726121139L");
        payload.put("registrationLink", "/rg");
        payload.put("functionsetAssignmentLink", "/fsa");
        payload.put("subscriptionLink", "/sub");
        payload.put("deviceStatusLink", "/dstat");
        payload.put("endDeviceListLink", "/edev");
        payload.put("derlistlink", "/der");
        return payload;
    }
  
    
    

    public Map<String, String> createRegisterEndDevice() throws Exception
    {
        
         Map<String, String> payLoad = new HashMap<>();
         payLoad.put("pin", "111115");
         return payLoad; 
        
    }

    public Map<String, String> postDeviceCapablity()
    {
        Map<String, String> payload = new HashMap<>();
       
        payload.put("mirrorUsagePointListLink", "/mup");
        payload.put("selfDeviceLink", "/sdev");
        payload.put("endDeviceListLink", "/edev");
        payload.put("timeLink", "/tm");

        return payload;
    }
    
    
    /* This method checks for the device capability found in the server or not  */

    //needs persistence
     public String findDeviceCapability(String natsSubject) throws Exception
     {
        /*DeviceCapabilitytest deviceCapabilitytest = new DeviceCapabilitytest();
        deviceCapabilitytest.setserviceName("getalldcapmanager" );
         Thread.sleep(100);*/
         String deviceCapabilityResponse = interstore.DeviceCapabilitytest.getDeviceCapabilityresponse();
         LOGGER.info("the device capability response is  " + deviceCapabilityResponse);
         return  deviceCapabilityResponse;
     }



    /*This method is to create a device capablity in the server */
    public String CreateDeviceCapabilityTest(String natsSubject) throws Exception {
        String deviceCapabilityResponse = findDeviceCapability(natsSubject);
        LOGGER.info("CreateDeviceCapability response: "+deviceCapabilityResponse);
        if(deviceCapabilityResponse != null){
            return deviceCapabilityResponse;
        }

        DeviceCapabilitytest deviceCapabilitytest = new DeviceCapabilitytest();
        deviceCapabilitytest.setserviceName("dcapmanager" ); 
        String Payload =  deviceCapabilitytest.setPostQuery(postDeviceCapablity());
        this.messageToPublish.newStart(natsSubject, Payload );
        Thread.sleep(300);
        deviceCapabilityResponse = interstore.DeviceCapabilitytest.getDeviceCapabilityresponse();
//        LOGGER.info("the device capability response is  " + deviceCapabilityResponse);
        return  deviceCapabilityResponse;
    }
   
    /*
     The test is intended to findout the list of end devices present in 
     the server .  CreateDeviceCapabilityTest("DeviceCapabilityTest"); this
     line will be removed once the tests strt from the dedicated frontend where 
     only single instance of the application runs . 
    */

    public Object DeviceCapabilitygetAllEndDevice(String natsSubject) throws Exception {
//        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
//        if(endDeviceList != null){
//            return endDeviceList;
//        }
        //String deviceCapabilityResponse = CreateDeviceCapabilityTest(natsSubject);
        String endDeviceListLink = interstore.DeviceCapabilitytest.getEndDeviceListLink(); 
        Thread.sleep(100);
        interstore.EndDeviceTest.setServicename("enddevicemanager");
        interstore.EndDeviceTest.setEndDeviceListLink(endDeviceListLink);
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.EndDeviceListLinktest()); 
        Thread.sleep(100);
        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList);
       
        return endDeviceList;
    }
    
     /* this test method is a post request to create the enddevice is there is no enddevice 
      * also for creating the new device . in the cucumber the test results of the newly created 
       * devices should be present , i mean the lfid and sfid from the front end has to stored in the 
       * cucumber . To create nultiple devcies call the below method multiple times with different 
       * payload as input .  To create an endDevcie what we need is the endevicelistlink from 
       * device capability this can be stored in the front end to make the application faster . 
      */

      public Object CreateEndDeviceTest(String natsSubject) throws Exception{
              interstore.EndDeviceTest.setServicename("createnewenddevice");
              JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
              String Payload =  interstore.EndDeviceTest.createNewEndDevice(currentTest);
              this.messageToPublish.newStart(natsSubject + "EndDevice", Payload);
              Thread.sleep(100);
              Object response = interstore.EndDeviceTest.getEndDevices();
              LOGGER.info("the created end device with sfdi and lfdi " + response);
             return response;
      }

    
     /*
       This returns all enddevices listed on the server  , the original release
       there is no need to create the end device below , it will created before
       so that part can be eliminated
     */
     public Object getAllEndDevicesTest(String natsSubject)throws Exception {
        Thread.sleep(300);

        // impliment a different logic for get all end device . ///
        String endDeviceListLink = interstore.DeviceCapabilitytest.getEndDeviceListLink();
        interstore.EndDeviceTest.setServicename("enddevicemanager");
        interstore.EndDeviceTest.setEndDeviceListLink(endDeviceListLink);
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.EndDeviceListLinktest()); 
        Thread.sleep(300);
        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList); 
        return endDeviceList; 
     }
      /*
       * based on the list of the end devices got back the user able to select the device that 
        he wants to test , so the user must choose one from the response , there is way with 
        every enddevice there should associated with /edev/{id} so from among the list of 
        endevices there needs to pick one and to call one . pick the lfdi or sfdi 
        get sfdi from the response and this sfdi should shoot in server and get 
        the corresponding url /enddevice and return it back and shoot this url aganist the 
        server to get all it's attributes .  a particular end device 
       *  time delay issue pay attention we may need to call the below function twice 
       * to sync with the time  
       */
      
    public Object getEndDeviceTest(String natsSubject)throws Exception{
        getAllEndDevicesTest("getAllEndDevicesTest");
        Thread.sleep(300);
        interstore.EndDeviceTest.setServicename("enddeviceinstancemanager"); // enddeviceinstancemanager
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        String Payload =  interstore.EndDeviceTest.createNewEndDevice(currentTest);
        LOGGER.info("the sfdi payload is " + Payload); 
        interstore.EndDeviceTest.setsfdi( Payload );  // needs to supply an sfdi 
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.getEndDeviceInstancetest());
        Thread.sleep(300);
        Object endDevice = interstore.EndDeviceTest.getEndDeviceInstance();
        LOGGER.info("the end device instance is " + endDevice); 
       return endDevice; 
    }

    
    /*
     first needs to check that the device has registered or not if it registered
     this will return  pin as the attribute if not it returns device not registered message
    */
    
     public Long createEndDeviceRegistrationTest(String natsSubject) throws Exception
     {
        Object response = getEndDeviceTest("EndDevice");
        Thread.sleep(300);
        interstore.EndDeviceTest.getRegisteredEndDevice(response.toString()); 
        String registrationLink = interstore.EndDeviceTest.getRegistrationLink();
        Map<String, String> payload = createRegisterEndDevice();
        payload.put("registrationLink", registrationLink);
        interstore.EndDeviceTest.setServicename("enddeviceregistrationmanager");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.createEndDeviceRegistration(payload));
        Thread.sleep(300);
       // LOGGER.info("the registration pin " + interstore.EndDeviceTest.getRegistrationPin());
       // LOGGER.info("the registration link is " + interstore.EndDeviceTest.getEndDeviceregisteredwithId());
        return interstore.EndDeviceTest.getRegistrationPin();

     }

    

     public String findRegisterdEndDeviceTest(String natsSubject)throws Exception
     {
        Long rgPin  = createEndDeviceRegistrationTest("CreateaRegisteredEndDevice");
        LOGGER.info("the pin is  rttt" + rgPin);
        Thread.sleep(300);
        LOGGER.info("the end device registerd link is " + interstore.EndDeviceTest.getEndDeviceregisteredwithId());
       // String  interstore.EndDeviceTest.getEndDeviceregisteredwithId();
        interstore.EndDeviceTest.setServicename("findallregistrededendevice");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.findRegisteredEndDevice());
        Thread.sleep(300);
        String detailsOfEndDeviceRegistration = interstore.EndDeviceTest.getregisteredEndDeviceDetails();
        LOGGER.info("the deatils of the registered end device is " + detailsOfEndDeviceRegistration );
        return detailsOfEndDeviceRegistration ;
     }

    public String TimeTest(String natsSubject) throws Exception {

        String response = CreateDeviceCapabilityTest("Device_Capability");
        LOGGER.info("the device capability response is  " + response);
        Thread.sleep(300);
        String timeLink = interstore.TimeTest.getTimeLink(response);
        LOGGER.info("the timelink response is  " + timeLink);
        interstore.TimeTest.setserviceName("timemanager");
        this.messageToPublish.newStart(natsSubject, interstore.TimeTest.getTimeQuery(timeLink));
        Thread.sleep(300);
        String timeListResponse = interstore.TimeTest.getTimeResponse();

        timeListResponse = timeListResponse.substring(1, timeListResponse.length() - 1);
        timeListResponse = timeListResponse.replace("\\", "");
        JSONObject object = new JSONObject(timeListResponse);
        String timeInstance = object.getString("time_instance");
        String quality = object.getString("quality");
        Thread.sleep(500);
        Instant instant = Instant.now();
        long currentTime =  instant.getEpochSecond();
        TimeType REF_Client_TimeInstance = new TimeType(currentTime);
        if (quality!=null && timeInstance!=null) {
            if(quality.equals("7")){
                LOGGER.info("Quality metric matched with Client with value: "+quality);
                LOGGER.info("REF-Client Time: "+ REF_Client_TimeInstance.getValue());
                LOGGER.info("Synchronized REF-Client Time: " + timeInstance);
                return "Synchronized the REF-Client Time with the Device Capability";
            }
            else {
                LOGGER.info("Wrong quality metric value of "+quality);
                return "Wrong quality metric value of "+quality;
            }
        } else {
            LOGGER.info("Quality value not found.");
            return "Quality value not found.";
        }
    }

    public String AdvancedTimeTest(String natsSubject) throws Exception {

        String response = CreateDeviceCapabilityTest("Device_Capability");
        LOGGER.info("the device capability response is  " + response);
        String timeLink = interstore.TimeTest.getTimeLink(response);
        LOGGER.info("the timelink response is  " + timeLink);
        interstore.TimeTest.setserviceName("timemanager");
        this.messageToPublish.newStart(natsSubject+"_Get_Time", interstore.TimeTest.getTimeQuery(timeLink));
        Thread.sleep(300);
        String timeListResponse = interstore.TimeTest.getTimeResponse();
        timeListResponse = timeListResponse.substring(1, timeListResponse.length() - 1);
        timeListResponse = timeListResponse.replace("\\", "");
        JSONObject object = new JSONObject(timeListResponse);
        String timeInstance = object.getString("time_instance");
        if (timeInstance!=null) {
            long updatedTime = Long.parseLong(timeInstance) + 3600;
            JSONObject payload = new JSONObject();
            payload.put("updated_time_instance", updatedTime);
            payload.put("timeLink", timeLink);
            interstore.TimeTest.setserviceName("advancedtimemanager");
            this.messageToPublish.newStart(natsSubject+"_update_time", interstore.TimeTest.updateTimeQuery(payload.toString()));
            Thread.sleep(1000);
            interstore.TimeTest.setserviceName("timemanager");
            this.messageToPublish.newStart(natsSubject+"_validate_updated_time", interstore.TimeTest.getTimeQuery(timeLink));
            Thread.sleep(100);
            return "The Time resource was updated successfully by 1 hour.";
        } else {
            System.out.println("Time Instance value not found.");
            return "Time Instance value not found.";
        }
    }

    public String FunctionSetAssignmentTest (String natsSubject) throws Exception
    {
        if(interstore.EndDeviceTest.getEndDeviceListLink() != null && interstore.DeviceCapabilitytest.getEndDeviceListLink() != null){
            String response = findRegisterdEndDeviceTest("RegisteredEndDevice");
            List<Integer> values = interstore.FunctionSetAssignmentTest.getPin(response);
            int regID = values.get(0);
            int pin = values.get(1);
            interstore.FunctionSetAssignmentTest.setServicename("fsalistmanager");
            this.messageToPublish.newStart(natsSubject+ "_FSAListLink",
                    interstore.FunctionSetAssignmentTest.getFSAListQuery(String.valueOf(pin), Integer.toUnsignedLong(regID)));
            Thread.sleep(100);
            interstore.FunctionSetAssignmentTest.setServicename("fsamanager");
            this.messageToPublish.newStart(natsSubject+ "_FSAInstances",
                    interstore.FunctionSetAssignmentTest.getFSAQuery(interstore.FunctionSetAssignmentTest.fsaIds));
            Thread.sleep(100);
//        List<String> derpLinks = functionSetAssignmentTest.getDerpLinks();
            interstore.FunctionSetAssignmentTest.setDERPListLinks();
            interstore.FunctionSetAssignmentTest.setServicename("derprogrammanager");
            this.messageToPublish.newStart(natsSubject+ "_DERPrograms",
                    interstore.FunctionSetAssignmentTest.getDERProgramQuery(FunctionSetAssignmentTest.derpListLinks));
            Thread.sleep(100);
//            return interstore.FunctionSetAssignmentTest.getDerProgramInstance();
            return "Found DERPrograms instances";
        }
        return "No EndDevices found";

//        FunctionSetAssignmentTest functionSetAssignmentTest = new FunctionSetAssignmentTest();
//

    }

    public void start(String natsUrl) throws Exception
    {    
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, this.serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
        this.uiControleHandler.setupBridge();
//        FunctionSetAssignmentTest("FSA-Test");
        //findDeviceCapability("Adam");
        // CreateDeviceCapabilityTest("vinay"); // returns the device capability in the server . 
          // DeviceCapabilitygetAllEndDevice("enddevicemanager"); 
          // CreateEndDeviceTest("Nithin");
           //this.multipleEndDeviceCreateTest();
//         getAllEndDevicesTest("Nithin");
          // getEndDeviceTest("EndDeviceInstanceTest");
//         createEndDeviceRegistrationTest("RegistrationLink");
       //findRegisterdEndDeviceTest("RegisteredEndDevice");
         // this.EndDeviceTest("enddevicemanager"); // returns all enddevices present in the server
//       this.EndDeviceRegistrationTest("enddeviceregistration"); // return particular enddevice with it's registration link
       // Thread.sleep(100);
        //this.messageToPublish.closeConnection();
        // needs to write a new test case for returnig the attribute of particular end device registered the output is this 
          // { "registered PIN": "111112" } 
              //this.DeviceCapblityendDeviceTest("enddevicemanager"); 

       // LOGGER.info("Publisher connection closed");
          
    }
   

  
   
    public static void main(String[] args) throws Exception {
        String natsUrl = System.getenv("NATS_URL");
        //String natsUrl = "nats://localhost:4222"; 
        ApplicationContext context = SpringApplication.run(App.class);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = (App)context.getBean("app");
        mainApp.start(natsUrl);

    }
    
}
       



/*
 *
     export NATS_URL=nats://localhost:4222 
 *
 */
