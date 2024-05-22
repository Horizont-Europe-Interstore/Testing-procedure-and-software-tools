
module.exports = class NatsInterface{
    static #dotenvConfig = require('dotenv').config();
    #sub;
    #processTimeout;
    static #processMaxTime=30000;
    static #controleChannel = 'ControleInstructions';
    static #responseChannel = 'ControleResponse';
    static #NATS = require("nats");
    static #decoder = NatsInterface.#NATS.StringCodec().decode; 
    constructor(){}

    handleSetTest (testObject, res) {
      console.log(testObject)
      let testName = testObject.test;
      testObject.test = testObject.test.replace(/ /g,'');
      this.#processTimeout = setTimeout(()=>{
        res.status(400)
           .json(this.#generateErrorReport("Test Timed Out", testName));
      },NatsInterface.#processMaxTime);
      NatsInterface.#NATS.connect({servers:process.env.NATS_URL})
          .then(connection=>{
            connection.publish(NatsInterface.#controleChannel, JSON.stringify(testObject));
            this.#sub = connection.subscribe(NatsInterface.#responseChannel);
            this.#sendMsgs(res,testName);
          })
          .catch(err=>{
              res.status(400)
                 .json(this.#generateErrorReport('Could not connect to NATS server\n'+err, 
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

      async #sendMsgs(res,testName) {
        for await (const m of this.#sub) {
          try{
            let data = NatsInterface.#decoder(m.data);
            if(data == "Received."){
              clearTimeout(this.#processTimeout);
            }
            else if (data == "Test failed"){
              res.status(400)
                 .json(this.#generateErrorReport('Exception while generating test report', 
                                           testName));
            }
            else{
              res.status(200)
                 .json(this.#generateReport(JSON.parse(data)));
            }
          }
          catch(err){
            res.status(400)
                 .json(this.#generateErrorReport('Exception while parsing test report\n'+err, 
                                           testName));
          }
        }
        this.#sub.unsubscribe();
      }
}
