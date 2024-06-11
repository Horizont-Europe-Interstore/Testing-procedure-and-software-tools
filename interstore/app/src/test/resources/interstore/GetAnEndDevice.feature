Feature: Device Capability Get All End Device Test
  @DeviceCapabilityGetAllEndDevice
  Scenario Outline: Verifying Device Capability Get All End Device Test Execution
    Given I have a device capability get all end device test setup
    When I execute the device capability get all end device test with service name "enddevicemanager" and subject "<natsSubject>"
    Then the test should complete successfully with DeviceCapabilityGetAllEndDevice response containing:
    """

      {"message":"No endDevices found."}
    
    """