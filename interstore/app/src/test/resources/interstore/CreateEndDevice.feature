Feature: Create End Device
  @CreateEndDevice
  Scenario Outline: Verifying Create End Device Test execution
    Given I have a create end device test setup
    When I execute the create end device test with service name "createnewenddevice" and subject "<natsSubject>"
    Then the test should complete successfully with CreateEndDevice response containing:
    """
    {"LFDI":"ssssss","SFDI":17}
    
    """