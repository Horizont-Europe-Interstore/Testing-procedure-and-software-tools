import React from "react";
import { ChakraProvider,
         GridItem, 
         Grid, 
         Text,
         Button,
         Box, 
         Tooltip, 
         Flex, 
         Center,
        List,
        SimpleGrid } from '@chakra-ui/react'
import tests from './modules/testlist.js'
import loadingImage from '../resources/media/loading.gif'
import checkMark from '../resources/media/check.png'
import {SquareMark,CheckMark,CrossMark} from './visual_components/logos.jsx'
import themeObject from '../resources/chakra_config/theme.ts'

const theme = themeObject.theme
const colors = themeObject.colors

function App(){
  const [isLogs,setLogs] = React.useState('Ready')
  const [testState,setTestState] = React.useState(true)
  const [imagePath,setImagePath] = React.useState(checkMark)
  const [toggleVar,setToggle] = React.useState(true)
  const [currentTest,setCurrentTest] = React.useState(tests[0])
  return(
    <ChakraProvider theme={theme}>
      <Flex flexDirection='row' h='100%' w='100%'>
        <Box h='100%' w= '100%'>
          <Grid h='100%' w='100%'
                templateRows='repeat(30, 1fr)'
                templateColumns='repeat(30, 1fr)'>
            <Header isLogs={isLogs} 
                    setLogs={setLogs} 
                    setTestState={setTestState} 
                    imagePath={imagePath} 
                    setImagePath={setImagePath}/>
            <Controle setTestState={setTestState}
                      testState={testState}
                      setToggle={setToggle}
                      toggleVar={toggleVar}
                      setCurrentTest={setCurrentTest}/>
            <ReportViewer toggleVar={toggleVar}
                          setToggle={setToggle}
                          currentTest={currentTest}
                          setCurrentTest={setCurrentTest}
                          setTestState={setTestState}/> 
          </Grid>
        </Box>
      </Flex>
  </ChakraProvider>
  );
}

function Header({isLogs,setLogs,setTestState,setImagePath,imagePath}){
  const handleGetLogs = React.useCallback((data)=>{
    console.log(data)
    setLogs(data)
    if(data=='Ready'){
      setTestState(true)
      setImagePath(checkMark)
    }
    if(data=='Running' || data == 'Preparing Test'){
      setImagePath(loadingImage)
    }
  })

  React.useEffect(()=>{
    window.electronAPI.getLogs(handleGetLogs)
    return ()=>{
      window.electronAPI.rmEventListener('log-msg')
    }
  })
  
  return(
    <GridItem rowSpan={4} colSpan={30}>
      <Flex flexDirection='row' w='100%' h='100%'>
        <Center w='20%'>
          <img src={imagePath} width='90vw'/>
        </Center>
        <Flex alignItems='center' w='80%'>
          <Text variant={'element_name'}>{isLogs}</Text>
        </Flex>
      </Flex>
    </GridItem>
  );
}

function Controle({setTestState,testState,setToggle,setCurrentTest}) {
  const handleTestSelect = (event) =>{
    if(testState){
      const testObject = tests.filter(x=>x.test === event.target.id)[0]
      if(!testObject.args){
        window.electronAPI.setTest(testObject)
      }
      else{
        setCurrentTest(testObject)
        setToggle(false)
      }
      setTestState(false)
    }
    else{
      console.log('click blocked') //animation here maybe
    }
  }

  return (
    <GridItem colSpan={10} rowSpan={26} padding='1vh' bg={colors.SECONDARY_COLOR} textAlign={'center'}
              border='outset' borderColor={colors.PRIMARY_COLOR} borderRadius='2%' borderWidth='0.8vh'>
      <Text variant={'element_name'}>
        TESTS
      </Text>
      <Flex padding='1vh'  marginTop={'1vh'} flexDirection='column' gap='1vh' border='groove' borderColor={colors.PRIMARY_COLOR}>
      {tests.map(t=>(
        <Tooltip variant={'test_desc_tt'} placement='right-end' hasArrow label={t.desc} key={t.test}>
          <Button id={t.test} variant={'controle_buttons'} onClick={handleTestSelect}>
            {t.test+' Test'}
          </Button>
        </Tooltip>
      ))}
      </Flex>
    </GridItem>
  );
}

