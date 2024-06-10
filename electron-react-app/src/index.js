import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import {App} from "./App.jsx";
import { Center } from '@chakra-ui/react'
import themeObject from '../resources/chakra_config/theme.ts'


const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Center  padding='1vh' border='outset' borderColor={themeObject.colors.SECONDARY_COLOR} 
           borderRadius='2%' borderWidth='0.8vh' bgColor={themeObject.colors.PRIMARY_COLOR} h='100vh' w='100vw'>
    <App/>
  </Center>
      
);