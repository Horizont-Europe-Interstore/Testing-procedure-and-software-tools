import React, { useState, useEffect } from 'react';
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
  Code,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
  IconButton,
  Tooltip,
  Select,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper
} from '@chakra-ui/react';
import Client from '../modules/client.js';

export function EnhancedTestForm({ test, onTestComplete }) {
  const [formData, setFormData] = useState({ ...test.object });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const [hasCachedData, setHasCachedData] = useState(false);


  const [selectedOptionalField, setSelectedOptionalField] = useState('');
  const [showOptionalFields, setShowOptionalFields] = useState(false);

 
  const [xyPairCount, setXyPairCount] = useState(1);

  const toast = useToast();

  
  const getCacheKey = () => `test_form_cache_${test.test.replace(/\s+/g, '_')}`;

  
  useEffect(() => {
    const cacheKey = getCacheKey();
    const cachedData = localStorage.getItem(cacheKey);

    if (cachedData) {
      try {
        const parsedData = JSON.parse(cachedData);
        setFormData(parsedData);
        setHasCachedData(true);
      } catch (error) {
        console.error('Failed to load cached data:', error);
      }
    }
  }, [test.test]);


  useEffect(() => {
    const cacheKey = getCacheKey();
    const hasValues = Object.values(formData).some(value =>
      value && value.toString().trim() !== ''
    );

    if (hasValues) {
      localStorage.setItem(cacheKey, JSON.stringify(formData));
      setHasCachedData(true);
    }
  }, [formData]);

  const handleInputChange = (key, value) => {
    setFormData(prev => ({
      ...prev,
      [key]: value
    }));
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    try {
     
      let submissionData = { ...formData };

      if (test.test === 'Create Der Curve') {
       
        const cleanedData = {};
        for (const [key, value] of Object.entries(submissionData)) {
          if (key.startsWith('x_value_') || key.startsWith('y_value_')) {
            const pairNum = parseInt(key.match(/\d+/)[0]);
            if (pairNum <= xyPairCount) {
              cleanedData[key] = value;
            }
          } else {
            cleanedData[key] = value;
          }
        }
        submissionData = cleanedData;
      }

      const testObject = {
        ...test,
        object: submissionData
      };

      const response = await Client.sendTest(testObject);
      setResult(response);

      const isGetOperation = response['End result'] === 'retrieved';

      toast({
        title: isGetOperation ? 'Data Retrieved' : 'Configuration Saved',
        description: isGetOperation
          ? 'Data has been fetched from the server successfully'
          : 'Test parameters have been stored successfully',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });

      if (onTestComplete && !isGetOperation) {
        setTimeout(() => {
          onTestComplete();
        }, 1500);
      }
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to complete operation',
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
    setSelectedOptionalField('');
    setXyPairCount(1);
  };

  const handleClearCache = () => {
    const cacheKey = getCacheKey();
    localStorage.removeItem(cacheKey);
    setFormData({ ...test.object });
    setHasCachedData(false);
    setResult(null);

    toast({
      title: 'Cache Cleared',
      description: 'Saved form data has been removed',
      status: 'info',
      duration: 2000,
      isClosable: true,
    });
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

  const isGetTest = test.test.startsWith('Get All') || test.test.startsWith('Get A') || test.test.startsWith('Get');


  const formatFieldLabel = (key) => {
    return key
      .replace(/([a-z])([A-Z])/g, '$1 $2')  
      .replace(/([A-Z])([A-Z][a-z])/g, '$1 $2')  
      .replace(/^./, str => str.toUpperCase());  
  };

  
  const fsaMandatoryFields = ['endDeviceId', 'mRID', 'description', 'subscribable', 'version'];


  const derCurveMandatoryFields = ['derProgramId', 'mRID', 'description', 'version', 'curveType',
                                    'x_multiplier_type', 'y_multiplier_type', 'y_ref_type'];

  
  const derControlMandatoryFields = ['derProgramId', 'deviceCategory', 'mRID', 'description', 'version',
                                      'duration', 'start', 'currentStatus', 'dateTime',
                                      'potentiallySuperseded', 'randomizeDuration', 'randomizeStart'];

  // Operation modes that require DER Curve links (href)
  const curveModes = [
    'opModFreqWatt', 'opModHFRTMayTrip', 'opModHFRTMustTrip', 'opModHVRTMayTrip',
    'opModHVRTMomentaryCessation', 'opModHVRTMustTrip', 'opModLFRTMayTrip', 'opModLFRTMustTrip',
    'opModLVRTMayTrip', 'opModLVRTMomentaryCessation', 'opModLVRTMustTrip', 'opModVoltVar',
    'opModVoltWatt', 'opModWattPF', 'opModWattVar'
  ];

  // Boolean operation modes
  const booleanModes = ['opModChargeMode', 'opModDischargeMode', 'opModConnect', 'opModEnergize'];

  // Integer operation modes
  const integerModes = [
    'opModFixedPFAbsorbW', 'opModFixedPFInjectW', 'opModFixedVar', 'opModFixedW',
    'opModFreqDroop', 'IntegeropModMaxLimW', 'opModTargetVar', 'opModTargetW', 'rampTms'
  ];

  const getDisplayFields = () => {
    if (test.test === 'Create Function Set Assignments') {
     
      const fields = Object.keys(test.object).filter(key =>
        fsaMandatoryFields.includes(key) || key === selectedOptionalField
      );
      return fields;
    } else if (test.test === 'Create Der Curve') {
      const fields = Object.keys(test.object).filter(key => {
        if (derCurveMandatoryFields.includes(key)) return true;
        if (key.startsWith('x_value_') || key.startsWith('y_value_')) {
          const pairNum = parseInt(key.match(/\d+/)[0]);
          return pairNum <= xyPairCount;
        }
        return false;
      });
      return fields;
    } else if (test.test === 'Create Der Control') {
      
      const fields = Object.keys(test.object).filter(key =>
        derControlMandatoryFields.includes(key) || key === selectedOptionalField
      );
      return fields;
    }
    return Object.keys(test.object);
  };

  
  const getOptionalFields = () => {
    if (test.test === 'Create Function Set Assignments') {
      return Object.keys(test.object).filter(key => !fsaMandatoryFields.includes(key));
    } else if (test.test === 'Create Der Control') {
      return Object.keys(test.object).filter(key => !derControlMandatoryFields.includes(key));
    }
    return [];
  };

  const displayFields = getDisplayFields();

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
              {hasCachedData && (
                <Tooltip label="Form data restored from last session" placement="right">
                  <Badge colorScheme="cyan" variant="solid" fontSize="xs">
                    💾 Cached
                  </Badge>
                </Tooltip>
              )}
            </HStack>
            <Text color="gray.600" fontSize="md">
              {test.desc}
            </Text>
          </VStack>

          <HStack>
            {hasCachedData && (
              <Tooltip label="Clear saved form data" placement="bottom">
                <IconButton
                  icon={<span>🗑️</span>}
                  variant="ghost"
                  size="sm"
                  onClick={handleClearCache}
                  isDisabled={isSubmitting}
                  aria-label="Clear cache"
                  colorScheme="red"
                />
              </Tooltip>
            )}
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
              loadingText={isGetTest ? "Fetching..." : "Saving..."}
            >
              {isGetTest ? '📥 Fetch Data' : '💾 Save Configuration'}
            </Button>
          </HStack>
        </HStack>
        <Divider />
      </Box>

      {/* Special Controls */}
      {(test.test === 'Create Function Set Assignments' || test.test === 'Create Der Control') && (
        <Box
          bg={test.test === 'Create Function Set Assignments' ? 'blue.50' : 'green.50'}
          p={4}
          borderRadius="md"
          border="1px solid"
          borderColor={test.test === 'Create Function Set Assignments' ? 'blue.200' : 'green.200'}
        >
          <VStack align="stretch" spacing={3}>
            <Text
              fontWeight="bold"
              fontSize="sm"
              color={test.test === 'Create Function Set Assignments' ? 'blue.800' : 'green.800'}
            >
              {test.test === 'Create Der Control'
                ? 'Optional Operation Mode Fields (Select one to add)'
                : 'Optional Fields (Select one to add)'}
            </Text>
            <Select
              placeholder={
                test.test === 'Create Der Control'
                  ? 'Select an operation mode field to add'
                  : 'Select an optional field to add'
              }
              value={selectedOptionalField}
              onChange={(e) => setSelectedOptionalField(e.target.value)}
              bg="white"
              size="sm"
            >
              {getOptionalFields().map(field => (
                <option key={field} value={field}>
                  {formatFieldLabel(field)}
                </option>
              ))}
            </Select>
            {test.test === 'Create Der Control' && (
              <Text fontSize="xs" color="green.700">
                Operation mode fields (opMod*) control DER behavior and power flow
              </Text>
            )}
          </VStack>
        </Box>
      )}

      {test.test === 'Create Der Curve' && (
        <Box bg="orange.50" p={4} borderRadius="md" border="1px solid" borderColor="orange.200">
          <VStack align="stretch" spacing={3}>
            <Text fontWeight="bold" fontSize="sm" color="orange.800">
              Number of X/Y Coordinate Pairs
            </Text>
            <NumberInput
              value={xyPairCount}
              onChange={(_, num) => setXyPairCount(num)}
              min={1}
              max={10}
              size="sm"
              bg="white"
            >
              <NumberInputField />
              <NumberInputStepper>
                <NumberIncrementStepper />
                <NumberDecrementStepper />
              </NumberInputStepper>
            </NumberInput>
            <Text fontSize="xs" color="orange.700">
              Will show {xyPairCount} pair{xyPairCount !== 1 ? 's' : ''} of X/Y coordinates
            </Text>
          </VStack>
        </Box>
      )}

      {/* Form Fields */}
      <Box flex="1" overflowY="auto">
        {displayFields.length > 0 ? (
          <Grid templateColumns="repeat(auto-fit, minmax(300px, 1fr))" gap={4}>
            {displayFields.map((key) => {
              // Render checkbox for boolean modes in DER Control
              if (test.test === 'Create Der Control' && booleanModes.includes(key)) {
                return (
                  <FormControl key={key} display="flex" alignItems="center">
                    <input
                      type="checkbox"
                      checked={formData[key] || false}
                      onChange={(e) => handleInputChange(key, e.target.checked)}
                      style={{ marginRight: '8px' }}
                    />
                    <FormLabel fontSize="sm" fontWeight="medium" color="gray.700" mb={0}>
                      {formatFieldLabel(key)}
                    </FormLabel>
                  </FormControl>
                );
              }
              
              // Render text input with placeholder for curve modes
              if (test.test === 'Create Der Control' && curveModes.includes(key)) {
                return (
                  <FormControl key={key}>
                    <FormLabel fontSize="sm" fontWeight="medium" color="gray.700">
                      {formatFieldLabel(key)} (Curve Link)
                    </FormLabel>
                    <Input
                      value={formData[key] || ''}
                      onChange={(e) => handleInputChange(key, e.target.value)}
                      placeholder="/derp/{derpId}/dc/{curveId}"
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
                );
              }
              
              // Render number input for integer modes
              if (test.test === 'Create Der Control' && integerModes.includes(key)) {
                return (
                  <FormControl key={key}>
                    <FormLabel fontSize="sm" fontWeight="medium" color="gray.700">
                      {formatFieldLabel(key)}
                    </FormLabel>
                    <Input
                      type="number"
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
                );
              }
              
              // Default text input for all other fields
              return (
                <FormControl key={key}>
                  <FormLabel fontSize="sm" fontWeight="medium" color="gray.700">
                    {formatFieldLabel(key)}
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
              );
            })}
          </Grid>
        ) : (
          <Alert status="info" borderRadius="md">
            <AlertIcon />
            <Box>
              <AlertTitle>No Parameters Required</AlertTitle>
              <AlertDescription>
                This test doesn't require any input parameters. Click "{isGetTest ? 'Fetch Data' : 'Save Configuration'}" to execute.
              </AlertDescription>
            </Box>
          </Alert>
        )}
      </Box>

      {/* Result Display - Same as TestForm.jsx */}
      {result && (
        <Box>
          <Divider mb={4} />
          {result['End result'] === 'retrieved' ? (
            <Alert status="info" borderRadius="md" flexDirection="column" alignItems="flex-start" p={4}>
              <HStack mb={2}>
                <AlertIcon />
                <AlertTitle>Data Retrieved from Server</AlertTitle>
              </HStack>
              {result.Steps && result.Steps[0] && (
                <AlertDescription width="100%">
                  <VStack align="start" spacing={3} width="100%">
                    <Text fontWeight="medium">{result.Steps[0].Name}</Text>
                    {result.Steps[0].FetchedData && (
                      <Accordion allowToggle width="100%">
                        <AccordionItem border="none">
                          <AccordionButton bg="blue.50" _hover={{ bg: 'blue.100' }} borderRadius="md">
                            <Box flex="1" textAlign="left">
                              <Text fontWeight="semibold">View Fetched Data (XML/JSON)</Text>
                            </Box>
                            <AccordionIcon />
                          </AccordionButton>
                          <AccordionPanel pb={4}>
                            <Code display="block" whiteSpace="pre-wrap" fontSize="xs" p={3} bg="white"
                                  borderRadius="md" maxH="400px" overflow="auto" fontFamily="Consolas, Monaco, monospace"
                                  border="1px solid" borderColor="gray.300">
                              {result.Steps[0].FetchedData}
                            </Code>
                          </AccordionPanel>
                        </AccordionItem>
                      </Accordion>
                    )}
                  </VStack>
                </AlertDescription>
              )}
            </Alert>
          ) : (
            <Alert status={result['End result'] === 'stored' ? 'success' : 'error'} borderRadius="md"
                   flexDirection="column" alignItems="flex-start" p={4}>
              <HStack mb={2}>
                <AlertIcon />
                <AlertTitle>
                  {result['End result'] === 'stored' ? 'Configuration Stored' : 'Operation Failed'}
                </AlertTitle>
              </HStack>
              {result.Steps && result.Steps[0] && (
                <AlertDescription width="100%">
                  <VStack align="start" spacing={2}>
                    <Text fontWeight="medium">{result.Steps[0].Name}</Text>
                    {result.Steps[0].Value && (
                      <Box width="100%">
                        <Text fontSize="sm" color="gray.600" mb={1}>Stored Values:</Text>
                        <Text fontSize="sm" fontFamily="mono" bg="gray.100" p={2} borderRadius="md">
                          {result.Steps[0].Value}
                        </Text>
                      </Box>
                    )}
                    {result.Steps[0].ServerResponse && (
                      <Box width="100%">
                        <Text fontSize="sm" color="gray.600" mb={1}>Server Response:</Text>
                        <Code display="block" whiteSpace="pre-wrap" fontSize="xs" p={2} bg="white"
                              borderRadius="md" maxH="200px" overflow="auto">
                          {result.Steps[0].ServerResponse}
                        </Code>
                      </Box>
                    )}
                  </VStack>
                </AlertDescription>
              )}
            </Alert>
          )}
        </Box>
      )}
    </VStack>
  );
}
