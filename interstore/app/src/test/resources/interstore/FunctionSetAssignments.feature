Feature: Function Set Assignments Test
  @FunctionSetAssignments
  Scenario Outline: Verifying Function Set Assignments Test Execution
    Given I have a fsa test setup
    When I execute the fsa test with service name "fsalistmanager" and subject "basic_fsa_test"
    Then the test should complete successfully with FunctionSetAssignmentTest response containing:
    """

     Test Description : This test is a part of the Basic Function Set Assignments, to find the FunctionSetAssignment
     resource present in EndDevice using the FunctionSetAssignmentListLink and to get back the resources present in
     the FunctionSetAssignments like the DERPrograms assigned to the FunctionSetAssignments.

     """