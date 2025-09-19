import {CheckMark,CrossMark} from '../visual_components/logos.jsx'
import {
    Grid, 
    Text,
    Box ,
    Flex,} from '@chakra-ui/react'
import React from "react";
  function ReportSubElement({report,toggleVar,colors}){
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
            {report.Feature}
          </Text>
        </Flex>
        <Flex rowSpan={2} flexDirection={'row'}>
          <Text variant={'report_key'}>
            Result:
          </Text>
          <Box marginTop={'2.4vh'} marginLeft={'0.3vw'}>
          {
            report['End result'] === 'passed' ? <CheckMark/> :  report['End result'] === 'failed' ? <CrossMark/> : <></>
          }
          </Box>
          <Text variant={'report_value'}>
            {report['End result']}
          </Text>
        </Flex>
      </Grid>
      {
        report["Feature"] !== '...' ?
      <Box paddingLeft='1vw' colSpan={2} rowSpan={'8'} fontWeight={'bold'} fontSize={'1xl'}>
        <Text variant={'report_key'} textAlign={'center'}>
            DESCRIPTION
        </Text>
        <Text maxHeight={'15vh'}  bgColor={colors.TERTIARY_COLOR} fontSize={'smaller'} fontWeight={'lighter'}>
          {report['Description']}
        </Text>
        <Flex flexDirection={'row'} w={'100%'}>
          <Flex flexDirection={'column'} w={'100%'}>
            <Text variant={'report_key'} textAlign={'center'}>
              RESPONSE
            </Text>
            <Text bgColor={report['End result'] === 'failed' ? colors.BAD_COLOR : colors.GOOD_COLOR} fontSize={'smaller'} fontWeight={'lighter'}>
            {report['Actual response']}
            </Text>
          </Flex>
          
        </Flex>
      </Box> :<></>
    }
    </Box>
    </Flex>)
  }

export {ReportSubElement}