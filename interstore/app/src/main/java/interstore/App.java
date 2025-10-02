
package interstore;
import interstore.Types.TimeType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.EnableScheduling;

// mDNS and TLS imports
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories( {"interstore.DeviceCapability", "interstore.Identity"
, "interstore.EndDevice", "interstore.Types", "interstore.Registration", "interstore.DER", "interstore.FunctionSetAssignments"
        ,"interstore.DERProgram", "interstore.Time", "interstore.DERCurve", "interstore.Events", "interstore.DERControl", "interstore.TestResults"})
@EntityScan(basePackages = "interstore")
@ComponentScan(basePackages = "interstore")
@Repository
@Scope("singleton")
public class App {

    // @Autowired
    // private UIControleHandler uiControleHandler;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    @Autowired
    private DeviceCapabilityTest deviceCapabilityTest;
    @Autowired
    private EndDeviceTest endDeviceTest;
    @Autowired
    private TimeTest timeTest;
    @Autowired
    private FunctionSetAssignmentsTest functionSetAssignmentsTest;
    @Autowired
    private DerProgramTest derProgramTest;
    @Autowired
    private DerTest derTest;
    @Autowired
    private DerCurveTest derCurveTest;
    @Autowired
    private DerControlTest derControlTest;

    


    public App(String natsUrl) throws Exception {

    }
    
    public App() throws Exception {
    }

  
    /* This method checks for the device capability found in the server or not  */
    public String findDeviceCapability() throws Exception {
        String deviceCapabilityResponse = deviceCapabilityTest.getDeviceCapabilityresponse();
        LOGGER.info("the device capability response is  " + deviceCapabilityResponse);
        return  deviceCapabilityResponse;
    }

    /*This method is to create a device capablity in the server */
    public String CreateDeviceCapabilityTest(JSONObject currentTest) throws Exception {
        String deviceCapabilityResponse = findDeviceCapability();
        LOGGER.info("CreateDeviceCapability response: "+deviceCapabilityResponse);
        if(deviceCapabilityResponse != null){
            return deviceCapabilityResponse;
        }
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        LOGGER.info("the current test is " + currentTest);
        deviceCapabilityTest.createNewDeviceCapability(currentTest);
        Thread.sleep(100);
        deviceCapabilityResponse = deviceCapabilityTest.getDeviceCapabilityresponse();
        return  deviceCapabilityResponse;
    }

    public Object CreateEndDeviceTest(JSONObject currentTest) throws Exception{

        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        endDeviceTest.createNewEndDevice(currentTest);
        Object response = endDeviceTest.getEndDevices();
        LOGGER.info("the created end device with sfdi and lfdi " + response);
        return response;
    }

