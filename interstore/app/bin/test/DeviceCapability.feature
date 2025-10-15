Feature: Device Capability Business Rules

Background:
  * def NatsSubscriber = Java.type('interstore.NatsSubscriber')
  * def natsSubscriber = new NatsSubscriber('nats://nats-server:4222')
  * def XmlCache = Java.type('interstore.XmlCache')
  * def TestResult = Java.type('interstore.TestResults.TestResult')

Scenario: Core device capabilities present
  * def natsMessage = karate.properties['nats.message']
  * print 'NATS Message received'
  
  # Check if message exists
  * match natsMessage != null
  
  # Get expected XML
  * def expectedXml = XmlCache.selectByRootElement(natsMessage)
  * match expectedXml != null
  * print 'Expected XML auto-selected based on root element'
  
  # Your validations
  * match natsMessage contains 'DeviceCapability'
  * match natsMessage contains 'SelfDeviceLink'
  * match natsMessage contains 'EndDeviceListLink'
  * match natsMessage contains 'href="/sdev"'
  * match natsMessage contains 'href="/edev"'
  
  # Create JSON string directly
  * def testId = java.util.UUID.randomUUID()
  * def timestamp = java.time.Instant.now()
  * def testResultJson = '{"testId":"' + testId + '","testName":"DeviceCapability Validation","timestamp":"' + timestamp + '","status":"PASSED","reason":"All core elements and href values are correct"}'
  
  # Send results directly to TestResult
  * print 'Test Result JSON:', testResultJson
  * TestResult.storeTestResult(testResultJson, natsMessage, expectedXml)
  
  # Print results
  * print '✅ Test PASSED: Root element matched perfectly'
  * print '✅ Results sent with raw XML content'