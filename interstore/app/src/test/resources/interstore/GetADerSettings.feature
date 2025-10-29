Feature: Get A Der Settings 
  @GetADerCapability
  Scenario Outline: Verifying DerSettings  Test Execution
    Given I have a get a der settings test setup
    When I execute the get a der settings test with subject "get_a_der_settings"
    Then the test should complete successfully with a der settings response containing:
    """
    {
      Test Description : This test is a part of the basic  der settings  and control
      test to test that the  der settings  is present for a particular 
      function set assignemnts , the out comes is either der settings  found or 
      not for the particular function set assignemnts
    }
    """ 