Feature: Device Capability EndDevice 
  @DeviceCapabilityEndDevice
  Scenario Outline: Verifying Device Capability End Device Test Execution
    Given I have a device capability and end device test setup
    When I execute the end device test with subject "<natsSubject>"
    Then the test should complete successfully with EndDevice response containing:
    """
    {
      "1": "http://localhost/interstore/edev/1",
      "2": "http://localhost/interstore/edev/2",
      "3": "http://localhost/interstore/edev/3",
      "4": "http://localhost/interstore/edev/4"
    }
    """

  Examples:
    | natsSubject | 
    | natsSubject |

    