import React, { useState } from 'react';
import {
  Box,
  VStack,
  HStack,
  Heading,
  Text,
  Input,
  Button,
  FormControl,
  FormLabel,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  Badge,
  Divider,
  Grid,
  useToast,
  Spinner
} from '@chakra-ui/react';
import Client from '../modules/client.js';

export function TestForm({ test, onTestComplete }) {
  const [formData, setFormData] = useState({ ...test.object });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const toast = useToast();

  const handleInputChange = (key, value) => {
    setFormData(prev => ({
      ...prev,
      [key]: value
    }));
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    try {
      const testObject = {
        ...test,
        object: formData
      };
      
      const response = await Client.sendTest(testObject);
      setResult(response);
      
      toast({
        title: 'Configuration Saved',
        description: 'Test parameters have been stored successfully',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to save configuration',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleReset = () => {
    setFormData({ ...test.object });
    setResult(null);
  };

  const getSectionName = (index) => {
    if (index >= 0 && index <= 7) return 'Device Capability & End Device';
    if (index >= 8 && index <= 13) return 'Function Sets and DER';
    if (index >= 14 && index <= 21) return 'DER Programs';
    return 'DER Capability and Settings';
  };

  const getPartColor = (index) => {
    if (index >= 0 && index <= 7) return 'blue';
    if (index >= 8 && index <= 13) return 'green';
    if (index >= 14 && index <= 21) return 'orange';
    return 'purple';
  };

  const hasValues = Object.values(formData).some(value => value && value.toString().trim() !== '');

  return (
    <VStack spacing={6} align="stretch" h="full">
      {/* Header */}
      <Box>
        <HStack justify="space-between" align="start" mb={4}>
          <VStack align="start" spacing={1}>
            <HStack>
              <Heading size="lg" color="gray.700">
                {test.test}
              </Heading>
              <Badge colorScheme={getPartColor(test.index)} variant="subtle">
                {getSectionName(test.index)}
              </Badge>
            </HStack>
            <Text color="gray.600" fontSize="md">
              {test.desc}
            </Text>
          </VStack>
          
          <HStack>
            <Button
              variant="outline"
              size="sm"
              onClick={handleReset}
              isDisabled={isSubmitting}
            >
              🔄 Reset
            </Button>
            <Button
              colorScheme={getPartColor(test.index)}
              size="sm"
              onClick={handleSubmit}
              isLoading={isSubmitting}
              loadingText="Saving..."
            >
              💾 Save Configuration
            </Button>
          </HStack>
        </HStack>
        <Divider />
      </Box>

      {/* Form Fields */}
      <Box flex="1" overflowY="auto">
        {Object.keys(test.object).length > 0 ? (
          <Grid templateColumns="repeat(auto-fit, minmax(300px, 1fr))" gap={4}>
            {Object.entries(test.object).map(([key, defaultValue]) => (
              <FormControl key={key}>
                <FormLabel fontSize="sm" fontWeight="medium" color="gray.700">
                  {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
                </FormLabel>
                <Input
                  value={formData[key] || ''}
                  onChange={(e) => handleInputChange(key, e.target.value)}
                  placeholder={`Enter ${key.toLowerCase()}`}
                  size="sm"
                  bg="white"
                  borderColor="gray.300"
                  _hover={{ borderColor: 'gray.400' }}
                  _focus={{ 
                    borderColor: `${getPartColor(test.index)}.400`,
                    boxShadow: `0 0 0 1px var(--chakra-colors-${getPartColor(test.index)}-400)`
                  }}
                />
              </FormControl>
            ))}
          </Grid>
        ) : (
          <Alert status="info" borderRadius="md">
            <AlertIcon />
            <Box>
              <AlertTitle>No Parameters Required</AlertTitle>
              <AlertDescription>
                This test doesn't require any input parameters. Click "Save Configuration" to execute.
              </AlertDescription>
            </Box>
          </Alert>
        )}
      </Box>

      {/* Result Display */}
      {result && (
        <Box>
          <Divider mb={4} />
          <Alert
            status={result['End result'] === 'stored' ? 'success' : 'error'}
            borderRadius="md"
            flexDirection="column"
            alignItems="flex-start"
            p={4}
          >
            <HStack mb={2}>
              <AlertIcon />
              <AlertTitle>
                {result['End result'] === 'stored' ? 'Configuration Stored' : 'Storage Failed'}
              </AlertTitle>
            </HStack>
            
            {result.Steps && result.Steps[0] && (
              <AlertDescription>
                <VStack align="start" spacing={2}>
                  <Text fontWeight="medium">
                    {result.Steps[0].Name}
                  </Text>
                  {result.Steps[0].Value && (
                    <Box>
                      <Text fontSize="sm" color="gray.600" mb={1}>Stored Values:</Text>
                      <Text fontSize="sm" fontFamily="mono" bg="gray.100" p={2} borderRadius="md">
                        {result.Steps[0].Value}
                      </Text>
                    </Box>
                  )}
                </VStack>
              </AlertDescription>
            )}
          </Alert>
        </Box>
      )}

      {/* Test Details Section */}
      <Box>
        <Divider mb={4} />
        <Box bg="gray.50" p={4} borderRadius="md">
          <Heading size="sm" mb={3} color="gray.700">
            Test Details
          </Heading>
          <Text fontSize="sm" color="gray.600" lineHeight="1.6">
            {/* Placeholder for test details - will be replaced with your provided text */}
            Test details and information will be displayed here. Please provide the text content you want to add.
          </Text>
        </Box>
      </Box>
    </VStack>
  );
}