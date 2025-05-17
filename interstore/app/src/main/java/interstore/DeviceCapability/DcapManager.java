package interstore.DeviceCapability;

import interstore.JsonToXmlConverter;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
public class DcapManager {
    private  DeviceCapabilityImpl deviceCapabilityImpl;
//    private final JsonToXmlConverter converter = new JsonToXmlConverter();
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
        return addDeviceCapability(jsonObject);
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
    LOGGER.info("the response is " + deviceCapabilityDto);
    return deviceCapabilityImpl.getAllLinks(deviceCapabilityDto);
  
}


// check that the device capability exist or not if not create one . 
//@GetMapping("/dcap")
@GetMapping(value = "/dcap", produces = MediaType.APPLICATION_XML_VALUE)
public Object getDeviceCapability() throws MalformedURLException, InterruptedException {

    if (RequestContextHolder.getRequestAttributes() != null) {
        // It's an HTTP call
        Map<String, Object> body = this.deviceCapabilityImpl.getDeviceCapabilities().getBody();
        LOGGER.info("Response body: " + body);
        List<DeviceCapabilityDto> dcapList = (List<DeviceCapabilityDto>) body.get("deviceCapabilityDtos");
        if (dcapList != null && !dcapList.isEmpty()) {
            DeviceCapabilityDto dcap = dcapList.get(0); // or loop if there are multiple
            String dcap_val = this.deviceCapabilityImpl.getDeviceCapability(dcap);
            LOGGER.info("Response from getDeviceCapability: " + dcap_val);
            return dcap_val;
//            try {
//                return converter.convertMapToXml(dcap_val, "dcap");
//            } catch (Exception e) {
//                return "<error>Unable to convert to XML</error>";
//            }
        }
        return null;
    } else {
        // It's a NATS or internal call
        ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityImpl.getDeviceCapabilities();
        return responseEntity.getBody();
    }

} 



    //DeviceCapabilityDto deviceCapabilityDto = deviceCapabilityImpl.createDeviceCapability();
   
   //Thread.sleep(100);
   // LOGGER.info(" the response to nats from the server " + deviceCapabilityImpl.getAllLinks(deviceCapabilityDto));
   // return deviceCapabilityImpl.getAllLinks(deviceCapabilityDto);



public void updateDeviceCapability( JSONObject jsonObject) {
{ 
    LOGGER.info("Returned a 400 or 405 status message"); 
}
}

public String getTime(String payload) throws JSONException{
    return deviceCapabilityImpl.getTime(payload);
}

public void updateTime(String payload) throws JSONException{
    deviceCapabilityImpl.updateTime(payload);
}

}




/*
 *  
   @GetMapping("/dcap")
public  Map<String, Object> getDeviceCapability() throws MalformedURLException, InterruptedException {
    ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityImpl.getDeviceCapabilities();
    return responseEntity.getBody(); }

 * 
 * 
 */