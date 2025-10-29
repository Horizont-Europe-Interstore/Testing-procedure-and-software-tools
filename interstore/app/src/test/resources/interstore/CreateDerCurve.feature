Feature: Create Der Curve
  @CreateDerCurve
  Scenario Outline: Verifying DerCurve Test Execution
    Given I have a create der curve test setup
    When I execute the create der curve test with subject "create_der_curve"
    Then the test should complete successfully with create a der curve response containing:
    """
      Test Description : This test is a part of the basic der program and control
      test to create a der curve for a der program and it returns
      the der curve values

    """