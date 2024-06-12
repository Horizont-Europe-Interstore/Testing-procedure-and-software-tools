import {GridItem} from '@chakra-ui/react'
import {FormSubElement} from './main_tab_subelements/FormSubElement.jsx';
import {ReportSubElement} from './main_tab_subelements/ReportSubElement.jsx';

function ReportViewer({toggleVar,
                       setToggle,
                       currentTest,
                       setTestState,
                       setCurrentTest,
                       colors,tests,
                       setReport,
                       report,
                       setHeaderState,
                       testState}){
    return(
      <GridItem colSpan={20} rowSpan={26} bg={colors.SECONDARY_COLOR} border='outset' 
                borderColor={colors.PRIMARY_COLOR} borderRadius='0 2% 2% 0' borderWidth='0.8vh' padding={'1vh'} >
         <ReportSubElement report={report}
                           toggleVar={toggleVar} 
                           setToggle={setToggle}
                           colors={colors}/>
         <FormSubElement toggleVar={toggleVar} 
                         setToggle={setToggle} 
                         currentTest={currentTest} 
                         setCurrentTest={setCurrentTest} 
                         setTestState={setTestState}
                         colors={colors}
                         tests={tests}
                         setReport={setReport}
                         setHeaderState={setHeaderState}
                         testState={testState}/>
        </GridItem>
    )
  }


  export {ReportViewer}