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

function DerControlSubForm({
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
    const [selectedField, setSelectedField] = useState(""); // Store selected dropdown option
    const [inputValues, setInputValues] = useState({}); // Store input values for optional fields
    const [selectedFields, setSelectedFields] = useState([]); // Store all selected optional fields

    useEffect(() => {
        // Reset selected field and input values whenever the test changes
        setSelectedField("");
        setInputValues({});
        setSelectedFields([]);
    }, [currentTest]);

    // List of mandatory fields
    const mandatoryFields = [
        "derProgramId",
        "deviceCategory",
        "mRID",
        "description",
        "version",
        "duration",
        "start",
        "currentStatus",
        "dateTime",
        "potentiallySuperseded",
        "randomizeDuration",
        "randomizeStart",
        // Add more mandatory fields as necessary
    ];

    // Gather all fields defined in the currentTest.object
    const allFields = Object.keys(currentTest.object);
    // Separate non-mandatory fields (optional fields) from mandatory ones
    const nonMandatoryFields = allFields.filter(field => !mandatoryFields.includes(field));

    const handleSubmitForm = async (event) => {
        event.preventDefault();

        // Validate mandatory fields
        for (let field of mandatoryFields) {
            const value = currentTest.object[field];
            if (value === "") {
                setHeaderState({ text: `Invalid Field ${field}`, visElemIdx: 1 });
                return;
            }
        }

        // Merge the input values for optional fields into the current test object
        const updatedTestObject = {
            ...currentTest.object,
            ...inputValues, // Include dynamic inputs for optional fields
        };

        console.log("Validation Passed. Submitting the form...");

        setTestState(false);
        setHeaderState({ text: "Running Test", visElemIdx: 2 });
        setToggle(true);

        console.log("Final Test Object being sent to Client.sendTest:", updatedTestObject);

        try {
            const res = await Client.sendTest({ ...currentTest, object: updatedTestObject });
            console.log("Response received from Client.sendTest:", res);
            console.log("Report after setting:", res);
            setReport(res);
        } catch (err) {
            console.error("Error occurred during form submission:", err);
        }

        setHeaderState({ text: "Ready", visElemIdx: 0 });
        setTestState(true);
    };

    const handleInputChange = (event) => {
        const { id, value } = event.target;
        setCurrentTest((currentTest) => ({
            ...currentTest,
            object: { ...currentTest.object, [id]: value }, // Update the currentTest.object with the input
        }));
    };

    const handleOptionalInputChange = (event) => {
        const { id, value } = event.target;
        setInputValues((prevValues) => ({ ...prevValues, [id]: value }));
    };

    const handleAddField = () => {
        // Add selected field to the list of selected fields
        if (selectedField && !selectedFields.includes(selectedField)) {
            setSelectedFields([...selectedFields, selectedField]);
        }
        setSelectedField(""); // Reset the dropdown after adding
    };

    return (
        <Flex flexDirection={"column"} maxHeight="100%" hidden={toggleVar}>
            <Text variant={"element_name"}>{currentTest.test} TEST PARAMETERS</Text>
            <Box>
                <form onSubmit={handleSubmitForm}>
                    <SimpleGrid columns={2}>
                        {/* Render mandatory fields directly and make them editable */}
                        {mandatoryFields.map((field) => (
                            <Flex
                                key={field}
                                border={"groove"}
                                borderWidth={"1vh"}
                                flexDirection={"column"}
                                alignItems={"center"}
                            >
                                <label>{field}:</label>
                                <input
                                    id={field}
                                    type="text"
                                    value={currentTest.object[field] || ""} // Bind to currentTest.object
                                    onChange={handleInputChange} // Allow edits
                                />
                            </Flex>
                        ))}

                        {/* Dropdown for selecting non-mandatory fields */}
                        <Flex
                            key={"optionalField"}
                            border={"groove"}
                            borderWidth={"1vh"}
                            flexDirection={"column"}
                            alignItems={"center"}
                        >
                            <label>Select Optional Field:</label>
                            <Select
                                placeholder="Select Field"
                                value={selectedField} // Bind selected value
                                onChange={(event) => {
                                    const value = event.target.value;
                                    setSelectedField(value); // Set the selected field
                                }}
                            >
                                {nonMandatoryFields.map((field) => (
                                    <option key={field} value={field}>
                                        {field}
                                    </option>
                                ))}
                            </Select>
                            <Button mt={2} onClick={handleAddField}>Add Field</Button>
                        </Flex>

                        {/* Render input for the selected non-mandatory fields */}
                        {selectedFields.map((field) => (
                            <Flex
                                key={field}
                                border={"groove"}
                                borderWidth={"1vh"}
                                flexDirection={"column"}
                                alignItems={"center"}
                            >
                                <label>{field}:</label>
                                <input
                                    id={field}
                                    type="text"
                                    value={inputValues[field] || ""}
                                    onChange={handleOptionalInputChange} // Allow edits for optional fields
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

export { DerControlSubForm };
