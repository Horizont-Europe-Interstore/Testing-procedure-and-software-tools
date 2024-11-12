import {
  GridItem,
  Text,
  Button,
  Tooltip, 
  Flex } from '@chakra-ui/react'
import Client from '../modules/client.js'


function Controle({setTestState,testState,setToggle,setCurrentTest,colors,tests,setReport,setHeaderState,headerState}) {
  let tmpTimeout;
  const handleTestSelect = async (event) =>{
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

  return (
    <GridItem colSpan={10} rowSpan={26} padding='1vh' bg={colors.SECONDARY_COLOR} textAlign={'center'}
              border='outset' borderColor={colors.PRIMARY_COLOR} borderRadius='2% 0 0 2%' borderWidth='0.8vh'>
      <Flex padding='1vh' maxHeight={'100%'}  marginTop={'1vh'} flexDirection='column' gap='1vh' border='groove' borderColor={colors.PRIMARY_COLOR} overflowY={'scroll'}>
      <Text variant={'element_name'}>
        TESTS
      </Text>
      {tests.map(t=>(
        <Tooltip variant={'test_desc_tt'} placement='right-end' hasArrow label={t.desc} key={t.test}>
          <Button id={t.index} variant={'controle_buttons'} onClick={handleTestSelect}>
            {t.test+' Test'}
          </Button>
        </Tooltip>
      ))}
      </Flex>
    </GridItem>
  );
}

export {Controle};