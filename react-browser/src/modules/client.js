export default class Client{
    static #baseUrl='/api'  //FOR WITHOUT DOCKER: 'http://fedora-gna-1.acs-lab.eonerc.rwth-aachen.de:5000/api'
    static async sendTest(testObject){
        try{
            const res = await fetch(Client.#baseUrl, {
                method: "POST",
                body: JSON.stringify(testObject),
                headers: {
                "Content-Type": "application/json"
                }
            })
            const data = await res.json()
            return data;
        }
        catch(err){
            console.log(err)
            return Client.#generateErrorReport("Exception when sending test object\n"+err,
                                                testObject.test)
        }
    }

    static #generateErrorReport(error, testName){
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

    static getTests(){
        return Client.#tests;
    }

    static #tests=[
        {
            index:0,
            test:'Device Capability', 
            desc:'description',
            args: false,
            object:{},
        },
        {
            index:1,
            test:'Get All End Devices',
            desc:'description',
            args: false,
            object:{},
        },
        {
            index:2,
            test:'Device Capability End Device',
            desc:'description',
            args: false,
            object:{},
        },
        {
            index:3,
            test:'Device Capability End Device Link',
            desc:'description',
            args: false,
            object:{},
        },
        {
            index:4,
            test:'Create End Device',
            desc:'description',
            args: true,
            object:{
                lfdi:'',
                deviceCategory:'',
                sfdi:'',
                registrationLink:'',
                functionsetAssignmentLink:'',
                subscriptionLink:'',
                deviceStatusLink:'',
                endDeviceListLink:'',
            },
        },
        {
            index:5,
            test:'Device Capability Get All End Device',
            desc:'description',
            args: false,
            object:{}
        }
    ];

    static #validationObject={
        'Create End Device':{
            sfdi:(field)=>{return !isNaN(field)},
            deviceCategory:(field=>{return !isNaN(field)})
        }
    }

    static getValid(key){
        return Client.#validationObject[key];
    }
}


