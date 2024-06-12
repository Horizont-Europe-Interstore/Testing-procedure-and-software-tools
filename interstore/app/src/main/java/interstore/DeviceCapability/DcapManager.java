package interstore.DeviceCapability;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class DcapManager {
    private  DeviceCapabilityImpl deviceCapabilityImpl;
    private static final Logger LOGGER = Logger.getLogger(DcapManager.class.getName());
    public DcapManager(DeviceCapabilityImpl deviceCapabilityImpl) {
        this.deviceCapabilityImpl = deviceCapabilityImpl;
    } 
public Object chooseMethod_basedOnAction(String payload) throws MalformedURLException, InterruptedException, JSONException{
if (payload == null || payload.isEmpty()) {
        throw new IllegalArgumentException("payload cannot be null or empty");
    }
      JSONObject jsonObject = new JSONObject(payload);
      if(!jsonObject.has("action"))
      {
          throw new IllegalArgumentException("action cannot be null or empty");
      }
      String action = jsonObject.getString("action");
      switch(action){
        case"post":
        return  addDeviceCapability(jsonObject);
        case "get":
        return getDeviceCapability();
        case "get-time":
        return getTime(jsonObject.getString("payload"));
        case "put":
        updateDeviceCapability(jsonObject);
        case "update-time":
        updateTime(jsonObject.getString("payload"));
        break; 
      }
      return "Operation completed successfully"; 
   }




   public Map<Long, Object>addDeviceCapability( JSONObject jsonObject) throws MalformedURLException, InterruptedException, JSONException  {
    LOGGER.info("the json is " + jsonObject); 
    DeviceCapabilityDto deviceCapabilityDto = deviceCapabilityImpl.createDeviceCapability(jsonObject);

    return deviceCapabilityImpl.getAllLinks(deviceCapabilityDto);
  
}


// check that the device capability exist or not if not create one . 
@GetMapping("/dcap")
public  Map<String, Object> getDeviceCapability() throws MalformedURLException, InterruptedException {
    ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityImpl.getDeviceCapabilities();
    return responseEntity.getBody();

   
   
    //DeviceCapabilityDto deviceCapabilityDto = deviceCapabilityImpl.createDeviceCapability();
   
   //Thread.sleep(100);
   // LOGGER.info(" the response to nats from the server " + deviceCapabilityImpl.getAllLinks(deviceCapabilityDto));
   // return deviceCapabilityImpl.getAllLinks(deviceCapabilityDto);

   
}







 




public void updateDeviceCapability( JSONObject jsonObject) {
{ 
    LOGGER.info("Returned a 400 or 405 status message"); 
}
}

public String getTime(String payload) throws JSONException{
    return deviceCapabilityImpl.getTime(payload);
}

public void updateTime(String payload){
    deviceCapabilityImpl.updateTime(payload);
}

}




/*
 *  

 * 
 * 
 */