    public Object getAllEndDevicesTest() throws Exception {
        Thread.sleep(300);
        String endDeviceListLink = interstore.DeviceCapabilityTest.getEndDeviceListLink(); 
        LOGGER.info("the end device list link is " + endDeviceListLink);
        endDeviceTest.setEndDeviceListLink(endDeviceListLink);
        endDeviceTest.EndDeviceListLinktest();
        Object endDeviceList = endDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList); 
        return endDeviceList; 
    }

    public Object getEndDeviceTest(JSONObject currentTest) throws Exception{
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("id");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID );
        endDeviceTest.getEndDeviceInstancetest(endDeviceID);
        Object endDevice = endDeviceTest.getEndDeviceInstance();
        LOGGER.info("the end device instance is in app class " + endDevice);
        return endDevice; 
    }

    public Object createEndDeviceRegistrationTest(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long pin = currentTest.getLong("registrationPin");
        endDeviceTest.createEndDeviceRegistration(endDeviceID, pin );
        LOGGER.info("the registration pin " + endDeviceTest.getRegistrationPin());
        Long pinRegistered = endDeviceTest.getRegistrationPin();
        Map<String, Long> registration = new HashMap<String , Long>();
        registration.put("pin", pinRegistered);
        String jsonResponse = new ObjectMapper().writeValueAsString(registration);
        return jsonResponse;
    }

    public String findRegisterdEndDeviceTest(JSONObject currentTest)throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long registrationID = currentTest.getLong("registrationID");
        endDeviceTest.findRegisteredEndDevice(endDeviceID,  registrationID);
        String detailsOfEndDeviceRegistration = endDeviceTest.getregisteredEndDeviceDetails();
        LOGGER.info("the deatils of the registered end device is " + detailsOfEndDeviceRegistration );
        return detailsOfEndDeviceRegistration ;
    }

    // Additional methods omitted for brevity - include all your existing IEEE 2030.5 test methods here
    // (getAllFsaTest, createFunctionsetAssignments, etc.)

    public void start(String natsUrl) throws Exception {    
        LOGGER.info("the nats url is " + natsUrl);
        NatsSubscriber subscriber = new NatsSubscriber(natsUrl);
        subscriber.subscribe("response.client");

        // Keep application running or integrate into your main loop
        Thread.currentThread().join();
        
        // Start IEEE 2030.5 mDNS service discovery
       
        
        LOGGER.info(" Everything is initialized including IEEE 2030.5 Windows discovery");
    }

   
   
    public static void main(String[] args) throws Exception {
        String natsUrl = "nats://nats-server:4222";
        //System.setProperty("server.port", "1900");
        //System.setProperty("server.address", "10.40.160.10");
        ApplicationContext context = SpringApplication.run(App.class, args);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = context.getBean(App.class);
        // App mainApp = (App)context.getBean("app");
        System.out.println("🚀 Application started with JMS and WebSocket support");
        mainApp.start(natsUrl);
       
        
        // Add shutdown hook to properly cleanup mDNS
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔌 Shutting down IEEE 2030.5 server...");
        
            System.out.println(" Server shutdown complete");
        }));
    }

    // Additional IEEE 2030.5 test methods (add all your existing methods here)
    
    public String getAllFsaTest(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID ); 
        functionSetAssignmentsTest.getAllFsa(endDeviceID);
        String response = functionSetAssignmentsTest.getAllFsa();
        LOGGER.info("the response of the function set assignment is in the app.java " + response);
        return response;
    }

    public String createFunctionsetAssignments(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        functionSetAssignmentsTest.createNewFunctionsetAssignments(currentTest);
        String response = functionSetAssignmentsTest.getCreatedFunctionSetAssignment();
        return response;
    }

    public String getAFunctionSetAssignments(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long fsaID = currentTest.getLong("fsaID");
        functionSetAssignmentsTest.findAFunctionSetAssignments(endDeviceID, fsaID);
        String singleFSA = functionSetAssignmentsTest.getSingleFSA();
        LOGGER.info("the deatils of the registered end device is " + singleFSA  );
        return singleFSA;
    }

    public String createDerProgram(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derProgramTest.createNewDerProgram(currentTest);
        String response = derProgramTest.getCreatedDerProgram();
        LOGGER.info("the response of DER Program is " + response);
        return response;
    }

    public String getAllDerPrograms(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        derProgramTest.getAllDerProgramRequest(fsaId);
        String response = derProgramTest.getAllderPrograms();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String getADerProgram(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + fsaId);
        LOGGER.info("the der id is " + derId);   
        derProgramTest.getADerProgramRequest(fsaId, derId);
        String response = derProgramTest.getADerProgram();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerCapability(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derTest.createNewDerCapability(currentTest);
        String response = derTest.getCreatedDerCapability();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerCapability(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        derTest.getADerCapabilityRequest( derId, endDeviceId);
        String response = derTest.getADerCapability();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerSettings(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derTest.createNewDerSettings(currentTest);
        String response = derTest.getCreatedDerSettings();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerSettings(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        derTest.getADerSettingsRequest( derId, endDeviceId);
        String response = derTest.getADerSettings();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String PowerGenerationtest(JSONObject currentTest)throws Exception{
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derTest.powerGenerationDeviceTest(currentTest);
        String response = derTest.getEditedpowerGeneration();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String TimeTest() throws Exception {
        JSONObject responseJson = new JSONObject();
        String msg;
        if(deviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = deviceCapabilityTest.getDeviceCapabilityresponse();
            LOGGER.info("the device capability response is  " + response);
            Thread.sleep(300);
            String timeLink = timeTest.getTimeLink(response);
            LOGGER.info("the timelink response is  " + timeLink);
            timeTest.getTimeQuery(timeLink);
            String timeListResponse = timeTest.getTimeResponse();
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
                    LOGGER.info("REF-Client Time: "+ REF_Client_TimeInstance.getInt64Value());
                    LOGGER.info("Synchronized REF-Client Time: " + timeInstance);
                    msg = "Synchronized the REF-Client Time with the Device Capability";
                    responseJson.put("message", msg);
                     return responseJson.toString(); 
                }
                else {
                    LOGGER.info("Wrong quality metric value of "+quality);
                    msg = "Wrong quality metric value of "+quality;
                    responseJson.put("message", msg);
                    return responseJson.toString();
                }
            } else {
                LOGGER.info("Quality value not found.");
                msg = "Quality value not found.";
                responseJson.put("message", msg);
                return responseJson.toString();
            }
        }
        msg = "Please run Device Capability Test first because the DeviceCapabilityResponse is " + deviceCapabilityTest.getDeviceCapabilityresponse();
        responseJson.put("message", msg);
        return responseJson.toString();
    }

    public String AdvancedTimeTest() throws Exception {
        JSONObject responseJson = new JSONObject();
        String msg;
        if(deviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = deviceCapabilityTest.getDeviceCapabilityresponse();
            LOGGER.info("the device capability response is  " + response);
            String timeLink = timeTest.getTimeLink(response);
            LOGGER.info("the timelink response is  " + timeLink);
            timeTest.getTimeQuery(timeLink);
            String timeListResponse = timeTest.getTimeResponse();
            JSONObject object = new JSONObject(timeListResponse);
            String timeInstance = object.getString("time_instance");
            if (timeInstance!=null) {
                long updatedTime = Long.parseLong(timeInstance) + 3600;
                JSONObject payload = new JSONObject();
                payload.put("updated_time_instance", updatedTime);
                payload.put("timeLink", timeLink);
                timeTest.updateTimeQuery(payload.toString());
                timeTest.getTimeQuery(timeLink);
                msg = "The Time resource was updated successfully by 1 hour.";
                responseJson.put("message", msg);
                return responseJson.toString(); 
            } else {
                System.out.println("Time Instance value not found.");
                msg = "Time Instance value not found.";
                responseJson.put("message", msg);
                return responseJson.toString();
            }
        }
        msg = "Please run Device Capability Test first because the DeviceCapabilityResponse is " + deviceCapabilityTest.getDeviceCapabilityresponse();
        responseJson.put("message", msg);
        return responseJson.toString();
    }

    public String createDerCurve(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derCurveTest.createNewDerCurve(currentTest);
        String response = derCurveTest.getCreatedDerCurve();
        LOGGER.info("the response of DERCurve is " + response);
        return response;
    }

    public String getADerCurve(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long dercId = currentTest.getLong("dercID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derc id is " + dercId);
        derCurveTest.getADerCurveRequest(derpId, dercId);
        String response = derCurveTest.getADerCurve();
        LOGGER.info("the response of the der curve is in the app.java " + response);
        return response;
    }

    public String getAllDerCurves(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        derCurveTest.getAllDerCurveRequest(derpId);
        String response = derCurveTest.getAllderCurves();
        LOGGER.info("the response of the der curves is in the app.java " + response);
        return response;
    }

    public String createDerControl(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        derControlTest.createNewDerControl(currentTest);
        String response = derControlTest.getCreatedDerControl();
        LOGGER.info("the response of DERControl is " + response);
        return response;
    }

    public String getAllDerControls(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        derControlTest.getAllDerControlRequest(derpId);
        String response = derControlTest.getAllderControls();
        LOGGER.info("the response of the der controls is in the app.java " + response);
        return response;
    }

    public String getADerControl(JSONObject currentTest) throws Exception {
        // JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long derControlId = currentTest.getLong("derControlID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derControl id is " + derControlId);
        derControlTest.getADerControlRequest(derpId, derControlId);
        String response = derControlTest.getADerControl();
        LOGGER.info("the response of the der control is in the app.java " + response);
        return response;
    }
}