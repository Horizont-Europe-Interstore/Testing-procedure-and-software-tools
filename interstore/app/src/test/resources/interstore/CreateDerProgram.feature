Feature: Create Der Program
  @CreateDerProgram
  Scenario Outline: Verifying DerProgram Test Execution
    Given I have a create der program test setup
    When I execute the create der program test with subject "create_der_program"
    Then the test should complete successfully with create a der program response containing:
    """
      Test Description : This test is a part of the basic der program and control 
      test to create a der program for a function set assignment and it returns 
      the der program values 
      
    """ 