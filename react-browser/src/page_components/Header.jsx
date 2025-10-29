import React from "react";
import {
    GridItem, 
    Text,
    Flex, 
    Center} from '@chakra-ui/react'
import {HeaderLoad, HeaderCheck, HeaderCross} from './visual_components/logos'

function Header({colors,headerState}){
  const cVisElemIdx = headerState.visElemIdx;
    return(
      <GridItem rowSpan={4} colSpan={30} 
      bg={colors.TERTIARY_COLOR}>
        <Flex justifyContent={'center'} flexDirection='row' w='100%' h='100%'>
          <Center justify={'center'}>
            <Text variant={'header_text'}>{headerState.text}</Text>
          </Center>
          <Center height='100%' width='10vw'>
           <HeaderLoad colors={colors}
                      cVisElemIdx={cVisElemIdx}/>
            <HeaderCheck colors={colors}
                        cVisElemIdx={cVisElemIdx}/>
            <HeaderCross colors={colors}
                        cVisElemIdx={cVisElemIdx}/>
          </Center>
        </Flex>
      </GridItem>
    );
  }

export {Header};