import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  Text,
  Flex,
  Badge,
  VStack,
  HStack,
  Card,
  CardHeader,
  CardBody,
  Code,
  Divider
} from '@chakra-ui/react';

const XmlTestResults = ({ colors, testResults, httpConnected }) => {
  const [selectedResult, setSelectedResult] = useState(null);
  
  // Show the latest test result by default
  const currentResult = selectedResult || (testResults && testResults[0]);
  
  const connectionStatus = httpConnected 
    ? '🟢 Connected - Polling for test results...' 
    : '🔴 Disconnected';

  return (
    <Box h="100%" w="100%" p={4} bg={colors.background}>
      <VStack spacing={4} align="stretch" h="100%">
        {/* Header */}
        <Flex justify="space-between" align="center">
          <Heading size="lg" color={colors.primary}>
            XML Test Results Dashboard
          </Heading>
          <HStack>
            <Text fontSize="sm">Status:</Text>
            <Badge 
              colorScheme={connectionStatus.includes('🟢') ? 'green' : 'red'}
              variant="solid"
            >
              {connectionStatus}
            </Badge>
          </HStack>
        </Flex>

        <Divider />

        {/* Test Results List */}
        {testResults && testResults.length > 0 && (
          <Box>
            <Heading size="sm" mb={2}>Recent Test Results ({testResults.length})</Heading>
            <HStack spacing={2} overflowX="auto" pb={2}>
              {testResults.slice(0, 5).map((result, index) => (
                <Badge
                  key={result.testId || index}
                  colorScheme={result.status === 'PASSED' ? 'green' : 'red'}
                  variant={currentResult === result ? 'solid' : 'outline'}
                  cursor="pointer"
                  onClick={() => setSelectedResult(result)}
                  p={2}
                  borderRadius="md"
                >
                  {result.testName} - {result.status}
                </Badge>
              ))}
            </HStack>
          </Box>
        )}

        {/* Test Results Content */}
        {currentResult ? (
          <VStack spacing={4} align="stretch" flex="1">
            {/* Test Info Card */}
            <Card>
              <CardHeader>
                <HStack justify="space-between">
                  <Heading size="md">{currentResult.testName}</Heading>
                  <Badge 
                    colorScheme={currentResult.status === 'PASSED' ? 'green' : 'red'}
                    variant="solid"
                    fontSize="sm"
                    p={2}
                  >
                    {currentResult.status}
                  </Badge>
                </HStack>
              </CardHeader>
              <CardBody>
                <VStack align="stretch" spacing={2}>
                  <Text><strong>Reason:</strong> {currentResult.reason}</Text>
                  <Text><strong>Test ID:</strong> {currentResult.testId}</Text>
                  <Text><strong>Timestamp:</strong> {new Date(currentResult.timestamp).toLocaleString()}</Text>
                </VStack>
              </CardBody>
            </Card>

            {/* XML Comparison */}
            <Flex gap={4} flex="1" h="400px">
              {/* Expected XML */}
              <Box flex="1">
                <Card h="100%">
                  <CardHeader>
                    <Heading size="sm" color={colors.secondary}>Expected XML</Heading>
                  </CardHeader>
                  <CardBody overflow="hidden">
                    <Code 
                      display="block"
                      whiteSpace="pre-wrap"
                      fontSize="xs"
                      p={3}
                      bg="gray.50"
                      borderRadius="md"
                      overflow="auto"
                      h="100%"
                      fontFamily="Consolas, Monaco, monospace"
                    >
                      {currentResult.expectedXml}
                    </Code>
                  </CardBody>
                </Card>
              </Box>

              {/* Actual XML */}
              <Box flex="1">
                <Card h="100%">
                  <CardHeader>
                    <Heading size="sm" color={colors.accent}>Actual XML (from NATS)</Heading>
                  </CardHeader>
                  <CardBody overflow="hidden">
                    <Code 
                      display="block"
                      whiteSpace="pre-wrap"
                      fontSize="xs"
                      p={3}
                      bg="gray.50"
                      borderRadius="md"
                      overflow="auto"
                      h="100%"
                      fontFamily="Consolas, Monaco, monospace"
                    >
                      {currentResult.actualXml}
                    </Code>
                  </CardBody>
                </Card>
              </Box>
            </Flex>
          </VStack>
        ) : (
          <Flex 
            justify="center" 
            align="center" 
            flex="1"
            direction="column"
            bg="gray.50"
            borderRadius="md"
            p={8}
          >
            <Heading size="md" color="gray.500" mb={4}>
              ⏳ Waiting for test results...
            </Heading>
            <Text color="gray.400" textAlign="center">
              Run your Karate XML validation tests to see results here
            </Text>
          </Flex>
        )}
      </VStack>
    </Box>
  );
};

export { XmlTestResults };