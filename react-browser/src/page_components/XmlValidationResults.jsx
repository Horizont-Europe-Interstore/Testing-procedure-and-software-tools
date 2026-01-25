import React, { useState, useEffect } from 'react';
import {
  Box,
  VStack,
  HStack,
  Text,
  Badge,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
  Button,
  Textarea,
  GridItem
} from '@chakra-ui/react';
import Client from '../modules/client.js';

const XmlValidationResults = ({ colors }) => {
  const [validationResults, setValidationResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchValidationResults();
    const interval = setInterval(fetchValidationResults, 2000); // Auto-refresh every 2 seconds
    return () => clearInterval(interval);
  }, []);

  const fetchValidationResults = async () => {
    setLoading(true);
    try {
      const data = await Client.getXmlValidationResults();
      setValidationResults(data);
    } catch (error) {
      console.error('Error fetching validation results:', error);
    } finally {
      setLoading(false);
    }
  };

  const clearResults = async () => {
    const success = await Client.clearXmlValidationResults();
    if (success) {
      setValidationResults([]);
    }
  };

  const formatTimestamp = (timestamp) => {
    return new Date(timestamp).toLocaleString();
  };

  return (
    <GridItem colSpan={20} rowSpan={26} padding='1vh' bg={colors.SECONDARY_COLOR}
              border='outset' borderColor={colors.PRIMARY_COLOR} borderRadius='0 2% 2% 0' borderWidth='0.8vh'>
      <Box padding='1vh' maxHeight={'100%'} marginTop={'1vh'} 
           border='groove' borderColor={colors.PRIMARY_COLOR} overflowY={'scroll'}>
        
        <HStack justify="space-between" mb={4}>
          <Text fontSize="xl" fontWeight="bold" color={colors.PRIMARY_COLOR}>XML Validation Results</Text>
          <HStack>
            <Button onClick={fetchValidationResults} isLoading={loading} size="sm">
              Refresh
            </Button>
            <Button onClick={clearResults} colorScheme="red" variant="outline" size="sm">
              Clear All
            </Button>
          </HStack>
        </HStack>

        {validationResults.length === 0 ? (
          <Text color="gray.500">No validation results available</Text>
        ) : (
          <VStack spacing={3} align="stretch">
            {validationResults.map((result) => (
              <Box key={result.id} border="1px" borderColor="gray.300" borderRadius="md" p={3}>
                <HStack justify="space-between" mb={2}>
                  <HStack>
                    <Badge colorScheme={result.valid ? 'green' : 'red'} fontSize="xs">
                      {result.valid ? 'VALID' : 'INVALID'}
                    </Badge>
                    <Text fontWeight="semibold" fontSize="sm">{result.httpMethod} {result.endpoint}</Text>
                  </HStack>
                  <Text fontSize="xs" color="gray.500">
                    {formatTimestamp(result.timestamp)}
                  </Text>
                </HStack>

                <Accordion allowToggle size="sm">
                  <AccordionItem>
                    <AccordionButton py={1}>
                      <Box flex="1" textAlign="left">
                        <Text fontWeight="medium" fontSize="sm">Request XML</Text>
                      </Box>
                      <AccordionIcon />
                    </AccordionButton>
                    <AccordionPanel pb={2}>
                      <Textarea
                        value={result.requestXml || 'No request body'}
                        isReadOnly
                        minH="60px"
                        fontFamily="monospace"
                        fontSize="xs"
                      />
                    </AccordionPanel>
                  </AccordionItem>

                  <AccordionItem>
                    <AccordionButton py={1}>
                      <Box flex="1" textAlign="left">
                        <Text fontWeight="medium" fontSize="sm">Expected XML</Text>
                      </Box>
                      <AccordionIcon />
                    </AccordionButton>
                    <AccordionPanel pb={2}>
                      <Textarea
                        value={result.expectedXml}
                        isReadOnly
                        minH="120px"
                        fontFamily="monospace"
                        fontSize="xs"
                      />
                    </AccordionPanel>
                  </AccordionItem>

                  <AccordionItem>
                    <AccordionButton py={1}>
                      <Box flex="1" textAlign="left">
                        <Text fontWeight="medium" fontSize="sm">Actual XML Response</Text>
                      </Box>
                      <AccordionIcon />
                    </AccordionButton>
                    <AccordionPanel pb={2}>
                      <Textarea
                        value={result.actualXml}
                        isReadOnly
                        minH="120px"
                        fontFamily="monospace"
                        fontSize="xs"
                      />
                    </AccordionPanel>
                  </AccordionItem>

                  {!result.valid && (
                    <AccordionItem>
                      <AccordionButton py={1}>
                        <Box flex="1" textAlign="left">
                          <Text fontWeight="medium" fontSize="sm" color="red.500">Differences</Text>
                        </Box>
                        <AccordionIcon />
                      </AccordionButton>
                      <AccordionPanel pb={2}>
                        <Box bg="red.50" p={2} borderRadius="md">
                          <Text fontFamily="monospace" fontSize="xs" whiteSpace="pre-wrap">
                            {result.differences}
                          </Text>
                        </Box>
                      </AccordionPanel>
                    </AccordionItem>
                  )}
                </Accordion>
              </Box>
            ))}
          </VStack>
        )}
      </Box>
    </GridItem>
  );
};

export default XmlValidationResults;