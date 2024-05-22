const path = require('node:path');
require('dotenv').config();
const NATS = require("nats");
const decoder = NATS.StringCodec().decode;
let sub;
let processTimeout;
const processMaxTime=30000;

function handleSetTest (event, testObject) {
  let testName = testObject.test;
  testObject.test = testObject.test.replace(/ /g,'');
  let webContents = event.sender;
  webContents.send('log-msg', 'Preparing Test');
  
  processTimeout = setTimeout(()=>{
    webContents.send('report-msg',generateErrorReport('App not responding', testName));
    webContents.send('log-msg','Ready')
  },processMaxTime);

  NATS.connect()
      .then(connection=>{
        const pub = connection.publish("ControleInstructions", JSON.stringify(testObject));
        sub = connection.subscribe("ControleResponse")
        sendMsgs(webContents,sub,testName)
     })
      .catch(err=>{
        webContents.send('report-msg',
                          generateErrorReport('Could not connect to NATS server.', 
                                              testName));
        webContents.send('log-msg','Ready')
        console.log(err);
      });
}

function generateErrorReport(error, testName){
  
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
}

}

function generateReport(fileContents){  
  let steps = []
  let report = {
    'Feature':fileContents[0].name,
    'Tag':fileContents[0].elements[0].tags[0].name,
    'Scenario' : fileContents[0].elements[0].name
  }
  passed = true;
  for(x of fileContents[0].elements[0].steps){
    passed &= x.result.status == 'passed'
    steps.push({
      'Keyword':x.keyword,
      'Name': x.name,
      'Result':x.result.status,
      'Error message':x.result.status=='failed' ? x.result.error_message:'',
      'Value': x.doc_string? JSON.stringify(x.doc_string.value).replace(/(\\\"|\\n)/g,'') : ''
    })
  }
  report['End result'] = passed ? 'passed' : 'failed'
  report['Steps'] = steps
  return report
}

async function sendMsgs(webContents,subscription,testName) {
  for await (const m of subscription) {
    try{
      let data = decoder(m.data);
      if(data == "Received."){
        webContents.send("log-msg", "Running");
        clearTimeout(processTimeout);
      }
      else if (data == "Test failed"){
        webContents.send('report-msg',
                          generateErrorReport('Exception while generating test report', 
                                              testName));
        webContents.send("log-msg", "Ready");
      }
      else{
        webContents.send('report-msg', generateReport(JSON.parse(data))); 
        webContents.send('log-msg', 'Ready');
      }
    }
    catch(err){
      webContents.send('report-msg', generateErrorReport('Exception while parsing report',
                                                          testName));
      console.log(err)
    }
  }
  subscription.unsubscribe()
}

  module.exports = {
    handleSetTest:handleSetTest
  }