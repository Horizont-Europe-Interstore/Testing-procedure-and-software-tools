package interstore;
import java.nio.charset.StandardCharsets;
import io.nats.client.MessageHandler;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import interstore.Types.TestRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@RestController()
@RequestMapping("/api")
public class UIControleHandler {
    private final App app;
    private JSONObject currentTestObject;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static Map<String, Function<JSONObject, Object>> testMap = new HashMap<>();
    
    public UIControleHandler(App app) throws Exception{
        this.app = app;
        testMap.put("DeviceCapability", s -> {
            try { return app.CreateDeviceCapabilityTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAllEndDevices", s -> {
            try { return app.getAllEndDevicesTest(); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateEndDevice", s -> {
            try { return app.CreateEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAnEndDevice", s -> {
            try { return app.getEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("RegisterEndDevice", s -> {
            try { return app.createEndDeviceRegistrationTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetRegisteredEndDevice", s -> {
            try { return app.findRegisterdEndDeviceTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("Time", s -> {
            try { return app.TimeTest(); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("AdvancedTime", s -> {
            try { return app.AdvancedTimeTest(); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAllFunctionSetAssignments", s -> {
            try { return app.getAllFsaTest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateFunctionSetAssignments", s -> {
            try { return app.createFunctionsetAssignments(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAFunctionSetAssignments", s -> {
            try { return app.getAFunctionSetAssignments(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateDerProgram", s -> {
            try { return app.createDerProgram(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAllDerPrograms", s -> {
            try { return app.getAllDerPrograms(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetADerProgram", s -> {
            try { return app.getADerProgram(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateDerCurve", s -> {
            try { return app.createDerCurve(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAllDerCurves", s -> {
            try { return app.getAllDerCurves(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetADerCurve", s -> {
            try { return app.getADerCurve(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateDerControl", s -> {
            try { return app.createDerControl(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetAllDerControls", s -> {
            try { return app.getAllDerControls(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetADerControl", s -> {
            try { return app.getADerControl(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("CreateDerCapability", s -> {
            try { return app.createDerCapability(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetADerCapability", s -> {
            try { return app.getADerCapability(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });


        testMap.put("CreateDerSettings", s -> {
            try { return app.createDerSettings(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("GetADerSettings", s -> {
            try { return app.getADerSettings(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });

        testMap.put("PowerGenerationTest", s -> {
            try { return app.PowerGenerationtest(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testMap.put("CreateDer", s -> {
            try { return app.createDer(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
        testMap.put("GetDer", s -> {
            try { return app.getADer(s); }
            catch(Exception e){ throw new RuntimeException(e); }
        });
    }

    @PostMapping
    public ResponseEntity<?> handleTest(@RequestBody TestRequest request) {
        Map<String, Object> response = new HashMap<>();
        String test = request.getTest().replaceAll(" ", "");
        try{
            System.out.println("Received test: " + test);
            System.out.println("Parameters: " + request.getObject());
            JSONObject currentTest = new JSONObject(request.getObject()); 
            setCurrentTestObject(currentTest);
            String result = (String)this.runTest(test);
            Thread.sleep(1000);
            response.put("Feature", test);
            response.put("Tag", "@" + String.join("", Arrays.asList(request.getTest().split(" ")).subList(0, Math.min(2, request.getTest().split(" ").length))));
            response.put("End result","passed");
            response.put("Description", "Test executed successfully");
            response.put("Actual response", result);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            System.out.println(e);
        }
        response.put("Feature", test);
        response.put("Tag", "@" + String.join("", Arrays.asList(request.getTest().split(" ")).subList(0, Math.min(2, request.getTest().split(" ").length))));
        response.put("End result","failed");
        response.put("Description", "Test execution failed!");
        response.put("Actual response", null);
        return ResponseEntity.ok(response);
    }

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
               Function<JSONObject, Object> testFunction = testMap.get(testName);
               if (testFunction == null) {
               LOGGER.warning("No test mapped for name: " + testName);
               return "No test found for name: " + testName;
               }

               JSONObject currentTest = getCurrentTestObject();

               // Execute the function
               Object result = testFunction.apply(currentTest);
               LOGGER.info("Test result: " + result);
               return result;
               
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