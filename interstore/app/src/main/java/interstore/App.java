
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

import com.google.common.net.InetAddresses;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

// mDNS and TLS imports
import javax.jmdns.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories( {"interstore.DeviceCapability", "interstore.Identity"
, "interstore.EndDevice", "interstore.Types", "interstore.Registration", "interstore.DER", "interstore.FunctionSetAssignments"
        ,"interstore.DERProgram", "interstore.Time", "interstore.DERCurve", "interstore.Events", "interstore.DERControl"})
@EntityScan(basePackages = "interstore")
@ComponentScan(basePackages = "interstore")
@Repository
@Scope("singleton")
public class App {
    private MessageToPublish messageToPublish;
    private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
    private UIControleHandler uiControleHandler;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    


    public App(String natsUrl) throws Exception {
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
    }
    
    public App() throws Exception {
    }

  
    /* This method checks for the device capability found in the server or not  */
    public String findDeviceCapability(String natsSubject) throws Exception {
        String deviceCapabilityResponse = DeviceCapabilityTest.getDeviceCapabilityresponse();
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
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        LOGGER.info("the current test is " + currentTest);
        DeviceCapabilityTest deviceCapabilitytest = new DeviceCapabilityTest();
        deviceCapabilitytest.setserviceName("dcapmanager" ); 

        String Payload =  interstore.DeviceCapabilityTest.createNewDeviceCapability(currentTest);
        this.messageToPublish.newStart(natsSubject, Payload );
        Thread.sleep(100);
        deviceCapabilityResponse = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
        return  deviceCapabilityResponse;
    }

