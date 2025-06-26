require('dotenv').config();
const express = require("express");
const bodyParser = require("body-parser");
const NatsInterface = require("../modules/NatsInterface.js");
const cors = require("cors");
const { exec } = require("child_process");

const natsInterface = new NatsInterface();
const app = express();
app.use(cors({
    origin: "*",
    methods: ["OPTIONS", "POST", "GET"]
}));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post("/api", (req, res) => {
    natsInterface.handleSetTest(req.body, res);
});

// New endpoint to run IEEE2030.5 client commands
app.post("/run", (req, res) => {
    const { command } = req.body;
    
    if (!command) {
        return res.status(400).json({ error: "Command is required" });
    }
    
    // Execute the IEEE2030.5 client command
    const clientCommand = `/app/build/client_test ens160 pti_dev.x509 ./certs/my_ca.pem https://134.130.169.111:8443/${command}`;
    
    exec(clientCommand, { cwd: "/IEEE-2030.5-Client" }, (error, stdout, stderr) => {
        if (error) {
            console.error(`Error executing command: ${error.message}`);
            return res.status(500).json({ error: error.message, output: stderr });
        }
        
        res.json({ output: stdout });
    });
});

app.listen(5000, () => {
    console.log("Listening on port 5000.");
});