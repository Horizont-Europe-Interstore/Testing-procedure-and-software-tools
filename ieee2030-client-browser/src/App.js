import { useState } from "react";

function App() {
  const [command, setCommand] = useState("");
  const [output, setOutput] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const apiBaseUrl = process.env.REACT_APP_EXPRESS_URL || "http://express-mid:5000";

    try {
      const response = await fetch(`${apiBaseUrl}/run`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ command }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Unknown error");
      }

      const data = await response.json();
      setOutput(data.output);
    } catch (err) {
      setOutput(`Error: ${err.message}`);
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h1>IEEE2030.5 Client Command Runner</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter command (e.g., dcap)"
          value={command}
          onChange={(e) => setCommand(e.target.value)}
          style={{ width: "300px", marginRight: "1rem" }}
        />
        <button type="submit">Run</button>
      </form>
      <pre style={{ marginTop: "1rem", whiteSpace: "pre-wrap", background: "#eee", padding: "1rem" }}>
        {output}
      </pre>
    </div>
  );
}

export default App;

