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

function DerCurveForm({
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
    const [selectedNumber, setSelectedNumber] = useState(""); // Store selected dropdown option
    const [inputValues, setInputValues] = useState(""); // Store input value for the selected option

    useEffect(() => {
        // Reset selected optional field and input value whenever the test changes
        setSelectedNumber("");
        setInputValues({});
    }, [currentTest]);


    // List of mandatory fields for the "Create Der Curve" test
    const mandatoryFields = ["derProgramId", "mRID", "curveType", "x_multiplier_type", "y_multiplier_type", "y_ref_type", "description", "version"];

    const handleSubmitForm = async (event) => {
        event.preventDefault();

        console.log("Form Submission Initiated for Test:", currentTest.test);
        console.log("Current Test Object before Validation:", currentTest.object);

        // Validate input for the current test
        const validation = Client.getValid(currentTest.test);
        let updatedTestObject = currentTest.object; // Declare a local variable to hold the updated object

        if (currentTest.test === "Create Der Curve") {
            // Ensure all mandatory fields are filled
            for (let field of mandatoryFields) {
                const value = currentTest.object[field];
                if (value === "" || (validation && validation[field] && !validation[field](value))) {
                    setHeaderState({ text: `Invalid Field ${field}`, visElemIdx: 1 });
                    return;
                }
            }

            // If a number was selected, ensure the corresponding x_value/y_value pairs are filled
            if (selectedNumber) {
                for (let i = 1; i <= selectedNumber; i++) {
                    const xField = `x_value_${i}`;
                    const yField = `y_value_${i}`;
                    if (!inputValues[xField] || !inputValues[yField]) {
                        setHeaderState({ text: `Please enter values for ${xField} and ${yField}`, visElemIdx: 1 });
                        return;
                    }
                }

                // Add the x_value and y_value pairs to the current test object
                setCurrentTest((currentTest) => {
                    updatedTestObject = {
                        ...currentTest.object,
                        ...inputValues, // Ensure that inputValues are merged here
                    };
                    console.log("Updated Test Object with x/y values:", updatedTestObject); // Log to check the final structure
                    return { ...currentTest, object: updatedTestObject };
                });
            }

            // Add optional fields to currentTest.object
            const optionalFields = Object.keys(currentTest.object).filter(
                (field) => !mandatoryFields.includes(field) && !field.startsWith("x_value") && !field.startsWith("y_value")
            );

            optionalFields.forEach((field) => {
                const value = currentTest.object[field];
                if (value !== "") {
                    setCurrentTest((currentTest) => ({
                        ...currentTest,
                        object: {
                            ...currentTest.object,
                            [field]: value,
                        },
                    }));
                }
            });
        } else {
            // Handle validation for other tests
            for (let [key, value] of Object.entries(currentTest.object)) {
                if (value === "" || (validation && validation[key] && !validation[key](value))) {
                    setHeaderState({ text: `Invalid Field ${key}`, visElemIdx: 1 });
                    return;
                }
            }
        }

        console.log("Validation Passed. Submitting the form...");

        setTestState(false);
        setHeaderState({ text: "Running Test", visElemIdx: 2 });
        setToggle(true);

        console.log("Final Test Object being sent to Client.sendTest:", updatedTestObject);

        try {
            const res = await Client.sendTest({...currentTest, object: updatedTestObject});
            console.log("Response received from Client.sendTest:", res);
            setReport(res);
        } catch (err) {
            console.error("Error occurred during form submission:", err);
        }

        setHeaderState({ text: "Ready", visElemIdx: 0 });
        setTestState(true);
    };

    const handleInputChange = (event) => {
        const { id, value } = event.target;
        setInputValues((prevValues) => ({ ...prevValues, [id]: value }));
    };

    return (
        <Flex flexDirection={"column"} maxHeight="100%" hidden={toggleVar}>
            <Text variant={"element_name"}>{currentTest.test} TEST PARAMETERS</Text>
            <Box>
                <form onSubmit={handleSubmitForm}>
                    <SimpleGrid columns={2}>
                        {/* Render the form dynamically based on the currentTest.object */}
                        {currentTest.test === "Create Der Curve" ? (
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

                                {/* Dropdown for selecting number of x_value/y_value pairs */}
                                <Flex
                                    key={"optionalField"}
                                    border={"groove"}
                                    borderWidth={"1vh"}
                                    flexDirection={"column"}
                                    alignItems={"center"}
                                >
                                    <label>Select Number of x_value/y_value Pairs:</label>
                                    <Select
                                        placeholder="Select Number"
                                        value={selectedNumber} // Bind selected value
                                        onChange={(event) => {
                                            const value = event.target.value;
                                            if (!value) {
                                                // Clear inputs if no number is selected
                                                setSelectedNumber("");
                                                setInputValues({});
                                            }else {
                                                setSelectedNumber(parseInt(value, 10));
                                                setInputValues({}); // Reset inputs when new number is selected
                                            }
                                        }}
                                    >
                                        {Array.from({ length: 10 }, (_, i) => i + 1).map((number) => (
                                            <option key={number} value={number}>
                                                {number}
                                            </option>
                                        ))}
                                    </Select>
                                </Flex>

                                {/* Render input for the selected x_value/y_value pairs */}
                                {selectedNumber &&
                                    Array.from({ length: selectedNumber }, (_, i) => (
                                        <React.Fragment key={i}>
                                            <Flex
                                                border={"groove"}
                                                borderWidth={"1vh"}
                                                flexDirection={"column"}
                                                alignItems={"center"}
                                            >
                                                <label>{`x_value_${i + 1}`}:</label>
                                                <input
                                                    id={`x_value_${i + 1}`}
                                                    type="text"
                                                    value={inputValues[`x_value_${i + 1}`] || ""}
                                                    onChange={handleInputChange}
                                                />
                                            </Flex>
                                            <Flex
                                                border={"groove"}
                                                borderWidth={"1vh"}
                                                flexDirection={"column"}
                                                alignItems={"center"}
                                            >
                                                <label>{`y_value_${i + 1}`}:</label>
                                                <input
                                                    id={`y_value_${i + 1}`}
                                                    type="text"
                                                    value={inputValues[`y_value_${i + 1}`] || ""}
                                                    onChange={handleInputChange}
                                                />
                                            </Flex>
                                        </React.Fragment>
                                    ))}
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

export { DerCurveForm };