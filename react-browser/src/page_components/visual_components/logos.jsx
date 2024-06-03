
import React from "react";
function CheckMark(){
    return (
      <svg height={'1em'} viewBox="0 0 512 512"><path fill="green" d="M504 256c0 136.967-111.033 248-248 248S8 392.967 8 256 119.033 8 256 8s248 111.033 248 248zM227.314 387.314l184-184c6.248-6.248 6.248-16.379 0-22.627l-22.627-22.627c-6.248-6.249-16.379-6.249-22.628 0L216 308.118l-70.059-70.059c-6.248-6.248-16.379-6.248-22.628 0l-22.627 22.627c-6.248 6.248-6.248 16.379 0 22.627l104 104c6.249 6.249 16.379 6.249 22.628.001z"></path></svg>
    )
  }
  
  function CrossMark(){
    return (
      <svg height={'1em'} viewBox="0 0 512 512" ><path fill="red" d="M256 8C119 8 8 119 8 256s111 248 248 248 248-111 248-248S393 8 256 8zm121.6 313.1c4.7 4.7 4.7 12.3 0 17L338 377.6c-4.7 4.7-12.3 4.7-17 0L256 312l-65.1 65.6c-4.7 4.7-12.3 4.7-17 0L134.4 338c-4.7-4.7-4.7-12.3 0-17l65.6-65-65.6-65.1c-4.7-4.7-4.7-12.3 0-17l39.6-39.6c4.7-4.7 12.3-4.7 17 0l65 65.7 65.1-65.6c4.7-4.7 12.3-4.7 17 0l39.6 39.6c4.7 4.7 4.7 12.3 0 17L312 256l65.6 65.1z"></path></svg>
    )
  }
  
  function SquareMark(){
    return (
      <svg height={'1em'} focusable="false" data-prefix="fas" data-icon="stop-circle"role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" data-status="SKIPPED"><path fill="#89CFF0" d="M256 8C119 8 8 119 8 256s111 248 248 248 248-111 248-248S393 8 256 8zm96 328c0 8.8-7.2 16-16 16H176c-8.8 0-16-7.2-16-16V176c0-8.8 7.2-16 16-16h160c8.8 0 16 7.2 16 16v160z"></path></svg>
    )
  }

  function HeaderCheck({colors,testState}){
    let hidden = testState;
    React.useEffect(()=>{
      hidden=testState;
    })
    return (<svg hidden={!hidden} width='100%' height='100%' xmlns="http://www.w3.org/2000/svg" fill={colors.PRIMARY_COLOR} viewBox="-5 -10 30 30">
      <path d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm10.03 4.97a.75.75 0 0 1 .011 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.75.75 0 0 1 1.08-.022z"/>
    </svg>);
  }

  function HeaderLoad({colors,testState}){
    const lim = 7 // strictly < lim from 0
    const loadString = "2 0a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0m";
    const baseString = "M3 4.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0m";
    const [counter,setCounter] = React.useState(0);
    let hidden = testState;
    const advanceCounter =()=>{
        setCounter((counter+1)%lim);
    }
    React.useEffect(()=>{
      if(!hidden){
        setTimeout(advanceCounter,100)
      }
      hidden=testState;
    })

    return(
      <svg hidden={hidden} width='100%' height='100%' xmlns="http://www.w3.org/2000/svg" fill={colors.PRIMARY_COLOR} viewBox="-5 -10 30 30">
        <path d="M14 10a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-1a1 1 0 0 1 1-1zM2 9a2 2 0 0 0-2 2v1a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-1a2 2 0 0 0-2-2z"/>
        <path d="M14 3a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1zM2 2a2 2 0 0 0-2 2v1a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z"/>  
        <path d={baseString+loadString.repeat(counter)}/>
      </svg>
    );
  }

  export {SquareMark,CrossMark,CheckMark,HeaderCheck,HeaderLoad};