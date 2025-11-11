import React, { useState, useEffect } from "react";
import {
  Flex,
  Box,
  Text,
  SimpleGrid,
  Button,
  VStack,
  Divider,
} from "@chakra-ui/react";
import Client from "../../modules/client.js";

function DerForm({
  toggleVar,
  currentTest,
  setCurrentTest,
  setToggle,
  setReport,
  setTestState,
  setHeaderState,
  tests,
}) {
  const capabilityObject = tests[20].object;
  const settingsObject = tests[22].object;

  // Merge objects but avoid duplicating endDeviceId & derSettingsLink
  const mergedObject = {
    // First: Capability fields (endDeviceId, derSettingsLink come from here)
    ...capabilityObject,
    // Then: Settings fields, but skip duplicates
    ...Object.fromEntries(
      Object.entries(settingsObject).filter(
        ([key]) => !["endDeviceId", "derSettingsLink"].includes(key)
      )
    ),
  };

  const [formObject, setFormObject] = useState(mergedObject);

  useEffect(() => {
    setFormObject(mergedObject);
  }, [tests]);

  const handleChange = (key, value) =>
    setFormObject((prev) => ({ ...prev, [key]: value }));

  const handleSubmit = async (e) => {
    e.preventDefault();

    // VALIDATION: Check all fields are filled
    for (const [key, value] of Object.entries(formObject)) {
      if (!value || value.toString().trim() === "") {
        setHeaderState({
          text: `Please fill in: ${key}`,
          visElemIdx: 1,
        });
        return;
      }
    }

    setTestState(false);
    setHeaderState({ text: "Running Test", visElemIdx: 2 });
    setToggle(true);
    setCurrentTest(tests[0]);
    try {
      const res = await Client.sendTest({ ...currentTest, object: formObject });
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
        Capability + Settings TEST PARAMETERS
      </Text>

      <form onSubmit={handleSubmit}>
        <VStack spacing={4} align="stretch">
          {/* Der Capability */}
          <Box>
            <Text fontWeight="bold" mb={2}>Der Capability</Text>
            <SimpleGrid columns={2} spacing={3}>
              {Object.keys(capabilityObject).map((key) => (
                <Flex
                  key={key}
                  border="groove"
                  borderWidth="1vh"
                  flexDirection="column"
                  alignItems="center"
                  p={2}
                >
                  <label>{key}:</label>
                  <input
                    type="text"
                    value={formObject[key] || ""}
                    onChange={(e) => handleChange(key, e.target.value)}
                    style={{ width: "100%", textAlign: "center" }}
                  />
                </Flex>
              ))}
            </SimpleGrid>
          </Box>

          <Divider />

          {/* Der Settings (excluding duplicates) */}
          <Box>
            <Text fontWeight="bold" mb={2}>Der Settings</Text>
            <SimpleGrid columns={2} spacing={3}>
              {Object.entries(settingsObject)
                .filter(([key]) => !["endDeviceId", "derSettingsLink"].includes(key))
                .map(([key]) => (
                  <Flex
                    key={key}
                    border="groove"
                    borderWidth="1vh"
                    flexDirection="column"
                    alignItems="center"
                    p={2}
                  >
                    <label>{key}:</label>
                    <input
                      type="text"
                      value={formObject[key] || ""}
                      onChange={(e) => handleChange(key, e.target.value)}
                      style={{ width: "100%", textAlign: "center" }}
                    />
                  </Flex>
                ))}
            </SimpleGrid>
          </Box>

          {/* Buttons */}
          <SimpleGrid columns={2} spacing={3} mt={4}>
            <Button variant="submit_form_button" type="submit">
              SUBMIT
            </Button>
            <Button variant="cancel_form_button" onClick={handleCancel}>
              CANCEL
            </Button>
          </SimpleGrid>
        </VStack>
      </form>
    </Flex>
  );
}

export { DerForm };