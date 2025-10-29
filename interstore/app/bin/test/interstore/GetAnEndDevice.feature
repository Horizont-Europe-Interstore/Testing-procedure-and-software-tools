Feature: Device Capability Get All End Device Test
  @DeviceCapabilityGetAllEndDevice
  Scenario Outline: Verifying Device Capability Get All End Device Test Execution
    Given I have a device capability get all end device test setup
    When I execute the device capability get all end device test with service name "enddevicemanager" and subject "<natsSubject>"
    Then the test should complete successfully with DeviceCapabilityGetAllEndDevice response containing:
    """

      {
      Test Description : This is part of the Basic End Device  Test, this 
      checks the Given end device is present in the server or not by entering 
      the SFDI value of the end device of interest , the response will be the 
      given end device or it says that the given end device is not found 
      
    }
    
    """