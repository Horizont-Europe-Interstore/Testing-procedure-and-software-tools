Feature: Create Der Settings
  @CreateDerSettings
  Scenario Outline: Verifying DerSettings Test Execution
    Given I have a create der settings test setup
    When I execute the create der settings test with subject "create_der_settings"
    Then the test should complete successfully with create a der settings response containing:
    """
      Test Description : This test is a part of the basic der settings and control 
      test to create a der settings for a function set assignment and it returns 
      the der program values 
      
    """ 