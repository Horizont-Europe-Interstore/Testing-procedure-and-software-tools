Feature: Get All Der Controls
  @GetAllDerControls
  Scenario Outline: Verifying DerControls Test Execution
    Given I have a get all der controls test setup
    When I execute the get all der controls test with subject "get_all_der_controls"
    Then the test should complete successfully with all der controls response containing:
    """
    {
      Test Description : This test is a part of the basic  der programs and control
      test to test that the  der controls are present for a particular
      der program , the out comes are either no der controls found or
      the der controls for the particular der program.
    }
    """