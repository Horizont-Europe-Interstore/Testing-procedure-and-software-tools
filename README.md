# IEEE2030.5 Testing Procdure and Software Tools 

## Getting started

The Testing procedure and test software were developed base on the Sunspec documentation this is a standered for developing testing 
softwares and the sunspec testing procedures emphasises on CSIP smart inverter profile . The test software constructed in a manner
which the behaviour driven development way which transalted that in given conditions what are the expected outcomes of the testing. 
A variety of tests can be conducted according to the SunSpec CSIP Conformance Test Procedures, encompassing server and client tests.

* IEEE 2030.5 is a wast protocol to test this protocol is a client server model which means that the server should contains the
  implimentation of the protocol to complete the testing , in IEEE 2030.5 the features or properties of the protocol is addressed
  as resources , different kind of resources is present in the IEEE 2030.5 which are inter connected via links (hyperlinks ). 
* Every Resources in IEEE 2030.5 is accessed through a Hyperlink for this reason the resources has to be found or constrcted in order
  to test the application , which implies that there is need of server that impliments the resources .
* To test the resources in the server there is a need of client this client can be a web client or desktop client all important
  is that this client shall send the paylad which is acceptable by IEEE 2030.5 protocol .

## Construction of test Software . 

   The test tool developed based on the fact that any IEEE 2030.5 system that has to be tested will needs to enter the IEEE 2030.5 
   Specification which is realized on thier premesis to thhis test application , using docker compose the instance of this software 
   will be available , by this way the test software will be a copy or mirror image of the IEEE 2030.5 system that choosen for testing.  
   
 * The test software is developed of Many components it's divided into different parts Backend, Mode communication, MiddleWare , FrontEnd
    and behaviour driven testing agent
 * This test software is behaviour driven which refers to ,
     * Given: In the Given test condition this referes to the test set up
     * When:  The prcedure of the test , every test has a list of procedure ( test procedure)
     * Then: The expected and actaul response to show that the test is passed or faulied .
         
  ## BackEnd
   
  * The Backend of the application is an application server (Spring Boot) which is complimented by dependency injection framework google guice 
   to automate the operations in the backend , the resources are created as Plain Old Java (POJO) .

  * It's necessary to have a database to store resources of the IEEE 2030.5 which every end user shall try to replicate from thier system for this
    reason the data has to persisted to maintain the  state of the resources . Since it's a testing application the database opted in an embeded
    database once the application is closed the data will be loss to keep the test data or mirror constrcted IEEE 2030.5 resources one can deploy it
    in Kubernetes that will persist the data . 
  * The backend of the appliction shall include a mechanisam to test the real IEEE 2030.5 system that's is mirror imaged in the application .

## Communication 
   * The communication between the client and the server ( where IEEE 2030.5 resources constructed ) is established throug NATS messaging system
     
   * What Nats does is that there is a NATS server to this NATS sever , the messages can be published and subscribed the testing application will
     publish a message under given subject  as a trigger from the frontend ( client who triger the test) and the server side can subscribe it ,
     based on the mesage it received the backend automatically find the corresponding resource and process the message and send back to
     the front end ( client who triger the test) using a new publish from the backend and it will consuned in the frontend by a subscription .
     
 ## Middle Ware    
 * There is need of a middleware to faciliate the actions from frontend to the backend becuase the front end is browser process which 
   can only send/accept the request/responses in http for this reason one who operate from the web which is the easyiest way to access
   the application wills end the request to backend end so the middeleware sitting in between will facilitate the message direction to 
   nats publisher and the publisher will publish the message .  
      
 ## Front End . 
 * The front end of the app is in React which has buttons corresponds to features of IEEE 2030.5 which will be the part of the testing .
     
 * For some features of the IEEE 2030.5 has to enter the details ( attributes) undergo testing this is handeled by the form . The react part
   of the front end will communicate to the middleware in the form of request and response. 
    
 ## Behaviour driven automated test set up . 
 * One of the most import part of the software testing is Cucumber , the Cucumber is bebaviour driven testing tool , open source 
   this tool can help to do the test set up and the test procedures and matching with the expected values . 
      
 ## An example testing procedure from the IEEE 2030.5 Core tests . 
 * Basice EndDevice Test (Core 008 in Suspec Document)
 * Acronym REF-Client: Reference client a front end client ot intiate the actions , REF server Reference Server where the resources are hosted . 
 *  Purpose : The basic end device test verifies the REF-Client can find or POST its EndDevice instance from the IEEE 2030.5 server
             and can use the resource information, including SFDI, LDFI, FunctionSetAssignmentsListLink, SubscriptionListLink, and LogEventListLink.
 * Set Up : Verify a DeviceCapability resource exists on the REF-Server, which includes a link to EndDeviceListLink and its subordinate resources. 
            Pre-register an EndDevice instance , including all following attributes. For example, SFDI, LFDI, FunctionSetAssignments. 
 * Procedure :  
