Feature: Create End Device
  @CreateEndDevice
  Scenario Outline: Verifying Create End Device Test execution
    Given I have a create end device test setup
    When I execute the create end device test with service name "createnewenddevice" and subject "<natsSubject>"
    Then the test should complete successfully with CreateEndDevice response containing:
    """
    Test Description: This test is part Basic End Device test to create an End Device , the operator  
                   shall have a possiblity to create the End Device the End Device have attributes 
                   and that is needed to be entered once clicking submit button , the end device is 
                   added to the test application database . To verify that the End Device is entered 
                   correctly by verfy it's attributes , the test returns the LFDI , SFDI of the End Devices 
                   and along with sample or expected outcome . 	
    """

    