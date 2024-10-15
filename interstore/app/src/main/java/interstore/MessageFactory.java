package interstore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
public class MessageFactory {
    String action;
    String natssubject;
    String payload;
    String serviceName; 
    public MicroServiceFactory microServiceFactory; 
    public static Map<String, Object> natsSubjectTotestclass = new HashMap<>(); 
    private static final Logger LOGGER = Logger.getLogger(MessageFactory.class.getName()); 
    public MessageFactory() 
    {
      this.microServiceFactory = new MicroServiceFactory(); 
    }
  
    // the service name should be the same one that send in the payload while start the test . 
    public Object getMicroservice(Map<String, Object> microservice, String serviceName)
    {
        Object serviceObject = microservice.get(serviceName); 
        return serviceObject; 
    }
    

    public void selfDeviceEndDeviceTests(String serviceName, String payLoad)
    {
        //{"payload":"http://localhost/edev","action":"get","servicename":"enddevicemanager"}  

    Object microServiceObject = getMicroservice(this.microServiceFactory.getDtoMap(), serviceName); 
    Class<?> microServiceClass = microServiceObject.getClass(); 
    LOGGER.info("microservice name is " + serviceName); 
    try {
        if(serviceName.equals("selfenddevicemanager") )
        {

            Method method = microServiceClass.getMethod("forwardResponse", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        else if(serviceName.equals("enddevicemanager"))
        {

            Method method = microServiceClass.getMethod("setEndDevices", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("createnewenddevice"))
        {

            Method method = microServiceClass.getMethod("setEndDevices", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("enddeviceinstancemanager"))
        {

            Method method = microServiceClass.getMethod("setEndDeviceInstance", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("enddevicelinkmanager"))
        {
            Method method = microServiceClass.getMethod("getEndDevice", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("enddeviceregistrationmanager"))
        {
            Method method = microServiceClass.getMethod("verifyRegisteredEndDevice", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        else if(serviceName.equals("getalldcapmanager"))
        {
            Method method = microServiceClass.getMethod("AllDeviceCapablity", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("findallregistrededendevice"))
        {
            Method method = microServiceClass.getMethod("setregisteredEndDeviceDetails", String.class); 
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("getallFsamanager"))  
        {
            Method method = microServiceClass.getMethod("setAllFsa", String.class);
            method.invoke(microServiceObject, payLoad);

        }

        else if(serviceName.equals("createFsamanager"))
        {
            Method method = microServiceClass.getMethod("setCreatedFunctionSetAssignment", String.class);
            method.invoke(microServiceObject, payLoad);

        } 
           
        else if(serviceName.equals("getASingleFsamanager"))
        {
            Method method = microServiceClass.getMethod("setSingleFunctionSetAssignments", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        
        else if(serviceName.equals("createDerprogrammanager"))
        {
            Method method = microServiceClass.getMethod("setCreatedDerProgram", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        else if(serviceName.equals("getallDerprogrammanager"))
        {
            Method method = microServiceClass.getMethod("setAllderPrograms", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        
        else if(serviceName.equals("getASingleDerprogrammanager"))
        {
            Method method = microServiceClass.getMethod("setADerprogram", String.class);
            method.invoke(microServiceObject, payLoad);
        }

         // #######################
        else if(serviceName.equals("createDerCapabilitymanager"))
        {
            Method method = microServiceClass.getMethod("setcreatedDerCapability", String.class);  
            method.invoke(microServiceObject, payLoad);

        }
        else if(serviceName.equals("getDerCapabilitymanager"))
        {
            Method method = microServiceClass.getMethod("setADerCapability", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        
        else if(serviceName.equals("createDerSettingsmanager"))
        {
            Method method = microServiceClass.getMethod("setcreatedDerSettings", String.class);
            method.invoke(microServiceObject, payLoad);

        }


        else if(serviceName.equals("getDerSettingsmanager"))
        {
            Method method = microServiceClass.getMethod("setADerSettings", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        
        
         // #######################

        else if(serviceName.equals("fsalistmanager"))
        {
            Method method = microServiceClass.getMethod("getFSAList", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        else if (serviceName.equals("fsamanager"))
        {
            Method method = microServiceClass.getMethod("getFSAInstance", Object.class);
            method.invoke(microServiceObject, payLoad);
        }
        else if (serviceName.equals("derplistmanager"))
        {
            Method method = microServiceClass.getMethod("getDERProgramInstance", String.class);
            method.invoke(microServiceObject, payLoad.toString());
        }
        else if (serviceName.equals("derprogrammanager"))
        {
            Method method = microServiceClass.getMethod("getDERProgramInstance", String.class);
            method.invoke(microServiceObject, payLoad.toString());
        }
      

        else if (serviceName.equals("timemanager"))
        {
            Method method = microServiceClass.getMethod("getTimeResponse", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        else if (serviceName.equals("advancedtimemanager"))
        {
            Method method = microServiceClass.getMethod("getUpdatedTimeResponse", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        else if(serviceName.equals("createDerCurveManager"))
        {
            Method method = microServiceClass.getMethod("setCreatedDerCurve", String.class);
            method.invoke(microServiceObject, payLoad);

        }
        else if(serviceName.equals("getASingleDerCurveManager"))
        {
            Method method = microServiceClass.getMethod("setADerCurve", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        else if(serviceName.equals("createDerControlManager"))
        {
            Method method = microServiceClass.getMethod("setCreatedDerControl", String.class);
            method.invoke(microServiceObject, payLoad);

        }


        else if(serviceName.equals("dcapmanager")){
            Method method = microServiceClass.getMethod("DeviceCapability", String.class);
            method.invoke(microServiceObject, payLoad);
        }
        
          
           
       } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.warning("Error invoking method: " + e.getMessage());   
        } 
       
       }

        
    }

   
   
    
    
    






    
    
   




  


    
    

