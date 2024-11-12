Feature: Create Der Control
  @CreateDerControl
  Scenario Outline: Verifying DerControl Test Execution
    Given I have a create der control test setup
    When I execute the create der control test with subject "create_der_control"
    Then the test should complete successfully with create a der control response containing:
    """
      Test Description : This test is a part of the basic der program and control
      test to create a der control for a der program and it returns
      the der control values

    """