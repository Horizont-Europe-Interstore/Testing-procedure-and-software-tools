Feature: Get A Der Curve
  @GetADerCurve
  Scenario Outline: Verifying DerCurve Test Execution
    Given I have a get a der curve test setup
    When I execute the get a der curve test with subject "get_a_der_curve"
    Then the test should complete successfully with a der curve response containing:
    """
    {
      Test Description : This test is a part of the basic  der programs and control
      test to test that the  der curve is present for a particular
      der program , the outcomes is either der curve is found or
      not for the particular der program.
    }
    """