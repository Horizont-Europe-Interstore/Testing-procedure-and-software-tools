import React from "react";
import {
         Text,
         Button,
         Box, 
         Flex,
        SimpleGrid } from '@chakra-ui/react'
import Client from '../../modules/client.js'


function FormSubElement({toggleVar,
                         currentTest,
                         setToggle,
                         setTestState,
                         setCurrentTest,
                         tests,
                         setReport,
                         setHeaderState,
                         testState}){  
    const handleSubmitForm = async (event)=>{
      event.preventDefault()
      let tmpTimeout;
      const validation = Client.getValid(currentTest.test)
      for(let entry of Object.entries(currentTest.object)){
        let key = entry[0].slice(0,1).toLowerCase()+entry[0].slice(1,entry[0].length).replace(' ','');
        console.log(key);
        
        if(entry[1]==='' || (validation[key]!==undefined&&!validation[key](entry[1]))){
            setHeaderState({text:'Invalid Field '+entry[0],visElemIdx:1});
            tmpTimeout=setTimeout(()=>{
              if(!testState){
                setHeaderState({text:'Ready',visElemIdx:0});
                return;
              }
              setHeaderState({text:'Running Test', visElemIdx:2});
            },1000);
            return;
        }
      }
      if(tmpTimeout !== undefined){clearTimeout(tmpTimeout);}
      setTestState(false);
      setHeaderState({text:'Running Test',visElemIdx:2})
      setCurrentTest(tests[0]);
      setToggle(true);
      const res = await Client.sendTest(currentTest);
      setReport(res);
      setHeaderState({text:'Ready',visElemIdx:0})
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