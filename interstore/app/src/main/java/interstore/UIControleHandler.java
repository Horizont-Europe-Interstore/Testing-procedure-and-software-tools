package interstore;
import java.nio.charset.StandardCharsets;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import interstore.ApplicationContextProvider;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class UIControleHandler {
    private JSONObject currentTestObject;
    private Connection natsConnection;
    private  Dispatcher controleDispatcher;
    private  MessageHandler controleMessageHandler;
    private static final String responseTopic = "ControleResponse";
    private static final String instructionsTopic = "ControleInstructions";
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static Map<String, Function<String, Object>> testMap = new HashMap<>();
    private static Map<String, String> testSubjectMap = new HashMap<>();
    

    public UIControleHandler() throws Exception{

            App app = (App) ApplicationContextProvider.getApplicationContext().getBean(App.class);
       // String natsUrl = System.getenv("NATS_URL");
       String natsUrl = "nats://nats-server:4222";
        //if (natsUrl == null || natsUrl.trim().isEmpty()) {
           // natsUrl = "nats://nats-server:4222";
       // }
        this.natsConnection = Nats.connect(natsUrl);
        testMap.put("DeviceCapability", s -> {
            try { return app.CreateDeviceCapabilityTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("DeviceCapability", "dcap");

        testMap.put("GetAllEndDevices", s -> {
            try { return app.getAllEndDevicesTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAllEndDevices", "get_all_edevs");

        testMap.put("CreateEndDevice", s -> {
            try { return app.CreateEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateEndDevice", "edev");

        testMap.put("GetAnEndDevice", s -> {
            try { return app.getEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAnEndDevice", "get_an_edev");

        testMap.put("RegisterEndDevice", s -> {
            try { return app.createEndDeviceRegistrationTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("RegisterEndDevice", "register_edev");

        testMap.put("GetRegisteredEndDevice", s -> {
            try { return app.findRegisterdEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetRegisteredEndDevice", "get_registered_edev");

        testMap.put("Time", s -> {
            try { return app.TimeTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("Time", "time");

        testMap.put("AdvancedTime", s -> {
            try { return app.AdvancedTimeTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("AdvancedTime", "advanced_time");

        testMap.put("GetAllFunctionSetAssignments", s -> {
            try { return app.getAllFsaTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAllFunctionSetAssignments", "get_all_fsa");

        testMap.put("CreateFunctionSetAssignments", s -> {
            try { return app.createFunctionsetAssignments(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateFunctionSetAssignments", "create_fsa");

        testMap.put("GetAFunctionSetAssignments", s -> {
            try { return app.getAFunctionSetAssignments(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAFunctionSetAssignments", "get_fsa");

        testMap.put("CreateDerProgram", s -> {
            try { return app.createDerProgram(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateDerProgram", "create_derprogram");

        testMap.put("GetAllDerPrograms", s -> {
            try { return app.getAllDerPrograms(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAllDerPrograms", "get_all_derprograms");

        testMap.put("GetADerProgram", s -> {
            try { return app.getADerProgram(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetADerProgram", "get_a_derprogram");

        testMap.put("CreateDerCurve", s -> {
            try { return app.createDerCurve(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateDerCurve", "create_dercurve");

        testMap.put("GetAllDerCurves", s -> {
            try { return app.getAllDerCurves(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAllDerCurves", "get_all_dercurves");

        testMap.put("GetADerCurve", s -> {
            try { return app.getADerCurve(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetADerCurve", "get_a_dercurve");

        testMap.put("CreateDerControl", s -> {
            try { return app.createDerControl(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateDerControl", "create_dercontrol");

        testMap.put("GetAllDerControls", s -> {
            try { return app.getAllDerControls(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetAllDerControls", "get_all_dercontrols");

        testMap.put("GetADerControl", s -> {
            try { return app.getADerControl(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetADerControl", "get_dercontrol");

        testMap.put("CreateDerCapability", s -> {
            try { return app.createDerCapability(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateDerCapability", "create_dercapability");

        testMap.put("GetADerCapability", s -> {
            try { return app.getADerCapability(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetADerCapability", "get_dercapability");

        testMap.put("CreateDerSettings", s -> {
            try { return app.createDerSettings(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("CreateDerSettings", "create_dersettings");

        testMap.put("GetADerSettings", s -> {
            try { return app.getADerSettings(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("GetADerSettings", "get_dersettings");

        testMap.put("PowerGenerationTest", s -> {
            try { return app.PowerGenerationtest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testSubjectMap.put("PowerGenerationTest", "power_generation");
    }

    public void setupBridge(){
        this.controleMessageHandler = getControleMessageHandler();
        this.controleDispatcher = natsConnection.createDispatcher(this.controleMessageHandler);
        this.controleDispatcher.subscribe(instructionsTopic);
    }

    public MessageHandler getControleMessageHandler(){
        return msg ->{
	        this.publishMsg("Received.");
            JSONTokener parser = new JSONTokener(new String(msg.getData(), StandardCharsets.UTF_8 ));
            try{
                JSONObject testObject = new JSONObject(parser);
                if((boolean)testObject.get("args")){
                   this.setCurrentTestObject(testObject.getJSONObject("object"));
                }
                String result = (String)this.runTest((String)testObject.getString("test"));
                Thread.sleep(1000);
                System.out.println("Test result: " + result); 
                this.publishMsg(result);
                
            }
            catch(Exception exception){
                this.publishMsg("Test failed");
            }
            
        };
       }
    
    //    public void modifyExpectedResult(String expectedResult, String featureFileName){
    //         try{
    //             Path featureFilePath = Paths.get("src/test/resources/interstore/"+featureFileName+".feature");
    //             String fileContents = new String(Files.readAllBytes(featureFilePath), StandardCharsets.UTF_8);
    //             System.out.println(fileContents);
    //             String[] contentsArray = fileContents.split("\"\"\"");
    //             fileContents = contentsArray[0]+"\"\"\"\n"+expectedResult+"\n\"\"\"";
    //             Files.write(featureFilePath,fileContents.getBytes());
    //         }
    //         catch(Exception e){
    //             System.out.println(e);
    //         }

    //    }

       public void publishMsg (String msg){
        this.natsConnection.publish(responseTopic, (msg).getBytes());
       }

    //    public String[] constructCommandArgument(String testName){
    //     return 
    //     new String[]{"--glue",
    //                  "classpath:interstore/stepdefinitions",
    //                  "--plugin",
    //                  "json:build/reports/cucumber/report.json",
    //                  "./src/test/resources/interstore/"+testName+".feature"};
    //    }

       private void setCurrentTestObject(JSONObject testObject){
            this.currentTestObject = testObject;
       }

       public JSONObject getCurrentTestObject(){
        return this.currentTestObject;
       }

       public Object runTest(String testName){
           try {
               LOGGER.info("Running test for: " + testName);
               
               // Get the function for the test name
               Function<String, Object> testFunction = testMap.get(testName);
               if (testFunction == null) {
               LOGGER.warning("No test mapped for name: " + testName);
               return "No test found for name: " + testName;
               }

               // Execute the function
               Object result = testFunction.apply(testSubjectMap.get(testName));
               LOGGER.info("Test result: " + result);
               return result;
               // Create reports directory if it doesn't exist
            //    Path reportsDir = Paths.get("/app/build/reports/cucumber");
            //    Files.createDirectories(reportsDir);
               
               // Run Cucumber test with proper configuration
            //    String[] args = {
            //        "--glue", "interstore.stepdefinitions",
            //        "--plugin", "json:/app/build/reports/cucumber/report.json",
            //        "--plugin", "pretty",
            //        "/app/src/test/resources/interstore/" + testName + ".feature"
            //    };
               
            //    LOGGER.info("Cucumber args: " + String.join(" ", args));
               
               // Run Cucumber
            //    byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
            //    LOGGER.info("Cucumber exit code: " + exitCode);
               
               // Wait for report to be generated
            //    Thread.sleep(2000);
               
               // Read the Cucumber JSON report
            //    Path reportPath = Paths.get("/app/build/reports/cucumber/report.json");
            //    if (Files.exists(reportPath)) {
            //        String cucumberReport = new String(Files.readAllBytes(reportPath), StandardCharsets.UTF_8);
            //        LOGGER.info("Cucumber report generated successfully");
            //        return cucumberReport;
            //    } else {
            //        LOGGER.warning("Cucumber report not found at: " + reportPath.toString());
            //        return "[]";
            //    }
               
           } catch (Exception e) {
               LOGGER.severe("Error running test: " + e.getMessage());
               e.printStackTrace();
               return "Error executing test: " + e.getMessage();
           }
       }
//        public void runTestWithArgs(String testName){
//         io.cucumber.core.cli.Main.run(constructCommandArgument(testName));
//    }
}





/*
 * public void runTest(String testName){
            io.cucumber.core.cli.Main.run(constructCommandArgument(testName));
       }
       public void runTestWithArgs(String testName){
        io.cucumber.core.cli.Main.run(constructCommandArgument(testName));
   }
 * 
 */

 /*
  * {report: report,
    expected: expected,
    actual: actual}
  */