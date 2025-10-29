Feature: Register an EndDevice 
  @RegisterEndDevice
  Scenario Outline: Verifying Register an EndDevice 
    Given I have a register an end device test setup
    When I execute the register end device test with service name "enddeviceregistrationmanager" and subject "my_subject"
    Then the test should complete successfully with RegisterEndDevice response containing:
    """

      {
      Test Description : This is part of the Basic End Device  Test, once
      there is an endDevice is present in the server it has to register with 
      a pin , this test register an endDevice with a pin and verify it with 
      corresponding field . 
      
    }
    
    """

    