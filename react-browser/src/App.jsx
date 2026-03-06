import React from "react";
import { ChakraProvider } from '@chakra-ui/react'
import themeObject from './resources/chakra_config/theme.ts'
import {ModernTestInterface} from './page_components/ModernTestInterface.jsx';
import {ModernTestResults} from './page_components/ModernTestResults.jsx';

function App(){
  const theme = themeObject.theme;
  const colors = themeObject.colors;
  const [showModernResults, setShowModernResults] = React.useState(false);

  return(
    <ChakraProvider theme={theme}>
      {showModernResults ? (
        <ModernTestResults
          onBack={() => setShowModernResults(false)}
        />
      ) : (
        <ModernTestInterface
          colors={colors}
          onViewTestResults={() => setShowModernResults(true)}
        />
      )}
    </ChakraProvider>
  );
}

export {App}