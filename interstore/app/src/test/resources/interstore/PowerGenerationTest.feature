Feature: Power Generation Test
  @CreateDerSettings
  Scenario Outline: Verifying PowerGeneration Test Execution
    Given I have a power generation test setup
    When I execute the power generation test with subject "power_generation"
    Then the test should complete successfully with create a power generation response containing:
    """
      Test Description : This test is a part of the basic  power generation and control 
      test to create a  power generation for a function set assignment and it returns 
      the der program values 
      
    """ 