testJson=JSON.stringify({ test:'CreateEndDevice',
desc:'description',
args: true,
object:{
    lfdi:"3E4F45",
    deviceCategory:0,
    sfdi:"16726121139L",
    registrationLink:'/rg',
    functionAssignmentLink:'/fsa',
    subscriptionLink:'/sub',
    deviceStatusLink:'/dstat',
    endDeviceListLink:'/edev',
}})

const nc = require("nats").connect().then(connection =>{
const pub = connection.publish("ControleInstructions", testJson); // 
const sub = connection.subscribe("ControleResponse"); //is application running ?
})

