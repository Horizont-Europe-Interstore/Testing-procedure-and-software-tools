
module.exports = class NatsInterface{
    static #dotenvConfig = require('dotenv').config();
    #processTimeout;
    static #processMaxTime=30000;
    static #controleChannel = 'ControleInstructions';
    static #responseChannel = 'ControleResponse';
    static #NATS = require("nats");
    static #decoder = NatsInterface.#NATS.StringCodec().decode;
    constructor(){}

    handleSetTest (testObject, res) {
      let testName = testObject.test;
      testObject.test = testObject.test.replace(/ /g,'');
      
      console.log('EXPRESS-MID: Received request for test:', testName);
      console.log('EXPRESS-MID: Request payload:', JSON.stringify(testObject));
      console.log('EXPRESS-MID: NATS_URL:', process.env.NATS_URL);

      this.#processTimeout = setTimeout(()=>{
        console.log('EXPRESS-MID: Test timed out after', NatsInterface.#processMaxTime, 'ms');
        res.status(400)
           .json(this.#generateErrorReport("Test Timed Out", testName));
      },NatsInterface.#processMaxTime);

      NatsInterface.#NATS.connect({servers:process.env.NATS_URL})
          .then(async (connection)=>{
            console.log('EXPRESS-MID: Connected to NATS server');
            console.log('EXPRESS-MID: Publishing to channel:', NatsInterface.#controleChannel);
            
            connection.publish(NatsInterface.#controleChannel, JSON.stringify(testObject));
            console.log('EXPRESS-MID: Message published to NATS');
            
            console.log('EXPRESS-MID: Subscribing to response channel:', NatsInterface.#responseChannel);
            const sub = connection.subscribe(NatsInterface.#responseChannel);
            
            await this.#sendMsgs(res,testName,sub).then(()=>{
              console.log('EXPRESS-MID: Closing NATS connection');
              connection.close();
            });
          })
          .catch(err=>{
              console.error('EXPRESS-MID: NATS connection error:', err);
              res.status(400)
                 .json(this.#generateErrorReport('Exception when getting response from backend\n'+err, 
                                                 testName));
              clearTimeout(this.#processTimeout);
          });
    }

    #generateErrorReport(error, testName){
        return {
          Feature: testName,
          Tag: '@'+testName.split(' ').slice(0,2).join(''),
          Scenario: '...',
          'End result': 'failed',
          'Description': error,
          'Expected response': 'none',
          'Actual response': 'none'
        };
    }

    #generateReport(fileContents,testName){  
        if(fileContents.length===0){
          return this.#generateErrorReport("Test does not yet exist",testName)
        }
        let report = {
          'Feature':fileContents[0].name,
          'Tag':fileContents[0].elements[0].tags[0].name,
          'Scenario' : fileContents[0].elements[0].name
        };
        
        try{
          let steps = fileContents[0].elements[0].steps;
          let last = steps[steps.length-1];
          let description = last.doc_string.value.replace(/(\\\"|\\n)/g,'');
          let outputarray = last.output;
          let actual = outputarray !== undefined ? outputarray.find(x=>x.slice(0,6) === "actual").split("actual")[1]: "No actual response for this test";
          let expected = outputarray !== undefined ? outputarray.find(x=>x.slice(0,8) === "expected").split("expected")[1]: "No expected response for this test";
          report["End result"] = last.result.status;
          report["Actual response"] = actual;
          report["Expected response"] = expected;
          report["Description"] = description;
          return report;
        }
        catch(error){
          clearTimeout(this.#processTimeout);
          throw error;
        }
      }

       #generatePlainReport(jsonData,testName){
          if(jsonData.length===0){
          return this.#generateErrorReport("Test does not yet exist",testName)
          }
          let report = {
          'Actual response': jsonData,
          'Feature': testName,
          'End result': 'passed',
          'Description': 'Test executed with expected responses from step definitions'
          };
          return report;
       }

      #pingBackend(){

      }

      #restoreBackend(){

      }

      async #sendMsgs(res,testName,sub) {
        console.log('EXPRESS-MID: Waiting for messages on response channel');
        for await (const m of sub) {
          try{
            console.log('EXPRESS-MID: Received message from NATS');
            let data = NatsInterface.#decoder(m.data);
            console.log('EXPRESS-MID: Decoded message:', data.substring(0, 100) + (data.length > 100 ? '...' : ''));
            
            if(data == "Received."){
              console.log('EXPRESS-MID: Received acknowledgement from backend');
              clearTimeout(this.#processTimeout);
            }
            else if (data == "Test failed"){
              console.log('EXPRESS-MID: Test failed response from backend');
              res.status(400)
                 .json(this.#generateErrorReport('Exception while generating test report', 
                                           testName));
              sub.unsubscribe();
              break;
            }
            else{
              console.log('EXPRESS-MID: Received test results from backend');
              try {
                // Try to parse as JSON
                const jsonData = JSON.parse(data);
                
                // Check if it's the new format with actual and expected fields
                if (jsonData.actual && jsonData.expected && jsonData.test && jsonData.status) {
                  console.log('EXPRESS-MID: Detected new format with actual/expected');
                  const report = {
                    'Feature': testName,
                    'Tag': '@' + testName.split(' ').slice(0,2).join(''),
                    'Scenario': 'Test execution with step definition expected values',
                    'End result': jsonData.status === 'completed' ? 'passed' : 'failed',
                    'Description': 'Test executed with expected responses from step definitions',
                    'Expected response': jsonData.expected,
                    'Actual response': jsonData.actual
                  };
                  console.log('EXPRESS-MID: Generated report with actual/expected:', JSON.stringify(report));
                  res.status(200).json(report);
                } else if (Array.isArray(jsonData)) {
                  console.log('EXPRESS-MID: Detected Cucumber JSON report format');
                  const report = this.#generateReport(jsonData, testName);
                  console.log('EXPRESS-MID: Generated report from Cucumber:', JSON.stringify(report));
                  res.status(200).json(report);
                } else if (jsonData.result && jsonData.test && jsonData.status) {
                  // Legacy direct method format
                  console.log('EXPRESS-MID: Detected legacy direct method format');
                  const report = {
                    'Feature': testName,
                    'Tag': '@' + testName.split(' ').slice(0,2).join(''),
                    'Scenario': 'Direct method execution',
                    'End result': jsonData.status === 'completed' ? 'passed' : 'failed',
                    'Description': 'Test executed directly via Java method',
                    'Expected response': jsonData.expected || 'Success',
                    'Actual response': jsonData.result
                  };
                  console.log('EXPRESS-MID: Generated report from direct execution:', JSON.stringify(report));
                  res.status(200).json(report);
                } else {
                  // Unknown format
                  console.log('EXPRESS-MID: Received plain test results...');
                  const report = this.#generatePlainReport(JSON.stringify(jsonData), testName);
                  console.log('EXPRESS-MID: Generated report:', JSON.stringify(report));
                  res.status(200).json(report);
                }
              } catch (parseError) {
                console.error('EXPRESS-MID: Error parsing response:', parseError);
                res.status(400).json(this.#generateErrorReport('Error parsing backend response: ' + parseError.message, testName));
              }
              sub.unsubscribe();
              break;
            }
          }
          catch(err){
            console.error('EXPRESS-MID: Error processing message:', err);
            clearTimeout(this.#processTimeout);
            throw err
          }
        }
      }
}
