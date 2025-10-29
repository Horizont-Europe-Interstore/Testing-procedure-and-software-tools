Feature: Get A Der Program
  @GetADerProgram
  Scenario Outline: Verifying DerProgram Test Execution
    Given I have a get a der program test setup
    When I execute the get a der program test with subject "get_a_der_program"
    Then the test should complete successfully with a der program response containing:
    """
    {
      Test Description : This test is a part of the basic  der programs and control
      test to test that the  der program is present for a particular 
      function set assignemnts , the out comes is either der program found or 
      not for the particular function set assignemnts
    }
    """ 