Feature: Device Capability EndDevice 
  @DeviceCapabilityEndDevice
  Scenario Outline: Verifying Device Capability End Device Test Execution
    Given I have a device capability and end device test setup
    When I execute the end device test with subject "<natsSubject>"
    Then the test should complete successfully with EndDevice response containing:
    """
    {
      Test Description :This is part of the Device capablity Test, to find out all
       End Devices present in the server , there is two possible outcomes 
			 for this test one is no end devices found which implies that no end devices 
			 added to the test application , another outcome is listing all the end devices urls 
			 through particular url will get the details of the end device presented on the 
			 server . if there is no end device found the next test test is to create an end device 
    }
    """ 

   

    