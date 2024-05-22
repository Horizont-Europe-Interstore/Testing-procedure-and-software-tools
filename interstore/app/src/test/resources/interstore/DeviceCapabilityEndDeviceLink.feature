Feature: Device Capability EndDevice Link
@DeviceCapabilityEndDeviceLink
 Scenario Outline: Verifying Device Capability End Device Attributes Test Execution
 Given I have a device capability and end device with attributes as response test setup
 When I execute the end device link test with subject "<natsSubject>"
 Then the test should complete successfully with EndDevice response with attributes containing:
 """
 {
    "registrationLink":"http://localhost/interstore/edev/1/rg",
    "deviceCategory":"00",
    "functionSetAssignmentsListLink":"http://localhost/interstore/edev/1/fsa",
    "deviceStatusLink":"http://localhost/interstore/edev/1/dstat",
    "subscriptionListLink": "http://localhost/interstore/edev/1/fsa",
    "sFDI":16726121139,"lFDI":"3E4F45"
 }
"""
 Examples:
    | natsSubject | 
    | natsSubject |
