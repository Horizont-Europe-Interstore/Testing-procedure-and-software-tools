import {
  GridItem,
  Text,
  Button,
  Tooltip,
  Flex,
  VStack,
  Divider
} from '@chakra-ui/react'
import Client from '../modules/client.js'

function Controle({
  setTestState,
  testState,
  setToggle,
  setCurrentTest,
  colors,
  tests,
  setReport,
  setHeaderState,
  headerState,
  setCurrentView,  // Add this prop
  currentView      // Add this prop
}) {
  let tmpTimeout;

  const handleTestSelect = async (event) => {
    if(tmpTimeout !== undefined){
      clearTimeout(tmpTimeout);
    }
    if(testState){
      setTestState(false)
      const testObject = tests[event.target.id];
      if(!testObject.args){
        setHeaderState({text:'Running Test',visElemIdx:2})
        const res = await Client.sendTest(testObject);
        setReport(res)
        setHeaderState({text:'Ready',visElemIdx:0})
        setTestState(true)
      }
      else{
        setCurrentTest(testObject)
        setToggle(false)
      }
    }
    else{
      let cState=headerState;
      setHeaderState({text:'Another operation is in progress',visElemIdx:1});
      tmpTimeout=setTimeout(()=>{
        setHeaderState(cState)
      },1000);
    }
  }

  // Handle view switching
  const handleViewChange = (view) => {
    setCurrentView(view);
    setHeaderState({
      text: view === 'reports' ? 'Ready' : 'XML Test Results',
      visElemIdx: view === 'reports' ? 0 : 1
    });
  };

  return (
    <GridItem colSpan={10} rowSpan={26} padding='1vh' bg={colors.SECONDARY_COLOR} textAlign={'center'}
              border='outset' borderColor={colors.PRIMARY_COLOR} borderRadius='2% 0 0 2%' borderWidth='0.8vh'>
      <Flex padding='1vh' maxHeight={'100%'} marginTop={'1vh'} flexDirection='column' gap='1vh' 
            border='groove' borderColor={colors.PRIMARY_COLOR} overflowY={'scroll'}>
        
        {/* View Toggle Buttons */}
        <VStack spacing={2} width="100%">
          <Text variant={'element_name'}>VIEWS</Text>
          
          <Button 
            onClick={() => handleViewChange('reports')}
            colorScheme={currentView === 'reports' ? 'blue' : 'gray'}
            variant={currentView === 'reports' ? 'solid' : 'outline'}
            size="sm"
            width="full"
            fontSize="xs"
          >
            📊 Test Reports
          </Button>
          
          <Button 
            onClick={() => handleViewChange('xmlResults')}
            colorScheme={currentView === 'xmlResults' ? 'blue' : 'gray'}
            variant={currentView === 'xmlResults' ? 'solid' : 'outline'}
            size="sm"
            width="full"
            fontSize="xs"
          >
            🔍 XML Results
          </Button>
        </VStack>

        <Divider borderColor={colors.PRIMARY_COLOR} />

        {/* Tests Section - Only show when in reports view */}
        {currentView === 'reports' && (
          <>
            <Text variant={'element_name'}>TESTS</Text>
            {tests.map(t=>(
              <Tooltip variant={'test_desc_tt'} placement='right-end' hasArrow label={t.desc} key={t.test}>
                <Button id={t.index} variant={'controle_buttons'} onClick={handleTestSelect}>
                  {t.test+' Test'}
                </Button>
              </Tooltip>
            ))}
          </>
        )}

        {/* XML Results Info - Only show when in XML view */}
        {currentView === 'xmlResults' && (
          <VStack spacing={2} align="stretch">
            <Text variant={'element_name'} fontSize="sm">XML VALIDATION</Text>
            <Text fontSize="xs" color="gray.600" textAlign="left" px={2}>
              • Live XML test results
              • NATS message validation  
              • Expected vs Actual comparison
              • HTTP polling updates
            </Text>
          </VStack>
        )}

      </Flex>
    </GridItem>
  );
}

export {Controle};