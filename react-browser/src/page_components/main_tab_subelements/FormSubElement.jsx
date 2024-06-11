import React from "react";
import {
         Text,
         Button,
         Box, 
         Flex,
        SimpleGrid } from '@chakra-ui/react'
import Client from '../../modules/client.js'


function FormSubElement({toggleVar,currentTest,setToggle,setTestState,setCurrentTest,tests,setReport}){  
    const handleSubmitForm = async (event)=>{
      event.preventDefault()
      const validation = Client.getValid(currentTest.test)
      for(let entry of Object.entries(currentTest.object)){
        if(validation[entry[0]] === undefined){
          continue;
        }
        if(!validation[entry[0]](entry[1])){
          console.log('Invalid Field '+entry[0]);
          return;
        }
      }
      setToggle(true)
      setCurrentTest(tests[0])
      const res = await Client.sendTest(currentTest)
      setReport(res);
      setTestState(true);
      }
    return (
      <Flex flexDirection={'column'} maxHeight='100%' hidden={toggleVar}>
        <Text variant={'element_name'}>{currentTest.test} TEST PARAMETERS</Text>
      <Box >
        <form onSubmit={handleSubmitForm}>
        <SimpleGrid columns={2}>
      {Object.entries(currentTest.object).map(t=>(
        <Flex key={t[0]} border={'groove'} borderWidth={'1vh'} flexDirection={'column'} alignItems={'center'}>
        <label key={t[0]+'l'}>
          {t[0]}:
          </label>
          <input  key={t[0]+'i'} id={t[0]} type="text" onChange={(event)=>{
          setCurrentTest(currentTest=>({...currentTest, object:{...currentTest.object, [event.target.id]:event.target.value}}))
        }}/>        
        </Flex>
      ))}
      </SimpleGrid>
      <SimpleGrid columns={2}>
      <Button variant={'submit_form_button'}  type="submit" value="Submit">
        SUBMIT
        </Button>
        <Button variant={'cancel_form_button'} onClick={()=>{
    setTestState(true)
    setToggle(true)
    setCurrentTest(tests[0])
    }}>
      CANCEL
  </Button>
      </SimpleGrid>
        </form>
  </Box>
  </Flex>
    );
  }

  export {FormSubElement}