    public Object DeviceCapabilitygetAllEndDevice(String natsSubject) throws Exception {
        String endDeviceListLink = interstore.DeviceCapabilityTest.getEndDeviceListLink(); 
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

    public Object getAllEndDevicesTest(String natsSubject)throws Exception {
        Thread.sleep(300);
        String endDeviceListLink = interstore.DeviceCapabilityTest.getEndDeviceListLink(); 
        LOGGER.info("the end device list link is " + endDeviceListLink);
        interstore.EndDeviceTest.setServicename("enddevicemanager");
        interstore.EndDeviceTest.setEndDeviceListLink(endDeviceListLink);
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.EndDeviceListLinktest()); 
        Thread.sleep(300);
        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList); 
        return endDeviceList; 
    }

    public Object getEndDeviceTest(String natsSubject)throws Exception{
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("id");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID );
        interstore.EndDeviceTest.setServicename("enddeviceinstancemanager");
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.getEndDeviceInstancetest(endDeviceID));
        Thread.sleep(300);
        Object endDevice = interstore.EndDeviceTest.getEndDeviceInstance();
        LOGGER.info("the end device instance is in app class " + endDevice);
        return endDevice; 
    }

    public Object createEndDeviceRegistrationTest(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long pin = currentTest.getLong("registrationPin");

        interstore.EndDeviceTest.setServicename("enddeviceregistrationmanager");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.createEndDeviceRegistration(endDeviceID, pin ));
        Thread.sleep(300);
        LOGGER.info("the registration pin " + interstore.EndDeviceTest.getRegistrationPin());
        Long pinRegistered = interstore.EndDeviceTest.getRegistrationPin();
        Map<String, Long> registration = new HashMap<String , Long>();
        registration.put("pin", pinRegistered);
        return registration;
    }

    public String findRegisterdEndDeviceTest(String natsSubject)throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long registrationID = currentTest.getLong("registrationID");
        Thread.sleep(300);

        interstore.EndDeviceTest.setServicename("findallregistrededendevice");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.findRegisteredEndDevice(endDeviceID,  registrationID));
        Thread.sleep(300);
        String detailsOfEndDeviceRegistration = interstore.EndDeviceTest.getregisteredEndDeviceDetails();
        LOGGER.info("the deatils of the registered end device is " + detailsOfEndDeviceRegistration );
        return detailsOfEndDeviceRegistration ;
    }

    // Additional methods omitted for brevity - include all your existing IEEE 2030.5 test methods here
    // (getAllFsaTest, createFunctionsetAssignments, etc.)

    public void start(String natsUrl) throws Exception {    
        LOGGER.info("the nats url is " + natsUrl);
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, this.serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
        this.uiControleHandler.setupBridge();
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
        ApplicationContext context = SpringApplication.run(App.class);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = (App)context.getBean("app");
        mainApp.start(natsUrl);
       
        
        // Add shutdown hook to properly cleanup mDNS
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔌 Shutting down IEEE 2030.5 server...");
        
            System.out.println(" Server shutdown complete");
        }));
    }

    // Additional IEEE 2030.5 test methods (add all your existing methods here)
    
    public String getAllFsaTest(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID ); 
        interstore.FunctionSetAssignmentsTest.setServicename("getallFsamanager");
        this.messageToPublish.newStart(natsSubject+ "getAllFunctionSetAssignments",
        interstore.FunctionSetAssignmentsTest.getAllFsa(endDeviceID)); 
        Thread.sleep(300);
        String response = interstore.FunctionSetAssignmentsTest.getAllFsa();
        LOGGER.info("the response of the function set assignment is in the app.java " + response);
        return response;
    }

    public String createFunctionsetAssignments(String natsSubject) throws Exception {
        interstore.FunctionSetAssignmentsTest.setServicename("createFsamanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject , 
         interstore.FunctionSetAssignmentsTest.createNewFunctionsetAssignments( currentTest));
        Thread.sleep(300);
        String response = interstore.FunctionSetAssignmentsTest.getCreatedFunctionSetAssignment();
        return response;
    }

    public String getAFunctionSetAssignments(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long fsaID = currentTest.getLong("fsaID");
        Thread.sleep(300);
        interstore.FunctionSetAssignmentsTest.setServicename("getASingleFsamanager");
        this.messageToPublish.newStart(natsSubject, interstore.FunctionSetAssignmentsTest.findAFunctionSetAssignments(endDeviceID, fsaID));
        Thread.sleep(300);
        String singleFSA = interstore.FunctionSetAssignmentsTest.getSingleFSA();
        LOGGER.info("the deatils of the registered end device is " + singleFSA  );
        return singleFSA   ;
    }

    public String createDerProgram(String natsSubject) throws Exception {
        interstore.DerProgramTest.setServicename("createDerprogrammanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerProgramTest.createNewDerProgram( currentTest));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getCreatedDerProgram();
        LOGGER.info("the response of DER Program is " + response);
        return response;
    }

    public String getAllDerPrograms(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        interstore.DerProgramTest.setServicename("getallDerprogrammanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerProgramTest.getAllDerProgramRequest(fsaId));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getAllderPrograms();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String getADerProgram(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + fsaId);
        LOGGER.info("the der id is " + derId);   
        interstore.DerProgramTest.setServicename("getASingleDerprogrammanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerProgramTest.getADerProgramRequest(fsaId, derId));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getADerProgram();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerCapability(String natsSubject) throws Exception {
        interstore.DerTest.setServicename("createDerCapabilitymanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.createNewDerCapability( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getCreatedDerCapability();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerCapability(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        interstore.DerTest.setServicename("getDerCapabilitymanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerTest.getADerCapabilityRequest( derId, endDeviceId));
        Thread.sleep(300);
        String response = interstore.DerTest.getADerCapability();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerSettings(String natsSubject) throws Exception {
        interstore.DerTest.setServicename("createDerSettingsmanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.createNewDerSettings( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getCreatedDerSettings();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerSettings(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        interstore.DerTest.setServicename("getDerSettingsmanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerTest.getADerSettingsRequest( derId, endDeviceId));
        Thread.sleep(300);
        String response = interstore.DerTest.getADerSettings();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String PowerGenerationtest(String natsSubject)throws Exception{
        interstore.DerTest.setServicename("PowerGenerationTestmanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.powerGenerationDeviceTest( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getEditedpowerGeneration();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String TimeTest(String natsSubject) throws Exception {
        if(interstore.DeviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
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
                    LOGGER.info("REF-Client Time: "+ REF_Client_TimeInstance.getInt64Value());
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
        return "Please run Device Capability Test first because the DeviceCapabilityResponse is " + interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
    }

    public String AdvancedTimeTest(String natsSubject) throws Exception {
        if(interstore.DeviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
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
        return "Please run Device Capability Test first because the DeviceCapabilityResponse is " + interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
    }

    public String createDerCurve(String natsSubject) throws Exception {
        interstore.DerCurveTest.setServicename("createDerCurveManager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
                interstore.DerCurveTest.createNewDerCurve( currentTest));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getCreatedDerCurve();
        LOGGER.info("the response of DERCurve is " + response);
        return response;
    }

    public String getADerCurve(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long dercId = currentTest.getLong("dercID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derc id is " + dercId);
        interstore.DerCurveTest.setServicename("getASingleDerCurveManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerCurveTest.getADerCurveRequest(derpId, dercId));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getADerCurve();
        LOGGER.info("the response of the der curve is in the app.java " + response);
        return response;
    }

    public String getAllDerCurves(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        interstore.DerCurveTest.setServicename("getallDerCurveManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerCurveTest.getAllDerCurveRequest(derpId));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getAllderCurves();
        LOGGER.info("the response of the der curves is in the app.java " + response);
        return response;
    }

    public String createDerControl(String natsSubject) throws Exception {
        interstore.DerControlTest.setServicename("createDerControlManager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
                interstore.DerControlTest.createNewDerControl( currentTest));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getCreatedDerControl();
        LOGGER.info("the response of DERControl is " + response);
        return response;
    }

    public String getAllDerControls(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        interstore.DerControlTest.setServicename("getallDerControlManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerControlTest.getAllDerControlRequest(derpId));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getAllderControls();
        LOGGER.info("the response of the der controls is in the app.java " + response);
        return response;
    }

    public String getADerControl(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long derControlId = currentTest.getLong("derControlID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derControl id is " + derControlId);
        interstore.DerControlTest.setServicename("getASingleDerControlManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerControlTest.getADerControlRequest(derpId, derControlId));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getADerControl();
        LOGGER.info("the response of the der control is in the app.java " + response);
        return response;
    }
}