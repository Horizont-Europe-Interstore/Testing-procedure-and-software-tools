Feature: Create Der Capability
  @CreateDerCapability
  Scenario Outline: Verifying DerCapability Test Execution
    Given I have a create der capability test setup
    When I execute the create der capability test with subject "create_der_capability"
    Then the test should complete successfully with create a der capability response containing:
    """
      Test Description : This test is a part of the basic der capability and control 
      test to create a der program for a function set assignment and it returns 
      the der capability values 
      
    """ 