1. Retrieve the DeviceCapability resource from the REF-Server using the supported HTTP and IP address and find the EndDeviceListLink element.
2.  Perform a HTTP GET operatoin on the EndDeviceListLink URI and search through the EndDeviceList payload to find if an EndDevice instance is included that matches the identity of the REF-Client device. For example, SFDI/LFDI. If found, skip next step.
3.  If no such EndDevice instance is found in the EndDeviceList, do a POST of an EndDevice instance, which includes the following elements. For example, SFDI/LFDI and changedTime elements. The REF-Server shall respond with a 201 Created HTTP response code based on the handling of the POST payload and a Location header indicating the URI of the created resource on the REF-Server.
4. Using the Location of the created EndDevice instance returned by the REF-Server, perform a HTTP GET operation on that Location. On successful GET operation, the EndDevice instance payload returned by the REF-Server shall include relevant subordinate resources assigned to the REF-Client. For example SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI, and others.
5. Process the EndDevice instance returned by the REF-Server and perform a HTTP GET operatoin on the FunctionSetAssignmentsListLink to retrieve the various FSA-assigned resources assigned to the REF-Client.         

* Pass/Fail Criteria :  
1). REF-Client requested and received the DeviceCapability resource on the REF-Server using the HTTP configuration information provided. REF-Server responded with 200 OK and returned a conformant payload for its DeviceCapability.
2). REF-Server returned an EndDeviceList payload in response to the REF-Client HTTP GET request. If the REF-Client is preregistered, the REF-Server should include an EndDevice instance associated with it. Otherwise, the EndDeviceList payload will not include the instance for the REF-Client in which case the REF-Client shall POST its own instance to the REF-Server.
3). If no EndDevice instance was found with the same identification as the REF-Client, it did a successful HTTP POST operation of its own EndDevice instance to the REF-Server. REF-Server successfully processed the HTTP POST operation from the REF-Client by creating a new EndDevice instance under the EndDeviceListLink resource and returned a 201 Created response with Location header indicating the URI of the newly created resource.
4). If no such EndDevice instance is found in the EndDeviceList, do a POST of an EndDevice instance, which includes mandatory elements. For example, SFDI/LFDI and changedTime elements. The REF-Server shall respond with a 201 Created HTTP response code based on the handling of the POST payload and a Location header indicating the URI of the created resource on the REF-Server.
5). REF-Client, using the Location of the created EndDevice instance returned by the REF-Server, performed a HTTP GET operation on that Location. On successful GET operation, the EndDevice instance payload returned by the REF-Server shall include relevant subordinate resources assigned to the REF-Client. For example, SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI, and others.
6). REF-Client processed the EndDevice instance returned by the REF-Server and did a HTTP GET on the FunctionSetAssignmentsListLink to retrieve the various FSA assigned resources assigned to the REF-Client.

## Walk Through the example using the TestApp. 
* This test application is made using NATS instead of http to send  message /request from client to server for this reason when it comes to actaul
  implimentation of the above described example testign procedure needs to adapt to NATS standers , although while a user clicking the buttons in the test app
  http request is sending but this will be immediately transferd to NATS messaging , the reason for clicking buttons sending the http request is that the
  application front end (clicking part) is a browser process , the browser directly supports http request not NATS . 
* The test app has button called device capability Test click on that it will returun the devcie capability response with the actual response form the
  server and an expected response if it mathces test passes next step is to check that if there is any end devcie present to do so click on the get all end devices
  Test button it will list the end devices present in the server else it will shows a message that no end devices present if there is no end devices present then
  create an end device using Create End Device Test once create the end device , call again get all end devcies it will shows the lastly created end device . 


## Installation
There are two docker compose files one is for development which is docker-compose-dev.yml and other one is docker-compose-web.yml to deploy the application 
clone this repository and run the docker-compose-web.yml and open your localhost in port 3001 to access the web interface . 


## Roadmap
In the current release of the software is with most of the core tests of IEEE 2030.5 , the other test will be added in each release to complete 
the IEEE 2030.5 protocol . 

## License
For open source projects, say how it is licensed.

## Project status
Active and maintaning . 
