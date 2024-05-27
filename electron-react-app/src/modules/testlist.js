/*
TESTS ARE GENERATED FROM THIS FILE !
Add the test to the tests array following the same pattern.
If the test requires some parameters, then the Obj key will 
represent those parameters exactly the same way they are fed
in the backend.
NAME: Words should be spaced and capitalized
DESCRIPTION: This appears on the tooltip when a user hovers over the button
ARGS: Whether the test requires parameters.
OBJECT:Representation of the test parameters as expected from the test runner
*/

const tests=[
    {
        test:'Device Capability', 
        desc:'description',
        args: false,
        object:{},
    },
    {
        test:'Get All End Devices',
        desc:'description',
        args: false,
        object:{},
    },
    {
        test:'Device Capability End Device Link',
        desc:'description',
        args: false,
        object:{},
    },
    {
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
            derListLink:'',
        }
    },
    {
        test:'Device Capability Get All End Device',
        desc:'description',
        args: false,
        object:{}
    }
]

export default tests