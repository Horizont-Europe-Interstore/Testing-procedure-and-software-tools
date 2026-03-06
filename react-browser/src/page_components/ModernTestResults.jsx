import React, { useState, useEffect } from 'react';
import {
  Box,
  VStack,
  HStack,
  Heading,
  Text,
  Button,
  Card,
  CardHeader,
  CardBody,
  Badge,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
  Code,
  Divider,
  useToast,
  Grid,
  Flex,
  Spinner,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription
} from '@chakra-ui/react';
import Client from '../modules/client.js';

export function ModernTestResults({ onBack }) {
  const [validationResults, setValidationResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [autoRefresh, setAutoRefresh] = useState(true);
  const toast = useToast();

  useEffect(() => {
    fetchValidationResults();

    if (autoRefresh) {
      const interval = setInterval(fetchValidationResults, 3000); // Refresh every 3 seconds
      return () => clearInterval(interval);
    }
  }, [autoRefresh]);

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
      toast({
        title: 'Results Cleared',
        description: 'All test results have been cleared successfully',
        status: 'success',
        duration: 2000,
        isClosable: true,
      });
    }
  };

  const formatTimestamp = (timestamp) => {
    return new Date(timestamp).toLocaleString();
  };

  const parseXmlDifferences = (differences) => {
    if (!differences) return [];

    // Parse the differences string to extract missing/extra fields
    const lines = differences.split('\n').filter(line => line.trim());
    const parsedDiffs = [];

    lines.forEach(line => {
      if (line.includes('Missing in actual')) {
        parsedDiffs.push({
          type: 'missing',
          field: line.replace('Missing in actual:', '').trim(),
          severity: 'error'
        });
      } else if (line.includes('Extra in actual')) {
        parsedDiffs.push({
          type: 'extra',
          field: line.replace('Extra in actual:', '').trim(),
          severity: 'warning'
        });
      } else if (line.includes('Value mismatch')) {
        parsedDiffs.push({
          type: 'mismatch',
          field: line.replace('Value mismatch:', '').trim(),
          severity: 'error'
        });
      }
    });

    return parsedDiffs;
  };

  return (
    <Box p={6} bg="white" minH="100vh">
      <VStack spacing={6} align="stretch">
        {/* Header */}
        <Flex justify="space-between" align="center">
          <VStack align="start" spacing={1}>
            <Heading size="xl" color="gray.700">
              Test Results & XML Validation
            </Heading>
            <Text color="gray.600" fontSize="md">
              Compare device responses with expected server payloads
            </Text>
          </VStack>

          <HStack spacing={3}>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setAutoRefresh(!autoRefresh)}
              colorScheme={autoRefresh ? 'green' : 'gray'}
            >
              {autoRefresh ? '🔄 Auto-Refresh ON' : '⏸️ Auto-Refresh OFF'}
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={fetchValidationResults}
              isLoading={loading}
            >
              🔄 Refresh Now
            </Button>
            <Button
              colorScheme="red"
              variant="outline"
              size="sm"
              onClick={clearResults}
            >
              🗑️ Clear All
            </Button>
            <Button
              colorScheme="blue"
              size="sm"
              onClick={onBack}
            >
              ← Back to Tests
            </Button>
          </HStack>
        </Flex>

        <Divider />

        {/* Stats Summary */}
        {validationResults.length > 0 && (
          <Grid templateColumns="repeat(4, 1fr)" gap={4}>
            <Card>
              <CardBody>
                <VStack spacing={1}>
                  <Text fontSize="2xl" fontWeight="bold" color="blue.500">
                    {validationResults.length}
                  </Text>
                  <Text fontSize="sm" color="gray.600">Total Tests</Text>
                </VStack>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <VStack spacing={1}>
                  <Text fontSize="2xl" fontWeight="bold" color="green.500">
                    {validationResults.filter(r => r.valid).length}
                  </Text>
                  <Text fontSize="sm" color="gray.600">Passed</Text>
                </VStack>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <VStack spacing={1}>
                  <Text fontSize="2xl" fontWeight="bold" color="red.500">
                    {validationResults.filter(r => !r.valid).length}
                  </Text>
                  <Text fontSize="sm" color="gray.600">Failed</Text>
                </VStack>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <VStack spacing={1}>
                  <Text fontSize="2xl" fontWeight="bold" color="purple.500">
                    {Math.round((validationResults.filter(r => r.valid).length / validationResults.length) * 100)}%
                  </Text>
                  <Text fontSize="sm" color="gray.600">Success Rate</Text>
                </VStack>
              </CardBody>
            </Card>
          </Grid>
        )}

        {/* Results List */}
        {loading && validationResults.length === 0 ? (
          <Flex justify="center" align="center" h="40vh">
            <VStack spacing={4}>
              <Spinner size="xl" color="blue.500" thickness="4px" />
              <Text color="gray.500">Loading test results...</Text>
            </VStack>
          </Flex>
        ) : validationResults.length === 0 ? (
          <Alert
            status="info"
            variant="subtle"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            textAlign="center"
            height="40vh"
            borderRadius="md"
          >
            <AlertIcon boxSize="40px" mr={0} />
            <AlertTitle mt={4} mb={1} fontSize="lg">
              No Test Results Yet
            </AlertTitle>
            <AlertDescription maxWidth="sm">
              Run tests from the configuration menu to see validation results here.
              The system will compare device responses with expected server payloads automatically.
            </AlertDescription>
          </Alert>
        ) : (
          <VStack spacing={4} align="stretch">
            {validationResults.map((result, index) => {
              const differences = parseXmlDifferences(result.differences);
              const hasMissingFields = differences.some(d => d.type === 'missing');

              return (
                <Card
                  key={result.id || index}
                  borderWidth="2px"
                  borderColor={result.valid ? 'green.200' : 'red.200'}
                  bg={result.valid ? 'green.50' : 'red.50'}
                >
                  <CardHeader>
                    <Flex justify="space-between" align="center">
                      <HStack spacing={3}>
                        <Badge
                          colorScheme={result.valid ? 'green' : 'red'}
                          fontSize="md"
                          px={3}
                          py={1}
                          borderRadius="md"
                        >
                          {result.valid ? '✓ VALID' : '✗ INVALID'}
                        </Badge>
                        <VStack align="start" spacing={0}>
                          <Text fontWeight="bold" fontSize="lg">
                            {result.httpMethod} {result.endpoint}
                          </Text>
                          <Text fontSize="sm" color="gray.600">
                            {formatTimestamp(result.timestamp)}
                          </Text>
                        </VStack>
                      </HStack>

                      {!result.valid && hasMissingFields && (
                        <Badge colorScheme="orange" fontSize="sm">
                          Missing Fields Detected
                        </Badge>
                      )}
                    </Flex>
                  </CardHeader>

                  <CardBody pt={0}>
                    <Accordion allowToggle>
                      {/* XML Comparison */}
                      <AccordionItem border="none">
                        <AccordionButton
                          bg="white"
                          _hover={{ bg: 'gray.100' }}
                          borderRadius="md"
                          mb={2}
                        >
                          <Box flex="1" textAlign="left">
                            <HStack>
                              <Text fontWeight="semibold">📄 XML Comparison</Text>
                              {!result.valid && (
                                <Badge colorScheme="red" fontSize="xs">
                                  {differences.length} differences found
                                </Badge>
                              )}
                            </HStack>
                          </Box>
                          <AccordionIcon />
                        </AccordionButton>
                        <AccordionPanel pb={4}>
                          <Grid templateColumns="1fr 1fr" gap={4}>
                            {/* Expected XML */}
                            <Box>
                              <Text fontWeight="bold" mb={2} color="blue.600">
                                Expected XML (Server)
                              </Text>
                              <Code
                                display="block"
                                whiteSpace="pre-wrap"
                                fontSize="xs"
                                p={3}
                                bg="blue.50"
                                borderRadius="md"
                                maxH="400px"
                                overflow="auto"
                                fontFamily="Consolas, Monaco, monospace"
                                border="2px solid"
                                borderColor="blue.200"
                              >
                                {result.expectedXml || 'No expected XML available'}
                              </Code>
                            </Box>

                            {/* Actual XML */}
                            <Box>
                              <Text fontWeight="bold" mb={2} color="green.600">
                                Actual XML (Device Response)
                              </Text>
                              <Code
                                display="block"
                                whiteSpace="pre-wrap"
                                fontSize="xs"
                                p={3}
                                bg="green.50"
                                borderRadius="md"
                                maxH="400px"
                                overflow="auto"
                                fontFamily="Consolas, Monaco, monospace"
                                border="2px solid"
                                borderColor="green.200"
                              >
                                {result.actualXml || 'No actual XML received'}
                              </Code>
                            </Box>
                          </Grid>
                        </AccordionPanel>
                      </AccordionItem>

                      {/* Request XML */}
                      {result.requestXml && (
                        <AccordionItem border="none">
                          <AccordionButton
                            bg="white"
                            _hover={{ bg: 'gray.100' }}
                            borderRadius="md"
                            mb={2}
                          >
                            <Box flex="1" textAlign="left">
                              <Text fontWeight="semibold">📤 Request Sent to Device</Text>
                            </Box>
                            <AccordionIcon />
                          </AccordionButton>
                          <AccordionPanel pb={4}>
                            <Code
                              display="block"
                              whiteSpace="pre-wrap"
                              fontSize="xs"
                              p={3}
                              bg="gray.50"
                              borderRadius="md"
                              maxH="300px"
                              overflow="auto"
                              fontFamily="Consolas, Monaco, monospace"
                            >
                              {result.requestXml}
                            </Code>
                          </AccordionPanel>
                        </AccordionItem>
                      )}

                      {/* Differences Report */}
                      {!result.valid && differences.length > 0 && (
                        <AccordionItem border="none">
                          <AccordionButton
                            bg="red.100"
                            _hover={{ bg: 'red.200' }}
                            borderRadius="md"
                          >
                            <Box flex="1" textAlign="left">
                              <HStack>
                                <Text fontWeight="semibold" color="red.700">
                                  ⚠️ Differences & Missing Fields Report
                                </Text>
                                <Badge colorScheme="red">{differences.length} issues</Badge>
                              </HStack>
                            </Box>
                            <AccordionIcon />
                          </AccordionButton>
                          <AccordionPanel pb={4}>
                            <VStack align="stretch" spacing={2}>
                              {differences.map((diff, idx) => (
                                <Alert
                                  key={idx}
                                  status={diff.severity === 'error' ? 'error' : 'warning'}
                                  borderRadius="md"
                                  size="sm"
                                >
                                  <AlertIcon />
                                  <Box flex="1">
                                    <HStack>
                                      <Badge
                                        colorScheme={diff.type === 'missing' ? 'red' : diff.type === 'extra' ? 'orange' : 'yellow'}
                                      >
                                        {diff.type.toUpperCase()}
                                      </Badge>
                                      <Text fontSize="sm" fontFamily="mono">
                                        {diff.field}
                                      </Text>
                                    </HStack>
                                  </Box>
                                </Alert>
                              ))}

                              {result.differences && (
                                <Box mt={2}>
                                  <Text fontWeight="bold" fontSize="sm" mb={2}>
                                    Full Differences Output:
                                  </Text>
                                  <Code
                                    display="block"
                                    whiteSpace="pre-wrap"
                                    fontSize="xs"
                                    p={3}
                                    bg="red.50"
                                    borderRadius="md"
                                    fontFamily="Consolas, Monaco, monospace"
                                  >
                                    {result.differences}
                                  </Code>
                                </Box>
                              )}
                            </VStack>
                          </AccordionPanel>
                        </AccordionItem>
                      )}
                    </Accordion>
                  </CardBody>
                </Card>
              );
            })}
          </VStack>
        )}
      </VStack>
    </Box>
  );
}
