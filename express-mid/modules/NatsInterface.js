
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
              clearTimeout(this.#processTimeout)
          });
    }

    #generateErrorReport(error, testName){
        return {
          Feature: testName,
          Tag: '@'+testName.split(' ').slice(0,2).join(''),
          Scenario: '...',
          'End result': 'failed',
          Steps: [
            {
              Keyword: 'Given ',
              Name: '...',
              Result: 'skipped',
              'Error message': error,
              Value: ''
            }
          ]
        };
    }

    #generateReport(fileContents){  
        let steps = [];
        let report = {
          'Feature':fileContents[0].name,
          'Tag':fileContents[0].elements[0].tags[0].name,
          'Scenario' : fileContents[0].elements[0].name
        };
        let passed = true;
        for(let x of fileContents[0].elements[0].steps){
          passed &= x.result.status == 'passed';
          steps.push({
            'Keyword':x.keyword,
            'Name': x.name,
            'Result':x.result.status,
            'Error message':x.result.status=='failed' ? x.result.error_message:'',
            'Value': x.doc_string? JSON.stringify(x.doc_string.value).replace(/(\\\"|\\n)/g,'') : ''
          });
        }
        report['End result'] = passed ? 'passed' : 'failed';
        report['Steps'] = steps;
        return report;
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
                 .json(this.#generateReport(JSON.parse(data)));
              sub.unsubscribe();
              break;
            }
          }
          catch(err){
            throw err
          }
        }
      }
}
