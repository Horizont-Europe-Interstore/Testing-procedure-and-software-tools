import {
    GridItem,
    Text,
    Button,
    Tooltip, 
    Flex } from '@chakra-ui/react'
import Client from '../modules/client.js'


function Controle({setTestState,testState,setToggle,setCurrentTest,colors,tests,setReport}) {
    const handleTestSelect = async (event) =>{
      if(testState){
        setTestState(false)
        const testObject = tests[event.target.id];
        if(!testObject.args){
          const res = await Client.sendTest(testObject);
          setReport(res)
          setTestState(true)
        }
        else{
          setCurrentTest(testObject)
          setToggle(false)
        }
      }
      else{
        console.log('click blocked') //animation here maybe
      }
    }
  
    return (
      <GridItem colSpan={10} rowSpan={26} padding='1vh' bg={colors.SECONDARY_COLOR} textAlign={'center'}
                border='outset' borderColor={colors.PRIMARY_COLOR} borderRadius='2% 0 0 2%' borderWidth='0.8vh'>
        <Text variant={'element_name'}>
          TESTS
        </Text>
        <Flex padding='1vh'  marginTop={'1vh'} flexDirection='column' gap='1vh' border='groove' borderColor={colors.PRIMARY_COLOR}>
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