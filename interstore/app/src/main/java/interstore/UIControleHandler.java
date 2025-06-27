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
import java.util.logging.Logger;

public class UIControleHandler {
    private JSONObject currentTestObject;
    private Connection natsConnection;
    private  Dispatcher controleDispatcher;
    private  MessageHandler controleMessageHandler;
    private static final String responseTopic = "ControleResponse";
    private static final String instructionsTopic = "ControleInstructions";
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public UIControleHandler() throws Exception{
       // String natsUrl = System.getenv("NATS_URL");
       String natsUrl = "nats://nats-server:4222";
        //if (natsUrl == null || natsUrl.trim().isEmpty()) {
           // natsUrl = "nats://nats-server:4222";
       // }
        this.natsConnection = Nats.connect(natsUrl);
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
    
       public void modifyExpectedResult(String expectedResult, String featureFileName){
            try{
                Path featureFilePath = Paths.get("src/test/resources/interstore/"+featureFileName+".feature");
                String fileContents = new String(Files.readAllBytes(featureFilePath), StandardCharsets.UTF_8);
                System.out.println(fileContents);
                String[] contentsArray = fileContents.split("\"\"\"");
                fileContents = contentsArray[0]+"\"\"\"\n"+expectedResult+"\n\"\"\"";
                Files.write(featureFilePath,fileContents.getBytes());
            }
            catch(Exception e){
                System.out.println(e);
            }

       }

       public void publishMsg (String msg){
        this.natsConnection.publish(responseTopic, (msg).getBytes());
       }

       public String[] constructCommandArgument(String testName){
        return 
        new String[]{"--glue",
                     "classpath:interstore/stepdefinitions",
                     "--plugin",
                     "json:build/reports/cucumber/report.json",
                     "./src/test/resources/interstore/"+testName+".feature"};
       }

       private void setCurrentTestObject(JSONObject testObject){
            this.currentTestObject = testObject;
       }

       public JSONObject getCurrentTestObject(){
        return this.currentTestObject;
       }

       public Object runTest(String testName){
           try {
               LOGGER.info("Running Cucumber test for: " + testName);
               
               // Create reports directory if it doesn't exist
               Path reportsDir = Paths.get("/app/build/reports/cucumber");
               Files.createDirectories(reportsDir);
               
               // Run Cucumber test with proper configuration
               String[] args = {
                   "--glue", "interstore.stepdefinitions",
                   "--plugin", "json:/app/build/reports/cucumber/report.json",
                   "--plugin", "pretty",
                   "/app/src/test/resources/interstore/" + testName + ".feature"
               };
               
               LOGGER.info("Cucumber args: " + String.join(" ", args));
               
               // Run Cucumber
               byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
               LOGGER.info("Cucumber exit code: " + exitCode);
               
               // Wait for report to be generated
               Thread.sleep(2000);
               
               // Read the Cucumber JSON report
               Path reportPath = Paths.get("/app/build/reports/cucumber/report.json");
               if (Files.exists(reportPath)) {
                   String cucumberReport = new String(Files.readAllBytes(reportPath), StandardCharsets.UTF_8);
                   LOGGER.info("Cucumber report generated successfully");
                   return cucumberReport;
               } else {
                   LOGGER.warning("Cucumber report not found at: " + reportPath.toString());
                   return "[]";
               }
               
           } catch (Exception e) {
               LOGGER.severe("Error running Cucumber test: " + e.getMessage());
               e.printStackTrace();
               return "Error executing test: " + e.getMessage();
           }
       }
       public void runTestWithArgs(String testName){
        io.cucumber.core.cli.Main.run(constructCommandArgument(testName));
   }
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