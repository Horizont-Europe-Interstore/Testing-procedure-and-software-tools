Feature: Device Capability Test
  @DeviceCapability
  Scenario Outline: Verifying Device Capability Test Execution
    Given I have a device capability test setup
    When I execute the device capability test with service name "dcapmanager" and subject "<natsSubject>"
    Then the test should complete successfully with DeviceCapability response containing:
    """
     Test Description : This test is a part of the Device Capablity , to find out the device
                   Capabilities is present or not , for the simplicity purpose this test 
                   allready added the device capablity features those atleast a link for 
                   an enddevice has to be present in the server , in this sample test have
                   3 device capablities added those are a link for end device , mirror usage
                   point link , self device link . Any operator will have to add thier device 
                   capablity before all the test started this one of the intail condition , the 
                   test application right now has a default device capablity while runnig this 
                   test it will depicts the default device capablities .   
    
     """
 