Feature: Get A Function Set Assignments
  @GetAFunctionSetAssignments
  Scenario Outline: Verifying FunctionSetAssignments Test Execution
    Given I have a get a function set assignments test setup
    When I execute the get a function set assignments test with subject "get_a_function_set_assignments"
    Then the test should complete successfully with a function set assignments response containing:
    """

      Test Description : This test is a part of the basic function set assignments 
      test to test that the function set assignemts of a particular device , details
      about a function set assignments , depicts it's attributes 

    """ 
