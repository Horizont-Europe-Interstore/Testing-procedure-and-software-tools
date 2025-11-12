import {GridItem, Box, Text, RadioGroup, Stack, Radio, Flex} from '@chakra-ui/react'
import {FormSubElement} from './main_tab_subelements/FormSubElement.jsx';
import {ReportSubElement} from './main_tab_subelements/ReportSubElement.jsx';
import { DerCurveForm } from './main_tab_subelements/DerCurveSubForm.jsx';
import {DerControlSubForm} from "./main_tab_subelements/DerControlSubForm";
import { DerForm } from './main_tab_subelements/DerForm.jsx';
import { useState, useMemo, useEffect } from 'react';
import Client from "../modules/client.js";

function ReportViewer({
  toggleVar,
  setToggle,
  currentTest,
  setCurrentTest,
  setTestState,
  colors,
  tests,
  setReport,
  report,
  setHeaderState,
  testState,
}) {
  const [formMode, setFormMode] = useState("der");
  
  const allTests = Client.getTests();
  const getTestByName = (name) => allTests.find(t => t.test === name);
  const derTest = useMemo(() => getTestByName("Create Der"), [allTests]);
  const capabilityTest = useMemo(() => getTestByName("Create Der Capability"), [allTests]);
  const settingsTest = useMemo(() => getTestByName("Create Der Settings"), [allTests]);

  return (
    <GridItem
      colSpan={20}
      rowSpan={26}
      bg={colors.SECONDARY_COLOR}
      border="outset"
      borderColor={colors.PRIMARY_COLOR}
      borderRadius="0 2% 2% 0"
      borderWidth="0.8vh"
      padding="1vh"
    >
      {/* MAIN CONTAINER - Full height, column layout */}
      <Flex flexDirection="column" height="100%" maxHeight="100%" gap={2}>

        {/* REPORT - Always visible at top */}
        <Box flexShrink={0}>
          <ReportSubElement report={report} toggleVar={toggleVar} setToggle={setToggle} colors={colors} />
        </Box>

        {/* SCROLLABLE FORM AREA - This fixes everything */}
        <Box flex="1" overflowY="auto" pr={2} pb={2}>
          
          {/* SPECIAL CASE: DerForm */}
          {currentTest.test === "Create Der" ? (
            <DerForm
              toggleVar={toggleVar}
              currentTest={currentTest}
              setCurrentTest={setCurrentTest}
              setToggle={setToggle}
              setReport={setReport}
              setTestState={setTestState}
              setHeaderState={setHeaderState}
              tests={tests}
              testState={testState}
            />
          ) : (
            /* ALL OTHER FORMS - Render normally */
            <>
              {currentTest.test === "Create Der Curve" && (
                <DerCurveForm
                  toggleVar={toggleVar}
                  setToggle={setToggle}
                  currentTest={currentTest}
                  setCurrentTest={setCurrentTest}
                  setTestState={setTestState}
                  colors={colors}
                  tests={tests}
                  setReport={setReport}
                  setHeaderState={setHeaderState}
                  testState={testState}
                />
              )}
              {currentTest.test === "Create Der Control" && (
                <DerControlSubForm
                  toggleVar={toggleVar}
                  currentTest={currentTest}
                  setToggle={setToggle}
                  setTestState={setTestState}
                  setCurrentTest={setCurrentTest}
                  setReport={setReport}
                  setHeaderState={setHeaderState}
                  testState={testState}
                  tests={tests}
                />
              )}
              {currentTest.test !== "Create Der" && 
               currentTest.test !== "Create Der Curve" && 
               currentTest.test !== "Create Der Control" && (
                <FormSubElement
                  toggleVar={toggleVar}
                  setToggle={setToggle}
                  currentTest={currentTest}
                  setCurrentTest={setCurrentTest}
                  setTestState={setTestState}
                  colors={colors}
                  tests={tests}
                  setReport={setReport}
                  setHeaderState={setHeaderState}
                  testState={testState}
                />
              )}
            </>
          )}
        </Box>
      </Flex>
    </GridItem>
  );
}


  export {ReportViewer}