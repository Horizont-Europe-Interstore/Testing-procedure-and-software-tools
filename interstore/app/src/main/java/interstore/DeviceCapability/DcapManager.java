package interstore.DeviceCapability;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class DcapManager {
    private  DeviceCapabilityImpl deviceCapabilityImpl;
    private static final Logger LOGGER = Logger.getLogger(DcapManager.class.getName());
    
    public DcapManager(DeviceCapabilityImpl deviceCapabilityImpl) {
        this.deviceCapabilityImpl = deviceCapabilityImpl;
    } 
public Object chooseMethod_basedOnAction(String payload) throws InterruptedException, JSONException, IOException{
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
        return getDeviceCapability(null);
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
//@GetMapping("/dcap") produces = MediaType.APPLICATION_XML_VALUE
@GetMapping(value = "/dcap")
public Object getDeviceCapability(HttpServletResponse response) throws InterruptedException, IOException {

    if (RequestContextHolder.getRequestAttributes() != null) {
        Map<String, Object> body = this.deviceCapabilityImpl.getDeviceCapabilities().getBody();
        @SuppressWarnings("unchecked")
        List<DeviceCapabilityDto> dcapList = (List<DeviceCapabilityDto>) body.get("deviceCapabilityDtos");
        if (dcapList != null && !dcapList.isEmpty()) {
            DeviceCapabilityDto dcapDto = dcapList.get(0);
           
            String dcap_val = this.deviceCapabilityImpl.getDeviceCapability(dcapDto);
            
            LOGGER.info("the dcap_val is " + dcap_val);
            byte[] bytes = dcap_val.getBytes(StandardCharsets.UTF_8);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/sep+xml;level=S1");
            response.setContentLength(bytes.length);
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
        }
        return null;
    } else {
        // It's a NATS or internal call
        ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityImpl.getDeviceCapabilities();
        return responseEntity.getBody();
    }

} 






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



