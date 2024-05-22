require('dotenv').config();
const express = require("express");
const bodyParser = require("body-parser")
const NATS = require("nats");
const NatsInterface = require("../modules/NatsInterface.js");

const natsInterface = new NatsInterface();
const app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post("/", (req,res)=>{
    natsInterface.handleSetTest(req.body, res)
})


app.listen(6000, ()=>{
    console.log("Listening on port 6000.")
})