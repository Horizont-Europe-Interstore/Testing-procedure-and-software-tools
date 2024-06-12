Feature: Time Test
  @Time
  Scenario Outline: Verifying Time Test Execution
    Given I have a time test setup
    When I execute the time test with service name "timemanager" and subject "<natsSubject>"
    Then the test should complete successfully with TimeTest response containing:
    """

     Test Description : This test is a part of the Basic Time, to find the time resource present in DeviceCapability.
                        First the quality metric returned from the DeviceCapability is checked and then the Time is
                        synchronized with the REF-Client.

     """