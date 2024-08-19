Feature: Create Function Set Assignments
  @CreateFunctionSetAssignments
  Scenario Outline: Verifying FunctionSetAssignments Test Execution
    Given I have a create function set assignments test setup
    When I execute the create function set assignments test with subject "create_function_set_assignment"
    Then the test should complete successfully with create a function set assignments response containing:
    """
      Test Description : This test is a part of the basic function set assignments 
      test to create a function set assignments for an end device and it returns 
      the id of the created function set assignemnts
      
    """ 