Feature: Get Registered EndDevice 
  @GetRegisteredEndDevice
  Scenario Outline: Verifying an EndDevice is Registered
    Given I have to find registered an end device test setup
    When I execute the find registered end device test with service name "findallregistrededendevice" and subject "get_registered_end_device"
    Then the test should complete successfully with find RegisterEndDevice response containing:
    """

      {
      Test Description : This is part of the Basic End Device  Test, once
      there is an endDevice is present in the server it has to register this
      test will find out the a particular registered end device with a pin 
      
    }
    
    """