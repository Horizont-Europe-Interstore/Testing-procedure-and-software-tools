Feature: Get All Der Curves
  @GetAllDerCurves
  Scenario Outline: Verifying DerCurves Test Execution
    Given I have a get all der curves test setup
    When I execute the get all der curves test with subject "get_all_der_curves"
    Then the test should complete successfully with all der curves response containing:
    """
    {
      Test Description : This test is a part of the basic  der programs and control
      test to test that the  der curves are present for a particular
      der program , the out comes are either no der curves found or
      the der curves for the particular der program.
    }
    """