function FormSubElement({toggleVar,currentTest,setToggle,setTestState,setCurrentTest}){
  function validateForm(){

  }
  
  const handleSubmitForm = (event)=>{
    event.preventDefault()
    for(let entry of Object.entries(currentTest.object)){
      console.log(entry)
      if(entry[1] === ''){
        console.log('Field cannot be empty')
        return;
      }
      if(/.*Link$/.test(entry[0]) && entry[1][0] !== '/'){
        console.log('Link fields have to start with slash')
        return;
      }
      if((entry[0] === 'sfdi' || entry[0] ==='deviceCategory') && isNaN(entry[1])){
        console.log('Device category and sfdi fields need to be numbers')
        return;
      }
    }
    window.electronAPI.setTest(currentTest)
    setToggle(true)
    setCurrentTest(tests[0])
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

function ReportSubElement({isReport,setReport,toggleVar}){
  const handleGetReport = React.useCallback((report)=>{
    setReport(report)
  })
  React.useEffect(()=>{
    window.electronAPI.getReport(handleGetReport)
    return ()=>{
      window.electronAPI.rmEventListener('report-msg')
    }
  })
  return( <Flex flexDirection={'column'} border={'groove'} borderColor={colors.PRIMARY_COLOR} maxHeight='100%' hidden={!toggleVar}>
  <Box h={'100%'}> 
    <Text variant={'element_name'}>REPORT</Text>
    <Grid templateRows='repeat(10, 1fr)'
          templateColumns='repeat(2, 1fr)' gap={'0vh'} maxHeight={'20vh'}>
      <Flex rowSpan={2} flexDirection={'row'}>
        <Text variant={'report_key'}>
          Feature:
        </Text>
        <Text variant={'report_value'}>
          {isReport.Feature}
        </Text>
      </Flex>
      <Flex rowSpan={2} flexDirection={'row'}>
        <Text variant={'report_key'}>
          Tag:
        </Text>
        <Text variant={'report_value'}>
          {isReport.Tag}
        </Text>
      </Flex>
      <Flex rowSpan={2} flexDirection={'row'}>
        <Text variant={'report_key'}>
          Scenario:
        </Text>
        <Text variant={'report_value'}>
          {isReport.Scenario}
        </Text>
      </Flex>
      <Flex rowSpan={2} flexDirection={'row'}>
        <Text variant={'report_key'}>
          Result:
        </Text>
        <Box marginTop={'2vh'}>
        {
          isReport['End result'] === 'passed' ? <CheckMark/> :  isReport['End result'] == 'failed' ? <CrossMark/> : <></>
        }
        </Box>
        <Text variant={'report_value'}>
          {isReport['End result']}
        </Text>
      </Flex>
    </Grid>
    <Box paddingLeft='1vw' colSpan={2} rowSpan={'8'} fontWeight={'bold'} fontSize={'1xl'}>
        Steps:
        { isReport.Steps == [] ?<List>
        </List>:
        <List>
          {isReport.Steps.map((x,i)=>(
            <Flex flexDirection='column' gap='1vh'>
            <Flex flexDirection={'row'} maxHeight='5vh' gap='0.5vw' paddingLeft='1vw'>
            <Text fontWeight={'bold'} fontSize={'1xl'}>
              {i+'. '}
            </Text>
            <Box marginTop={'0.5vh'}>
            {
              x.Result == 'passed' ? <CheckMark/> : x.Result == 'failed'? <CrossMark/> :<SquareMark/>
            }
            </Box>
            <Text fontsize={'small'} textColor={colors.PRIMARY_COLOR}>
              {x.Keyword}
            </Text>
            <Text fontSize='small' marginTop={'0.3vh'} fontWeight='light'>
              {x.Name}
            </Text>
          </Flex>
          <Text maxHeight={'15vh'}  textColor={'black'} bgColor={colors.BAD_COLOR} fontSize={'smaller'} fontWeight={'lighter'} overflowY={'scroll'}>
            {x['Error message'] ? x['Error message'] : ''}
          </Text>
          <Text maxHeight={'15vh'}  bgColor={colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'} overflowY={'scroll'}>
            {x['Value'] ? x['Value'] : ''}
          </Text>
          </Flex>
          ))}
        </List>
        }
      </Box>
  </Box>
  </Flex>)
}

function ReportViewer({toggleVar,setToggle,currentTest,setTestState,setCurrentTest}){
  const [isReport,setReport] = React.useState({
    'Feature':'...',
    'Tag':'...',
    'End result':'...',
    'Scenario':'...',
    'Steps':[]
  })
  return(
    <GridItem colSpan={20} rowSpan={26} bg={colors.SECONDARY_COLOR} border='outset' 
              borderColor={colors.PRIMARY_COLOR} borderRadius='2%' borderWidth='0.8vh' padding={'1vh'} >
       <ReportSubElement isReport={isReport} setReport={setReport} toggleVar={toggleVar} setToggle={setToggle}/>
       <FormSubElement toggleVar={toggleVar} setToggle={setToggle} currentTest={currentTest} setCurrentTest={setCurrentTest} setTestState={setTestState}/>
      </GridItem>
  )
}

export {App}
