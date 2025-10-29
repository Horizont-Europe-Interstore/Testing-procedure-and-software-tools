Feature: Get A Der Control
  @GetADerControl
  Scenario Outline: Verifying DerControl Test Execution
    Given I have a get a der control test setup
    When I execute the get a der control test with subject "get_a_der_control"
    Then the test should complete successfully with a der control response containing:
    """
    {
      Test Description : This test is a part of the basic  der programs and control
      test to test that the  der control is present for a particular
      der program , the outcomes is either der control is found or
      not for the particular der program.
    }
    """