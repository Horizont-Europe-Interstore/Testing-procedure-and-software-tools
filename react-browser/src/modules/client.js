export default class Client{
    static #baseUrl='/api' 
    static async sendTest(testObject){
        try{
            let argsObject = {}
            for(let [k,v] of Object.entries(testObject.object)){
                k=k.split(' ').join('')
                k=k[0].toLowerCase()+k.substring(1,k.length)
                argsObject[k] = v;
            }
            testObject.object = argsObject;
            const res = await fetch(Client.#baseUrl, {
                method: "POST",
                body: JSON.stringify(testObject),
                headers: {
                "Content-Type": "application/json"
                }
            })
            const data = await res.json()
            
            // Return simplified response showing values stored
            return Client.#generateStoredValuesResponse(testObject, data);
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

    static #generateStoredValuesResponse(testObject, serverResponse){
        const storedValues = Object.entries(testObject.object)
            .filter(([k,v]) => v !== '')
            .map(([k,v]) => `${k}: ${v}`)
            .join(', ');
        
        return {
          Feature: testObject.test,
          Tag: '@'+testObject.test.split(' ').slice(0,2).join(''),
          Scenario: 'Values stored successfully',
          'End result': 'stored',
          Steps: [
            {
              Keyword: 'Stored ',
              Name: 'Values saved to application server',
              Result: 'passed',
              'Error message': '',
              Value: storedValues || 'No values provided'
            }
          ]
        };
    }

    static getTests(){
        return Client.#tests;
    }

    static getTestsByPart(part){
        switch(part) {
            case 1: return Client.#tests.filter(t => t.index >= 0 && t.index <= 7);
            case 2: return Client.#tests.filter(t => t.index >= 8 && t.index <= 21);
            case 3: return Client.#tests.filter(t => t.index >= 22);
            default: return Client.#tests;
        }
    }

    static getTestPart(index){
        if(index >= 0 && index <= 7) return 1;
        if(index >= 8 && index <= 21) return 2;
        if(index >= 22) return 3;
        return 1;
    }

    static #tests=[
        {
            index:0,
            test:'Device Capability', 
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: true,
            object:{
                'endDeviceListLink':'',
                'selfDeviceLink':'',
                'mirrorUsagePointListLink':'',
                'timeLink':'',
            },
        },
        {
            index:1,
            test:'Get All End Devices',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: false,
            object:{},
        },

        {
            index:2,
            test:'Create End Device',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: true,
            object:{
                'lfdi':'',
                'Device Category':'',
                'sfdi':'',
                'Registration Link':'',
                'Functionset Assignment Link':'',
                'Subscription Link':'',
                'Device Status Link':'',
                'End Device List Link':'',
                'DER List Link':'',
            },
        },
        {
            index:3,
            test:'Get An End Device',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: true,
            object:{
                id:''
                
            },
        },
        {
            index:4,
            test:'Register End Device',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: true,
            object:{
                endDeviceId:'',
                registrationPin:''
            },
        },
        {
            index:5,
            test:'Get Registered End Device',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: true,
            object:{
                endDeviceId:'',
                registrationID:''
            }
        },
        {
            index:6,
            test:'Time',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: false,
            object:{}
        },
        {
            index:7,
            test:'Advanced Time',
            desc:'Part 1: Basic Device Setup',
            part: 1,
            args: false,
            object:{}
        },
         
        {
            index:8,
            test:'Get All Function Set Assignments',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                endDeviceId:'',
                
            }
        },

        {
            index:9,
            test:'Create Function Set Assignments',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                endDeviceId:'',
                mRID:'',
                description:'',
                subscribable:'',
                version:'',
                DemandResponseProgramListLink:'', 
                FileListLink:'',
                TraiffProfileListLink:'',
                MessagingProgramListLink:'',
                UsagePointListLink:'',
                DERProgramListLink:'',
                CustomerAccountListLink:'',
                PrepaymentListLink:'',
                ResponseSetListLink:'',
            }
        }, 
        {
            index:10,
            test:'Get A Function Set Assignments',
            desc:'Part 2: Function Sets & DER Programs', 
            part: 2,
            args: true,
            object:{
                endDeviceId:'',
                fsaID:''
            }
        },

        {
            index:11,
            test:'Create Der Program', 
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                fsaID:'',
                mRID:'',
                primacy:'',
                description:'',
                subscribable:'',
                version:'',
                activeDERControlListLink:'',
                defaultDERControlLink:'',
                dERControlListLink:'',
                dERCurveListLink:'',
                
            }
        },

        {
            index:12,
            test:'Get All Der Programs',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                fsaID:'',
            }
        },
        {
            index:13,
            test:'Get A Der Program',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                fsaID:'',
                derID:'',
            }
        },

        
        {
            index:14,
            test:'Create Der Curve',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derProgramId:'',
                mRID:'',
                description:'',
                version:'',
                curveType:'',
                x_multiplier_type: '',
                y_multiplier_type: '',
                y_ref_type: '',
                x_value_1:'',
                y_value_1:'',
                x_value_2:'',
                y_value_2:'',
                x_value_3:'',
                y_value_3:'',
                x_value_4:'',
                y_value_4:'',
                x_value_5:'',
                y_value_5:'',
                x_value_6:'',
                y_value_6:'',
                x_value_7:'',
                y_value_7:'',
                x_value_8:'',
                y_value_8:'',
                x_value_9:'',
                y_value_9:'',
                x_value_10:'',
                y_value_10:'',

            }
        },
        {
            index:15,
            test:'Get All Der Curves',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derpID:'',
            }
        },
        {
            index:16,
            test:'Get A Der Curve',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derpID:'',
                dercID:''
            }   
        } ,
        
        {
            index:17,
            test:'Create Der Control',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derProgramId:'',
                deviceCategory:'',
                mRID: '',
                description: '',
                version: '',
                duration: '',
                start:'',
                currentStatus: '',
                dateTime: '',
                potentiallySuperseded: '',
                randomizeDuration: '',
                randomizeStart: '',
                opModConnect: '',
                opModEnergize: '',
                opModFixedPFAbsorbW: '',
                opModFixedPFInjectW: '',
                opModFixedVar: '',
                opModFixedW: '',
                opModFreqDroop: '',
                opModFreqWatt: '',
                opModHFRTMayTrip: '',
                opModHFRTMustTrip: '',
                opModHVRTMayTrip: '',
                opModHVRTMomentaryCessation: '',
                opModHVRTMustTrip: '',
                opModLFRTMayTrip: '',
                opModLFRTMustTrip: '',
                opModLVRTMayTrip: '',
                opModLVRTMomentaryCessation: '',
                opModLVRTMustTrip: '',
                opModMaxLimW: '',
                opModTargetVar: '',
                opModTargetW: '',
                opModVoltVar: '',
                opModVoltWatt: '',
                opModWattPF: '',
                opModWattVar: '',
                rampTms: '',

            }
        },
        {
            index:18,
            test:'Get All Der Controls',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derpID:'',
            }
        },
        {
            index:19,
            test:'Get A Der Control',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                derpID:'',
                derControlID:'',
            }
        },
        {
            index:20,
            test:'Create Der',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                endDeviceID:'',
                derCapabilityLink:'',
                derStatusLink:'',
                derAvailabilityLink:'',
                derSettingsLink:'',
                associatedUsagePointLink:'',
                associatedDERProgramListLink:'',
                currentDERProgramLink:''
            }
        },
        {
            index:21,
            test:'Get Der',
            desc:'Part 2: Function Sets & DER Programs',
            part: 2,
            args: true,
            object:{
                endDeviceID:'',
                derID:''
            }
        },
        {
            index:22,
            test:'Create Der Capability',
            desc:'Part 3: Advanced DER Configuration',
            part: 3,
            hidden: true,
            args: true,
            object:{
                derId:'',
                derCapabilityLink:'',
                modesSupported:'',
                rtgAbnormalCategory:'',
                rtgMaxA:'',
                rtgMaxAh:'',
                rtgMaxChargeRateVA:'',
                rtgMaxChargeRateW:'',
                rtgMaxDischargeRateVA:'',
                rtgMaxDischargeRateW:'',
                rtgMaxV:'',
                rtgMaxVA:'',
                rtgMaxVar:'',
                rtgMaxVarNeg:'',
                rtgMaxW:'',
                rtgMaxWh:'',
                rtgMinPFOverExcited:'',
                rtgMinPFUnderExcited:'',
                rtgMinV:'',
                rtgNormalCategory:'',
                rtgOverExcitedPF:'',
                rtgOverExcitedW:'',
                rtgReactiveSusceptance:'',
                rtgUnderExcitedPF:'',
                rtgUnderExcitedW:'',
                rtgVNom:'',
                derType:''

            }
        },

        {
            index:23,
            test:'Get A Der Capability',
            desc:'Part 3: Advanced DER Configuration',
            part: 3,
            args: true,
            object:{
                derID:'',
                endDeviceId:''

            }
        },
        {
            index:24,
            test:'Create Der Settings',
            desc:'Part 3: Advanced DER Configuration',
            part: 3,
            hidden: true,
            args: true,
            object:{
                   derId:'',
                   derSettingsLink:'',
                   modesEnabled:'',
                   setESDelay:'',
                   setESHighFreq:'',
                   setESHighVolt:'',
                   setESLowVolt:'',
                   setESRampTms:'',
                   setESRandomDelay:'',
                   setGradW:'',
                   setSoftGradW:'',
                   setMaxA:'',
                   setMaxChargeRateVA:'',
                   setMaxChargeRateW:'',
                   setMaxDischargeRateVA:'',
                   setMaxDischargeRateW:'',
                   setMaxV:'',
                   setMaxVA:'',
                   setMaxVar:'',
                   setMaxVarNeg:'',
                   setMaxW:'',
                   setMaxWh:'',
                   setMinPFOverExcited:'',
                   setMinPFUnderExcited:'',
                   setMinV:'',
                   setVNom:'',
                   setVRef:'',
                  

            }

        },

        {
            index:25,
            test:'Get A Der Settings',
            desc:'Part 3: Advanced DER Configuration',
            part: 3,
            args: true,
            object:{
                derID:'',
                endDeviceId:''

            }
        },

        {
            index:26,
            test:'Power Generation Test',
            desc:'Part 3: Advanced DER Configuration',
            part: 3,
            args:true,
            object:{
                endDeviceId:'',
                derID:'',
                setMaxW:'',
                setMaxVA:''

            }
        }
    ];

    static #validationObject={
        'Create End Device':{
            sfdi:(field)=>{return !isNaN(field)},
            deviceCategory:(field=>{return !isNaN(field)})
        }
        ,
        'Get An End Device':{
            id:(field)=>{return !isNaN(field)}
        }
        ,
        'Register End Device':{
            endDeviceId:(field)=>{return !isNaN(field)},
            registrationPin:(field)=>{return !isNaN(field)}
        }
        ,
        'Get Registered End Device':{
            endDeviceId:(field)=>{return !isNaN(field)},
            registrationID:(field)=>{return !isNaN(field)}
        }
        ,
        'Get All Function Set Assignments':{
            endDeviceId:(field)=>{return !isNaN(field)}
        }
        ,
        'Create Function Set Assignments':{
            endDeviceId:(field)=>{return !isNaN(field)},
            mRID: (field) => field.trim() !== "",
            subscribable:(field)=>{return !isNaN(field)},
            version:(field)=>{return !isNaN(field)}
        }, 

        'Get A Function Set Assignments':{
            endDeviceId:(field)=>{return !isNaN(field)},
            fsaID:(field)=>{return !isNaN(field)}
        },
        
        'Create Der Program':{
            fsaID:(field)=>{return !isNaN(field)},
            mRID: (field) => field.trim() !== "",
            primacy:(field)=>{return !isNaN(field)},
            subscribable:(field)=>{return !isNaN(field)},
            version:(field)=>{return !isNaN(field)},
            
   
        },

        'Get All Der Programs':{
            fsaID:(field)=>{return !isNaN(field)}
        },

        'Create Der':{
            endDeviceID:(field)=>{return !isNaN(field)}
        },
        'Get Der':{
            endDeviceID:(field)=>{return !isNaN(field)},
            derID:(field)=> {return !isNaN(field)}
        }

    }

    static getValid(key){
        return Client.#validationObject[key];
    }

    static async getXmlValidationResults(){
        try{
            const res = await fetch(Client.#baseUrl + '/xml-validation/results')
            const data = await res.json()
            return data;
        }
        catch(err){
            console.log('Error fetching XML validation results:', err)
            return [];
        }
    }

    static async clearXmlValidationResults(){
        try{
            await fetch(Client.#baseUrl + '/xml-validation/results', {
                method: 'DELETE'
            })
            return true;
        }
        catch(err){
            console.log('Error clearing XML validation results:', err)
            return false;
        }
    }
}


