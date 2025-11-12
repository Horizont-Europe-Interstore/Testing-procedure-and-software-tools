import React, { useState, useEffect } from "react";
import {
  Text,
  Button,
  Box,
  Flex,
  SimpleGrid,
  Select,
  RadioGroup, 
  Stack,
  Radio
} from "@chakra-ui/react";
import Client from "../../modules/client.js";

function DerForm({
  toggleVar,
  currentTest,
  setToggle,
  setTestState,
  setCurrentTest,
  tests,
  setReport,
  setHeaderState,
  testState,
}) {
  // Radio mode: der | capability | settings
  const [formMode, setFormMode] = useState("der");

  // Shared data across all 3 modes
  const [formData, setFormData] = useState({});

  // Get the 3 test definitions
  const derTest = tests.find(t => t.test === "Create Der");
  const capabilityTest = tests.find(t => t.test === "Create Der Capability");
  const settingsTest = tests.find(t => t.test === "Create Der Settings");

  // Current active test based on radio
  const activeTest = formMode === "der" ? derTest :
                    formMode === "capability" ? capabilityTest :
                    settingsTest;

  // Initialize formData on first load
  useEffect(() => {
    if (activeTest) {
      setFormData({ ...activeTest.object });
    }
  }, [activeTest]);


  const handleChange = (key, value) => {
    setFormData(prev => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // REQUIRED FIELDS — only IDs
    const requiredFields = {
      "der": ["endDeviceID"],
      "capability": ["derId"],  
      "settings": ["derId"]
    };

    const required = requiredFields[formMode] || [];

    // Validate only required fields
    for (const key of required) {
      const value = formData[key];
      if (!value || value.toString().trim() === "") {
        setHeaderState({ text: `Please fill in: ${key}`, visElemIdx: 1 });
        return;
      }
    }

    const testToSend = {
      ...activeTest,
      object: formData
    };

    setTestState(false);
    setHeaderState({ text: "Running Test", visElemIdx: 2 });
    setToggle(true);

    try {
      const res = await Client.sendTest(testToSend);
      setReport(res);
    } catch (err) {
      console.error(err);
      setReport({ error: err.toString() });
    }

    setHeaderState({ text: "Ready", visElemIdx: 0 });
    setTestState(true);
  };

  const handleCancel = () => {
    setToggle(true);
    setTestState(true);
    setCurrentTest(tests[0]);
  };

  return (
    <Flex flexDirection="column" hidden={toggleVar}>
      <Text variant="element_name" mb={3}>
        {activeTest?.test || "DER"} TEST PARAMETERS
      </Text>

      {/* Radio Buttons */}
      <Box mb={4}>
        <Text fontWeight="bold" mb={1} color="blue.600">
          Select Form
        </Text>
        <RadioGroup onChange={setFormMode} value={formMode}>
          <Stack direction="row" spacing={6}>
            <Radio value="der">Create Der</Radio>
            <Radio value="capability">Create Der Capability</Radio>
            <Radio value="settings">Create Der Settings</Radio>
          </Stack>
        </RadioGroup>
      </Box>

      <Box>
        <form onSubmit={handleSubmit}>
          <SimpleGrid columns={2} spacing={3}>
            {Object.entries(formData).map(([key, value]) => {
              const isRequired = 
                (formMode === "der" && key === "endDeviceID") ||
                (formMode === "capability" && key === "derId") ||
                (formMode === "settings" && key === "derId");

              return (
                <Flex
                  key={key}
                  border="groove"
                  borderWidth="1vh"
                  flexDirection="column"
                  alignItems="center"
                  p={2}
                  position="relative"
                >
                  <label>
                    {key}
                    {isRequired && <Text as="span" color="red.500" ml={1}>*</Text>}
                    :
                  </label>
                  <input
                    type="text"
                    name={key}
                    autoComplete="on"
                    value={value || ""}
                    onChange={(e) => handleChange(key, e.target.value)}
                    style={{ width: "100%", textAlign: "center" }}
                  />
                </Flex>
              );
            })}
          </SimpleGrid>

          {/* Buttons - Always visible */}
          <SimpleGrid columns={2} spacing={3} mt={4}>
            <Button variant="submit_form_button" type="submit">
              SUBMIT
            </Button>
            <Button variant="cancel_form_button" onClick={handleCancel}>
              CANCEL
            </Button>
          </SimpleGrid>
        </form>
      </Box>
    </Flex>
  );
}

export { DerForm };