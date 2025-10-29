Feature: Get All Function Set Assignments
  @GetAllFunctionSetAssignments
  Scenario Outline: Verifying FunctionSetAssignments Test Execution
    Given I have a get all function set assignments test setup
    When I execute the get all function set assignments test with subject "<natsSubject>"
    Then the test should complete successfully with all function set assignments response containing:
    """
    {
      Test Description : This test is a part of the basic function set assignments 
      test to test that the function set assignemts are present for a particular 
      end device , the out comes are either no function set assignemnts found or 
      the function set assignments for the particular device 
    }
    """ 

   