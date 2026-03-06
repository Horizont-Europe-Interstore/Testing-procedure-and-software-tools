# IEEE 2030.5 Testing Software Embeded With IEEE 2030.5 Server

Compact overview, structure, and quick-start instructions for the workspace.

## Project overview
This repository implements an IEEE 2030.5 conformance test application with:
- Java Spring Boot backend and services (interstore) — see [`interstore/app/src/main/java/interstore/App.java`](./interstore/app/src/main/java/interstore/App.java).
- React front-end (UI) — see [`react-browser`](./react-browser).
- Ad-hoc server for IEEE 2030.5 protocol handling — see [`adhoc-server`](./adhoc).
- The entire application is containerized via Docker — see [`docker-compose-prod.yml`](./docker-compose-prod.yml).

## Repository layout
- [ieee2030.5-conformance-testapp](./)
  - [interstore](./interstore/) — Java service, Spring Boot app and tests.
    - Main app: [`interstore/app/src/main/java/interstore/App.java`](./interstore/app/src/main/java/interstore/App.java)
  - [react-browser](./react-browser) — React UI; page components e.g. [`DerCurveSubForm.jsx`](./react-browser/src/page_components/main_tab_subelements/DerCurveSubForm.jsx) and [`MainTab.jsx`](./react-browser/src/page_components/MainTab.jsx).
  - [adhoc-server](./adhoc) — Ad-hoc server for IEEE 2030.5 protocol implementation.
  - Compose files:
    - Production: [`docker-compose-prod.yml`](./docker-compose-prod.yml)

## Project Usage
This server can be used in a Client-Server architecture with the client either being in a cloud environment or the client being as a field device. This can be classified as below:
1. Software-in-the-loop (cloud-based client)
2. Hardware-in-the-loop (field device)

## Configuration
1. Software-in-the-loop:
     The Software in the loop where the client as software in IEEE 2030.5 nomenclature the client refres to device that follows IEEE 2030.5 protocol norms and
     the server stands for any application server that made with rest api as per IEEE 2030.5. To test the software in the loop the client is an opensourced C-client
     which can can be self hosted in any server with self signed certificate in the IEEE2030.5 to eastablish the communication between client and server (TLS1.2) there
     is need of the certiface self signed CA (sign it with the IP of the host machine)  certificate is enoguh for the client.
     This is the github repo link for the C-client used: [Testing-Procedure-and-Software-tools-IEEE-2030.5-Client](https://github.com/Horizont-Europe-Interstore/Testing-Procedure-and-Software-tools-       IEEE-2030.5-Client).
     The testing software (referign to this software) which can be hosted in cloud and use the docker compose commnad to make it up make sure that this not https only
     support http for this reason make the right inbound and outbound configuration.
     Take the ip of the testing software and use it in the docker compose dev of the C-client in the above link( check it out the read me Testing-Procedure-and-Software-tools-IEEE-2030.5-Client)
      
2. Hardware-in-the-loop:
    Depoly adhoc server on premis in a computer where the ieee 2030.5 native device under test make sure that this computer is the same network as that of the device under test. 
    For the hardware in the loop deploy the adhoc server ( [`adhoc-server`](./adhoc).) in a docker installed computer and place the ip address of the hosted
    IEEE 2030.5 Testing Software Embeded With  IEEE 2030.5 Server in the docker compose of the adhoc server environment: - BACKEND_URL=http://xxx.xxx.xxx.xxx:8080 ports: put the ip address in xxx         part.
    The adhoc server must need the CA certificate issued from SunSpec (https://sunspec.org/) once the CA certificate is obtained place it ./certs folder should contain 3 files ca.pem,
    cert-key.pem, sat-root.pem. Once this is done the configuration is over , use the docker compose up --build to run the adhoc server
   
    Now set up the testing software on any cloud remmber that it does support the https ports of 443 for TLS however this software support the http so the inbound and outbound rules
    should select accordingly checkt he inbound rules edited ![In hound rules example](assets/https://github.com/Horizont-Europe-Interstore/Testing-procedure-and-software-tools/blob/main/assets/inbound_rules%20in_edited%20aws.jpg) and corresponding out bound rules. Run the docker compose up --build to start the software and start testing. 
   


## Quick start
1. Production-like stack:
   ```bash
   docker-compose.yml up --build

2. Access the frontend UI via the port 3000

3. The server works in 3 major phases. These are:
     The testing software consist of 2 pages one page is where the user can the details about the device under test such as the lfdi, sfdi of the device
     the testing form page it the page where the user can enter the details about the IEEE 2030.5 device uder test outbound registration in IEEE 2030.5
    ![GUI to enter the details about device under test](https://github.com/Horizont-Europe-Interstore/Testing-procedure-and-software-tools/blob/main/assets/ Testing%20Software%20Front%20End%20Form%20Page.png) and test results and validation page where the results are shown ![The expected and actual response compare the test results shown](https://github.com/Horizont-Europe-Interstore/Testing-procedure-and-software-tools/blob/main/assets/Test%20Results%20and%20Validation%20Page%20.png) 
     
    - Adding the resources:
        ![Demo](assets/demo.gif)

## Development notes
- Main test orchestration and helper methods live in [`interstore/App.java`](./interstore/app/src/main/java/interstore/App.java).
- The ad-hoc server handles IEEE 2030.5 protocol requests and inject LFDI/SFDI headers for requests — see [adhoc-server](./adhoc).
<!-- - Logstash destination and ports are configured in compose files: [`docker-compose-prod.yml`](ieee2030.5-restapi/docker-compose-prod.yml). -->



## Links (quick)
- [Interstore Java backend main app](./interstore/app/src/main/java/interstore/App.java) 
- [adhoc-server](./adhoc)
- [Docker compose (prod)](./docker-compose-prod.yml)
- [React UI](./react-browser)

<!-- - [Logstash config](ieee2030.5-restapi/logstash.conf) -->
