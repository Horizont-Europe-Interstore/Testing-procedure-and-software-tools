import React, { useState } from "react";
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
  const [selectedFunctionSet, setSelectedFunctionSet] = useState("");
  const [inputValue, setInputValue] = useState("");

  const handleSubmitForm = async (event) => {
    event.preventDefault();

    console.log("Form Submission Initiated for Test:", currentTest.test);
    console.log("Current Test Object before Validation:", currentTest.object);

    // Validate input for the current test
    const validation = Client.getValid(currentTest.test);
    for (let [key, value] of Object.entries(currentTest.object)) {
      key = key.split(' ').join('');
      key = key[0].toLowerCase() + key.substring(1);
      if (value === "" || (validation[key] !== undefined && !validation[key](value))) {
        console.log(`Validation failed for field: ${key}, Value: ${value}`);
        setHeaderState({ text: `Invalid Field ${key}`, visElemIdx: 1 });
        return;
      }
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

  return (
    <Flex flexDirection={"column"} maxHeight="100%" hidden={toggleVar}>
      <Text variant={"element_name"}>{currentTest.test} TEST PARAMETERS</Text>
      <Box>
        <form onSubmit={handleSubmitForm}>
          <SimpleGrid columns={2}>
            {Object.entries(currentTest.object).map(([key, value]) => (
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
            ))}
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
