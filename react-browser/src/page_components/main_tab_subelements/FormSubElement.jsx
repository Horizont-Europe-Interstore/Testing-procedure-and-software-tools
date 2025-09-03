import React, { useState, useEffect } from "react";
import {
  Text,
  Button,
  Box,
  Flex,
  SimpleGrid,
  Select,
} from "@chakra-ui/react";
import Client from "../../modules/client.js";

function FormSubElement({
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
  const [selectedOptionalField, setSelectedOptionalField] = useState(""); // Store selected dropdown option
  const [inputValue, setInputValue] = useState(""); // Store input value for the selected option

  useEffect(() => {
    // Reset selected optional field and input value whenever the test changes
    setSelectedOptionalField("");
    setInputValue("");
  }, [currentTest]);

  // Define mandatory fields based on the test
  const mandatoryFields =
    currentTest.test === "Create Der Program"
      ? ["fsaID", "mRID", "subscribable", "version", "description", "primacy"]
      : ["endDeviceId", "mRID", "subscribable", "version", "description"];

  const handleSubmitForm = async (event) => {
    event.preventDefault();

    console.log("Form Submission Initiated for Test:", currentTest.test);
    console.log("Current Test Object before Validation:", currentTest.object);

    // Validate input for the current test
    const validation = Client.getValid(currentTest.test);

    if (currentTest.test === "Create Function Set Assignments" || currentTest.test === "Create Der Program") {
      // Ensure all mandatory fields are filled
      for (let field of mandatoryFields) {
        const value = currentTest.object[field];
        if (value === "" || (validation && validation[field] && !validation[field](value))) {
          setHeaderState({ text: `Invalid Field ${field}`, visElemIdx: 1 });
          return;
        }
      }

      // Validate the optional field selected via the dropdown
      if (selectedOptionalField && inputValue === "") {
        setHeaderState({
          text: `Please enter a value for ${selectedOptionalField}`,
          visElemIdx: 1,
        });
        return;
      }

      // If dropdown is used, add selectedOptionalField and its value to currentTest.object
      if (selectedOptionalField) {
        setCurrentTest((currentTest) => ({
          ...currentTest,
          object: { ...currentTest.object, [selectedOptionalField]: inputValue },
        }));
      }
    } else {
      // Handle validation for other tests
      for (let [key, value] of Object.entries(currentTest.object)) {
        if (value === "" || (validation && validation[key] && !validation[key](value))) {
          setHeaderState({ text: `Invalid Field ${key}`, visElemIdx: 1 });
          return;
        }
      }
    }

    // Ensure the optional field value is added to the currentTest object before submission
    if (selectedOptionalField && inputValue) {
      currentTest.object[selectedOptionalField] = inputValue;
    }

    console.log("Validation Passed. Submitting the form...");

    setTestState(false);
    setHeaderState({ text: "Running Test", visElemIdx: 2 });
    setToggle(true);

    console.log("Final Test Object being sent to Client.sendTest:", currentTest);

    try {
      const res = await Client.sendTest(currentTest);
      console.log("Response received from Client.sendTest:", res);
      setReport(res);
    } catch (err) {
      console.error("Error occurred during form submission:", err);
    }

    setHeaderState({ text: "Ready", visElemIdx: 0 });
    setTestState(true);
  };

  // Generate the list of optional fields (not mandatory) for the "Create Function Set Assignments" test or "Create Der Program"
  const optionalFields =
    currentTest.test === "Create Function Set Assignments" || currentTest.test === "Create Der Program"
      ? Object.keys(currentTest.object).filter((field) => !mandatoryFields.includes(field))
      : [];

  return (
    <Flex flexDirection={"column"} maxHeight="100%" hidden={toggleVar}>
      <Text variant={"element_name"}>{currentTest.test} TEST PARAMETERS</Text>
      <Box>
        <form onSubmit={handleSubmitForm}>
          <SimpleGrid columns={2}>
            {/* Render the form dynamically based on the currentTest.object */}
            {currentTest.test === "Create Function Set Assignments" || currentTest.test === "Create Der Program" ? (
              <>
                {/* Render all mandatory fields */}
                {mandatoryFields.map((field) => (
                  <Flex
                    key={field}
                    border={"groove"}
                    borderWidth={"1vh"}
                    flexDirection={"column"}
                    alignItems={"center"}
                  >
                    <label key={field + "l"}>{field}:</label>
                    <input
                      key={field + "i"}
                      id={field}
                      type="text"
                      value={currentTest.object[field]}
                      onChange={(event) => {
                        const updatedValue = event.target.value;
                        setCurrentTest((currentTest) => ({
                          ...currentTest,
                          object: {
                            ...currentTest.object,
                            [event.target.id]: updatedValue,
                          },
                        }));
                        console.log(`Updated ${field} to ${updatedValue}`);
                      }}
                    />
                  </Flex>
                ))}

                {/* Dropdown for selecting optional fields */}
                <Flex
                  key={"optionalField"}
                  border={"groove"}
                  borderWidth={"1vh"}
                  flexDirection={"column"}
                  alignItems={"center"}
                >
                  <label key={"optionalFieldLabel"}>Select Optional Field:</label>
                  <Select
                    key={"optionalFieldSelect"}
                    placeholder="Select Optional Field"
                    onChange={(event) => {
                      setSelectedOptionalField(event.target.value);
                      setInputValue(""); // Reset the input value when a new field is selected
                    }}
                  >
                    {optionalFields.map((field) => (
                      <option key={field} value={field}>
                        {field}
                      </option>
                    ))}
                  </Select>
                </Flex>

                {/* Render input for the selected optional field */}
                {selectedOptionalField && (
                  <Flex
                    key={"optionalFieldValue"}
                    border={"groove"}
                    borderWidth={"1vh"}
                    flexDirection={"column"}
                    alignItems={"center"}
                  >
                    <label key={"optionalFieldValueLabel"}>
                      Enter value for {selectedOptionalField}:
                    </label>
                    <input
                      key={"optionalFieldValueInput"}
                      id={selectedOptionalField}
                      type="text"
                      value={inputValue}
                      onChange={(event) => setInputValue(event.target.value)}
                    />
                  </Flex>
                )}
              </>
            ) : (
              // For other tests, render fields dynamically based on currentTest.object
              Object.entries(currentTest.object).map(([key, value]) => (
                <Flex
                  key={key}
                  border={"groove"}
                  borderWidth={"1vh"}
                  flexDirection={"column"}
                  alignItems={"center"}
                >
                  <label key={key + "l"}>{key}:</label>
                  <input
                    key={key + "i"}
                    id={key}
                    type="text"
                    value={value}
                    onChange={(event) => {
                      const updatedValue = event.target.value;
                      setCurrentTest((currentTest) => ({
                        ...currentTest,
                        object: { ...currentTest.object, [event.target.id]: updatedValue },
                      }));
                      console.log(`Updated ${key} to ${updatedValue}`);
                    }}
                  />
                </Flex>
              ))
            )}
          </SimpleGrid>
          <SimpleGrid columns={2}>
            <Button variant={"submit_form_button"} type="submit" value="Submit">
              SUBMIT
            </Button>
            <Button
              variant={"cancel_form_button"}
              onClick={() => {
                console.log("Form submission canceled. Resetting to initial state.");
                setTestState(true);
                setToggle(true);
                setCurrentTest(tests[0]);
              }}
            >
              CANCEL
            </Button>
          </SimpleGrid>
        </form>
      </Box>
    </Flex>
  );
}

export { FormSubElement };




