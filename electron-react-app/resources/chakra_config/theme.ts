import {extendTheme,defineStyleConfig} from '@chakra-ui/react'
import '@fontsource-variable/roboto-mono';

const PRIMARY_COLOR = '#6B5B95'
const SECONDARY_COLOR = 'white'
const TEXT_COLOR = 'black'
const GOOD_COLOR = '#45B8AC'
const BAD_COLOR = '#F7CAC9'

const Button = defineStyleConfig({
    variants: {
      controle_buttons: {
        border: '2px solid',
        borderColor: PRIMARY_COLOR,
        bg: SECONDARY_COLOR,
        textColor:PRIMARY_COLOR,
        _hover:{
            bg: PRIMARY_COLOR,
            textColor:SECONDARY_COLOR
        }
      },
      cancel_form_button: {
        border: '2px solid',
        borderColor: BAD_COLOR,
        bg: SECONDARY_COLOR,
        textColor:BAD_COLOR,
        _hover:{
            bg: BAD_COLOR,
            textColor:SECONDARY_COLOR
        }
      },
      submit_form_button: {
        border: '2px solid',
        borderColor: GOOD_COLOR,
        bg: SECONDARY_COLOR,
        textColor:GOOD_COLOR,
        _hover:{
            bg: GOOD_COLOR,
            textColor:SECONDARY_COLOR
        }
      }
    
    }
  })

  const Text = defineStyleConfig({
    variants: {
        element_name:{
            h:'10%',
            textColor: TEXT_COLOR,
            textAlign:'center',
            fontSize:'3xl',
            fontWeight:'bold'
        },
        report_key:{
            paddingLeft:'1vw', 
            paddingTop:'1.5vh',
            fontWeight:'bold',
            fontSize:'1xl'
        },
        report_value:{
            paddingLeft:'0.3vw',
            paddingTop: '1vw'
        }
    }
  })

  const Tooltip = defineStyleConfig({
    variants:{
        test_desc_tt:{
            textColor: SECONDARY_COLOR,
            textAlign:'center', 
            w:'9vw',
            bgColor:PRIMARY_COLOR
        }
    }
  })

  const themeObject = {
    theme:extendTheme({
      components: {
        Button,
        Tooltip,
        Text
      }
    }),
    colors:{
      PRIMARY_COLOR:PRIMARY_COLOR,
      SECONDARY_COLOR:SECONDARY_COLOR,
      TEXT_COLOR:TEXT_COLOR,
      BAD_COLOR:BAD_COLOR,
      GOOD_COLOR:GOOD_COLOR
  }
}
export default themeObject


/*


*/