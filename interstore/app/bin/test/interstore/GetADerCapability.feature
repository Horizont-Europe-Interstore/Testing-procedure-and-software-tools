Feature: Get A Der Capability
  @GetADerCapability
  Scenario Outline: Verifying DerCapability Test Execution
    Given I have a get a der capability test setup
    When I execute the get a der capability test with subject "get_a_der_capability"
    Then the test should complete successfully with a der capability response containing:
    """
    {
      Test Description : This test is a part of the basic  der capability and control
      test to test that the  der capability is present for a particular 
      function set assignemnts , the out comes is either der capability found or 
      not for the particular function set assignemnts
    }
    """ 