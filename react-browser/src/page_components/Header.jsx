import React from "react";
import {
    GridItem, 
    Text,
    Flex, 
    Center} from '@chakra-ui/react'
import {HeaderLoad, HeaderCheck} from './visual_components/logos'

function Header({colors,testState}){
    let hidden = testState;
    React.useEffect(()=>{
      hidden=testState;
    })
    return(
      <GridItem rowSpan={4} colSpan={30} 
      bg={colors.TERTIARY_COLOR}>
        <Flex justifyContent={'center'} flexDirection='row' w='100%' h='100%'>
          <Center justify={'center'}>
            <Text hidden={!hidden}  variant={'header_text'}>Ready</Text>
            <Text hidden={hidden}  variant={'header_text'}>Running Test</Text>
          </Center>
          <Center height='100%'>
           <HeaderLoad colors={colors}
                      testState={testState}/>
            <HeaderCheck colors={colors}
                        testState={testState}/>
          </Center>
        </Flex>
      </GridItem>
    );
  }

export {Header};