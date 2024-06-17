Feature: Advanced Time Test
  @AdvancedTime
  Scenario Outline: Verifying Advanced Time Test Execution
    Given I have an advanced time test setup
    When I execute the advanced time test with service name "advancedtimemanager" and subject "<natsSubject>"
    Then the test should complete successfully with AdvancedTimeTest response containing:
    """

     Test Description : This test is a part of the Advanced Time, to find the time resource present in DeviceCapability and update it by 1 hour.
                        First the time resource from the DeviceCapability is fetched and then the Time is forwarded by 1 hour and stored.

     """