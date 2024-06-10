import {SquareMark,CheckMark,CrossMark} from '../visual_components/logos.jsx'
import {
    Grid, 
    Text,
    Box ,
    Flex,
   List } from '@chakra-ui/react'
import React from "react";
  
  function ReportSubElement({isReport,setReport,toggleVar,colors}){
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
          <Box marginTop={'2.4vh'} marginLeft={'0.3vw'}>
          {
            isReport['End result'] === 'passed' ? <CheckMark/> :  isReport['End result'] === 'failed' ? <CrossMark/> : <></>
          }
          </Box>
          <Text variant={'report_value'}>
            {isReport['End result']}
          </Text>
        </Flex>
      </Grid>
      <Box paddingLeft='1vw' colSpan={2} rowSpan={'8'} fontWeight={'bold'} fontSize={'1xl'}>
          Steps:
          { isReport.Steps.length === 0 ?<List>
          </List>:
          <List>
            {isReport.Steps.map((x,i)=>(
              <Flex flexDirection='column' gap='1vh' key={i}>
              <Flex flexDirection={'row'} maxHeight='5vh' gap='0.5vw' paddingLeft='1vw'>
              <Box marginTop={'0.8vh'}>
              {
                x.Result === 'passed' ? <CheckMark/> : x.Result === 'failed'? <CrossMark/> :<SquareMark/>
              }
              </Box>
            </Flex>
            <Text maxHeight={'15vh'}  bgColor={colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'} overflowY={'scroll'}>
              {x['test_description']}
            </Text>
            <Text maxHeight={'15vh'}  bgColor={colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'} overflowY={'scroll'}>
              {x['expected_result']}
            </Text>
            <Text maxHeight={'15vh'}  bgColor={colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'} overflowY={'scroll'}>
              {x['actual_result']}
            </Text>
            </Flex>
            ))}
          </List>
          }
        </Box>
    </Box>
    </Flex>)
  }

export {ReportSubElement}