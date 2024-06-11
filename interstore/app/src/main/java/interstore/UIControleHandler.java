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


public class UIControleHandler {
    private JSONObject currentTestObject;
    private Connection natsConnection;
    private  Dispatcher controleDispatcher;
    private  MessageHandler controleMessageHandler;
    private static final String responseTopic = "ControleResponse";
    private static final String instructionsTopic = "ControleInstructions";

    public UIControleHandler() throws Exception{
        this.natsConnection = Nats.connect(System.getenv("NATS_URL"));
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
                this.runTest((String)testObject.getString("test"));
                Thread.sleep(2000);
                Path reportPath = Paths.get("build/reports/cucumber/report.json").toAbsolutePath();
                String file = new String(Files.readAllBytes(reportPath), StandardCharsets.UTF_8);
                System.out.println(file); 
                this.publishMsg(file);
                
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
           return  io.cucumber.core.cli.Main.run(constructCommandArgument(testName));
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