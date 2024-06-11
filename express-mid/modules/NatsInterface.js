
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

      this.#processTimeout = setTimeout(()=>{
        res.status(400)
           .json(this.#generateErrorReport("Test Timed Out", testName));
      },NatsInterface.#processMaxTime);

      NatsInterface.#NATS.connect({servers:process.env.NATS_URL})
          .then(async (connection)=>{
            connection.publish(NatsInterface.#controleChannel, JSON.stringify(testObject));
            const sub = connection.subscribe(NatsInterface.#responseChannel);
            await this.#sendMsgs(res,testName,sub).then(()=>connection.close());
          })
          .catch(err=>{
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

      #pingBackend(){

      }

      #restoreBackend(){

      }

      async #sendMsgs(res,testName,sub) {
        for await (const m of sub) {
          try{
            let data = NatsInterface.#decoder(m.data);
            if(data == "Received."){
              clearTimeout(this.#processTimeout);
            }
            else if (data == "Test failed"){
              res.status(400)
                 .json(this.#generateErrorReport('Exception while generating test report', 
                                           testName));
              sub.unsubscribe();
              break;
            }
            else{
              res.status(200)
                 .json(this.#generateReport(JSON.parse(data),testName));
              sub.unsubscribe();
              break;
            }
          }
          catch(err){
            clearTimeout(this.#processTimeout);
            throw err
          }
        }
      }
}
