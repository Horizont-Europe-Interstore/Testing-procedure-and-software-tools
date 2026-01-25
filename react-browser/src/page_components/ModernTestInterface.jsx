import React, { useState } from 'react';
import {
  Box,
  Grid,
  Card,
  CardBody,
  CardHeader,
  Heading,
  Text,
  Button,
  VStack,
  HStack,
  Badge,
  Flex,
  useColorModeValue,
  Divider
} from '@chakra-ui/react';
import Client from '../modules/client.js';
import { TestForm } from './TestForm.jsx';

export function ModernTestInterface({ colors }) {
  const [selectedTest, setSelectedTest] = useState(null);
  const [activeSection, setActiveSection] = useState(null);
  
  const tests = Client.getTests();
  
  const sections = [
    {
      id: 'part1',
      title: 'Device Capability & End Device',
      subtitle: 'Basic device configuration and registration',
      color: 'blue',
      tests: tests.filter(t => !t.hidden && t.index >= 0 && t.index <= 7)
    },
    {
      id: 'part2',
      title: 'Function Sets and DER',
      subtitle: 'Function set assignments and DER management',
      color: 'green',
      tests: tests.filter(t => !t.hidden && t.index >= 8 && t.index <= 13)
    },
    {
      id: 'part3',
      title: 'DER Programs',
      subtitle: 'DER program configuration and controls',
      color: 'orange',
      tests: tests.filter(t => !t.hidden && t.index >= 14 && t.index <= 21)
    },
    {
      id: 'part4',
      title: 'DER Capability and Settings',
      subtitle: 'Advanced DER capabilities and settings',
      color: 'purple',
      tests: tests.filter(t => !t.hidden && t.index >= 22)
    }
  ];

  const handleTestSelect = (test) => {
    setSelectedTest(test);
  };

  const handleSectionClick = (sectionId) => {
    setActiveSection(activeSection === sectionId ? null : sectionId);
    setSelectedTest(null);
  };

  return (
    <Box p={6} bg="white" minH="100vh">
      <VStack spacing={6} align="stretch">
        {/* Header */}
        <Box textAlign="center" py={4}>
          <Heading size="xl" color="gray.700" mb={2}>
            IEEE 2030.5 Test Configuration
          </Heading>
          <Text color="gray.600" fontSize="lg">
            Configure and store test parameters for IEEE 2030.5 conformance testing
          </Text>
        </Box>

        {/* Main Content */}
        <Grid templateColumns="1fr 2fr" gap={6} minH="70vh">
          {/* Left Panel - Test Categories */}
          <VStack spacing={4} align="stretch">
            {sections.map((section) => (
              <Card
                key={section.id}
                cursor="pointer"
                onClick={() => handleSectionClick(section.id)}
                bg={activeSection === section.id ? `${section.color}.50` : 'white'}
                borderColor={activeSection === section.id ? `${section.color}.200` : 'gray.200'}
                borderWidth="2px"
                _hover={{
                  borderColor: `${section.color}.300`,
                  transform: 'translateY(-2px)',
                  shadow: 'md'
                }}
                transition="all 0.2s"
              >
                <CardHeader pb={2}>
                  <HStack>
                    <VStack align="start" spacing={0}>
                      <Heading size="md" color="gray.700">
                        {section.title}
                      </Heading>
                      <Text fontSize="sm" color="gray.500">
                        {section.subtitle}
                      </Text>
                    </VStack>
                    <Badge
                      colorScheme={section.color}
                      variant="subtle"
                      ml="auto"
                    >
                      {section.tests.length} tests
                    </Badge>
                  </HStack>
                </CardHeader>

                {/* Expanded Test List */}
                {activeSection === section.id && (
                  <CardBody pt={0}>
                    <Divider mb={3} />
                    <VStack spacing={2} align="stretch">
                      {section.tests.map((test) => (
                        <Button
                          key={test.index}
                          variant={selectedTest?.index === test.index ? 'solid' : 'ghost'}
                          colorScheme={section.color}
                          size="sm"
                          justifyContent="flex-start"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleTestSelect(test);
                          }}
                          _hover={{
                            bg: `${section.color}.100`
                          }}
                        >
                          {test.test}
                        </Button>
                      ))}
                    </VStack>
                  </CardBody>
                )}
              </Card>
            ))}
          </VStack>

          {/* Right Panel - Test Form */}
          <Card bg="white" borderColor="gray.200" borderWidth="2px">
            <CardBody>
              {selectedTest ? (
                <TestForm
                  test={selectedTest}
                  onTestComplete={() => setSelectedTest(null)}
                />
              ) : (
                <Flex
                  direction="column"
                  align="center"
                  justify="center"
                  h="full"
                  color="gray.500"
                >
                  <Heading size="lg" mb={2}>
                    Select a Test
                  </Heading>
                  <Text textAlign="center" maxW="md">
                    Choose a test category from the left panel, then select a specific test to configure its parameters.
                  </Text>
                </Flex>
              )}
            </CardBody>
          </Card>
        </Grid>
      </VStack>
    </Box>
  );
}