import React from "react";
import { ChakraProvider,
         Grid,
         Box,
         Flex } from '@chakra-ui/react'
import themeObject from './resources/chakra_config/theme.ts'
import {Header} from './page_components/Header.jsx';
import {ReportViewer} from './page_components/MainTab.jsx';
import {Controle} from './page_components/SideBar.jsx';
import Client from './modules/client.js'

function App(){
  const theme = themeObject.theme;
  const colors = themeObject.colors;
  const tests = Client.getTests();
  const [testState,setTestState] = React.useState(true);
  const [toggleVar,setToggle] = React.useState(true);
  const [currentTest,setCurrentTest] = React.useState(tests[0]);
  
  const [isReport,setReport] = React.useState({
    'Feature':'...',
    'Tag':'...',
    'End result':'...',
    'Scenario':'...',
    'Description': '...',
    'Actual response': '...',
    'Expected response': '...'
  });

  return(
    <ChakraProvider theme={theme}>
      <Flex flexDirection='row' h='100%' w='100%'>
        <Box h='100%' w= '100%'>
          <Grid h='100%' w='100%'
                templateRows='repeat(30, 1fr)'
                templateColumns='repeat(30, 1fr)'>
            <Header colors={colors}
                    testState={testState}/>
            <Controle setTestState={setTestState}
                      testState={testState}
                      setToggle={setToggle}
                      toggleVar={toggleVar}
                      setCurrentTest={setCurrentTest}
                      colors={colors}
                      tests={tests}
                      setReport={setReport}/>
            <ReportViewer toggleVar={toggleVar}
                          setToggle={setToggle}
                          currentTest={currentTest}
                          setCurrentTest={setCurrentTest}
                          setTestState={setTestState}
                          colors={colors}
                          tests={tests}
                          setReport={setReport}
                          isReport={isReport}/> 
          </Grid>
        </Box>
      </Flex>
  </ChakraProvider>
  );
}

export {App}
