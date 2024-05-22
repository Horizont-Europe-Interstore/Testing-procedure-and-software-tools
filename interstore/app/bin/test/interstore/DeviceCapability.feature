Feature: Device Capability Test
  @DeviceCapability
  Scenario Outline: Verifying Device Capability Test Execution
    Given I have a device capability test setup
    When I execute the device capability test with service name "dcapmanager" and subject "<natsSubject>"
    Then the test should complete successfully with DeviceCapability response containing:
    """
    {
      
      "1":["http://localhost/sdev","http://localhost/mup","http://localhost/edev"]
    }
    """