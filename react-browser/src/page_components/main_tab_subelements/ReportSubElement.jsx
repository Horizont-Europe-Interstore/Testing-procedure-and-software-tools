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
      {
        isReport["Feature"] !== '...' ?
      <Box paddingLeft='1vw' colSpan={2} rowSpan={'8'} fontWeight={'bold'} fontSize={'1xl'}>
        <Text variant={'report_key'} textAlign={'center'}>
            DESCRIPTION
        </Text>
        <Text maxHeight={'15vh'}  bgColor={colors.TERTIARY_COLOR} fontSize={'smaller'} fontWeight={'lighter'}>
          {isReport['Description']}
        </Text>
        <Flex flexDirection={'row'} w={'100%'}>
          <Flex flexDirection={'column'} w={'100%'}>
            <Text variant={'report_key'} textAlign={'center'}>
              ACTUAL
            </Text>
            <Text bgColor={isReport['End result'] === 'failed' ? colors.BAD_COLOR : colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'}>
            {isReport['Actual response']}
            </Text>
          </Flex>
          <Flex flexDirection={'column'} colSpan={2} w={'100%'}>
            <Text variant={'report_key'} textAlign={'center'}>
              EXPECTED
            </Text>
            <Text bgColor={colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'}>
            {isReport['Expected response']}
            </Text>
          </Flex>
        </Flex>
      </Box> :<></>
    }
    </Box>
    </Flex>)
  }

export {ReportSubElement}