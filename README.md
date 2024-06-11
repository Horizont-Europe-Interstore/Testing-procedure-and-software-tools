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
## MiddleWare    
    * There is need of a middleware to faciliate the actions from frontend to the backend becuase the front end is browser process which 
      can only send/accept the request/responses in http for this reason one who operate from the web which is the easyiest way to access
      the application wills end the request to backend end so the middeleware sitting in between will facilitate the message direction to 
      nats publisher and the publisher will publish the message . 
## Frontend 
     * The front end of the app is in React which has buttons corresponds to features of IEEE 2030.5 which will be the part of the testing . 
     * For some features of the IEEE 2030.5 has to enter the details ( attributes) undergo testing this is handeled by the form . The react part
       of the front end will communicate to the middleware in the form of request and response. 
    
 ## Behaviour Driven automated test set up 
    * One of the most import part of the software testing is Cucumber , the Cucumber is bebaviour driven testing tool , open source 
      this tool can help to do the test set up and the test procedures and matching with the expected values . 
      
 ## An example testing procedure from the IEEE 2030.5 Core tests . 
     * Basice EndDevice Test (Core 008 in Suspec Doc)
        *  Purpose : The basic end device test verifies the REF-Client can find or POST its EndDevice instance from the IEEE 2030.5 server
                    and can use the resource information, including SFDI, LDFI, FunctionSetAssignmentsListLink, SubscriptionListLink, and LogEventListLink.
        * Set Up : Verify a DeviceCapability resource exists on the REF-Server, which includes a link to EndDeviceListLink and its subordinate resources. 
                   Pre-register an EndDevice instance for the REF-Client device, including all following attributes. For example, SFDI, LFDI, FunctionSetAssignments. 
        * Procedure :  
1. Retrieve the DeviceCapability resource from the REF-Server using the supported HTTP and IP address and find the EndDeviceListLink element.
2.  Perform a HTTP GET operatoin on the EndDeviceListLink URI and search through the EndDeviceList payload to find if an EndDevice instance is included that matches the identity of the REF-Client device. For example, SFDI/LFDI. If found, skip next step.
3.  If no such EndDevice instance is found in the EndDeviceList, do a POST of an EndDevice instance, which includes the following elements. For example, SFDI/LFDI and changedTime elements. The REF-Server shall respond with a 201 Created HTTP response code based on the handling of the POST payload and a Location header indicating the URI of the created resource on the REF-Server.
4. Using the Location of the created EndDevice instance returned by the REF-Server, perform a HTTP GET operation on that Location. On successful GET operation, the EndDevice instance payload returned by the REF-Server shall include relevant subordinate resources assigned to the REF-Client. For example SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI, and others.
5. Process the EndDevice instance returned by the REF-Server and perform a HTTP GET operatoin on the FunctionSetAssignmentsListLink to retrieve the various FSA-assigned resources assigned to the REF-Client.         

* Pass/Fail Criteria 
REF-Client requested and received the DeviceCapability resource on the REF-Server using the HTTP configuration information provided. REF-Server responded with 200 OK and returned a conformant payload for its DeviceCapability.
REF-Server returned an EndDeviceList payload in response to the REF-Client HTTP GET request. If the REF-Client is preregistered, the REF-Server should include an EndDevice instance associated with it. Otherwise, the EndDeviceList payload will not include the instance for the REF-Client in which case the REF-Client shall POST its own instance to the REF-Server.
If no EndDevice instance was found with the same identification as the REF-Client, it did a successful HTTP POST operation of its own EndDevice instance to the REF-Server. REF-Server successfully processed the HTTP POST operation from the REF-Client by creating a new EndDevice instance under the EndDeviceListLink resource and returned a 201 Created response with Location header indicating the URI of the newly created resource.
If no such EndDevice instance is found in the EndDeviceList, do a POST of an EndDevice instance, which includes mandatory elements. For example, SFDI/LFDI and changedTime elements. The REF-Server shall respond with a 201 Created HTTP response code based on the handling of the POST payload and a Location header indicating the URI of the created resource on the REF-Server.
REF-Client, using the Location of the created EndDevice instance returned by the REF-Server, performed a HTTP GET operation on that Location. On successful GET operation, the EndDevice instance payload returned by the REF-Server shall include relevant subordinate resources assigned to the REF-Client. For example, SubscriptionListLink, RegistrationLink, FunctionSetAssignmentsListLink, SFDI/LFDI, and others.
REF-Client processed the EndDevice instance returned by the REF-Server and did a HTTP GET on the FunctionSetAssignmentsListLink to retrieve the various FSA assigned resources assigned to the REF-Client.
                 
      

       
     
       
     
  
    


To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://git-ce.rwth-aachen.de/acs/private/research/interstore/ieee2030.5-restapi.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://git-ce.rwth-aachen.de/acs/private/research/interstore/ieee2030.5-restapi/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thank you to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README
Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
