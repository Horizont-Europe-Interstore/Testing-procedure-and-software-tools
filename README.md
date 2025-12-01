# IEEE 2030.5 Server

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
    - Development: [`docker-compose-dev.yml`](./docker-compose-dev.yml)
    - Production: [`docker-compose-prod.yml`](./docker-compose-prod.yml)

## Project Usage
This server can be used in a Client-Server architecture with the client either being in a cloud environment or the client being as a field device. This can be classified as below:
1. Software-in-the-loop (cloud-based client)
2. Hardware-in-the-loop (field device)

## Configuration
1. Software-in-the-loop: 
    - The cloud-based client used was a C-client complient to the IEEE2030.5 protocol.
    - The C-client requires a self-signed certificate via the IP on which the server is hosted.
    - This is the github repo link for the C-client used: [Testing-Procedure-and-Software-tools-IEEE-2030.5-Client](https://github.com/Horizont-Europe-Interstore/Testing-Procedure-and-Software-tools-IEEE-2030.5-Client)  

2. Hardware-in-the-loop:




## Quick start
1. Production-like stack:
   ```bash
   docker compose -f ./docker-compose-prod.yml up --build

2. Access the frontend UI via the port 3000

3. The server works in 3 major phases. These are:
    - Adding the resources:
        <div>
            <video src="./assets/adding_resource.mp4" controls width="800"></video>
        </div>
          - Result of adding the resource:
          <div>
              <img src="./assets/adding_resource_result.jpg" width="800"/>
          </div>
    - Testing the resources:

    - Viewing the results:
        <div>
            <video src="./assets/viewing_report.mp4" controls width="800"></video>
        </div>

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