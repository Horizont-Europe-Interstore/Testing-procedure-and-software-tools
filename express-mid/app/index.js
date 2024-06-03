require('dotenv').config();
const express = require("express");
const bodyParser = require("body-parser")
const NatsInterface = require("../modules/NatsInterface.js");
const cors = require("cors")

const natsInterface = new NatsInterface();
const app = express();
app.use(cors({
    methods:["OPTIONS","POST"]
}));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post("/api", (req,res)=>{
    natsInterface.handleSetTest(req.body, res)
})


app.listen(5000, ()=>{
    console.log("Listening on port 5000.")
})