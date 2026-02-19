package interstore.DeviceCapability;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
// import interstore.Util.SfdiUtil;


@RestController
public class DcapManager {
    private  DeviceCapabilityService deviceCapabilityService;
    private static final Logger LOGGER = Logger.getLogger(DcapManager.class.getName());
    
    public DcapManager(DeviceCapabilityService deviceCapabilityService) {
        this.deviceCapabilityService = deviceCapabilityService;
    } 
public Object chooseMethod_basedOnAction(String payload) throws Exception{
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
        return getDeviceCapability(null, null);
        case "get-time":
        return getTime(jsonObject.getString("payload"));
        case "put":
        updateDeviceCapability(jsonObject);
        return "Device capability updated";
        case "update-time":
        updateTime(jsonObject.getString("payload"));
        return "Time updated";
      }
      return "Operation completed successfully"; 
   }



   public Map<String, Object> addDeviceCapability( JSONObject jsonObject) throws MalformedURLException, InterruptedException, JSONException  {
    DeviceCapabilityEntity deviceCapabilityDto = deviceCapabilityService.createDeviceCapability(jsonObject);
    Map<String, Object> body = this.deviceCapabilityService.getDeviceCapabilities().getBody();
    @SuppressWarnings("unchecked")
    List<DeviceCapabilityEntity> dcapList = (List<DeviceCapabilityEntity>) body.get("deviceCapabilityDtos");
    DeviceCapabilityEntity dcapDto;
    if (dcapList == null || dcapList.isEmpty()) {
        LOGGER.info("No device capabilities found, creating default one for Schneider polling");
        dcapDto = this.deviceCapabilityService.createDefaultDeviceCapability();
    } else {
        dcapDto = dcapList.get(0);
    }
   
    return Map.of("timeLink", dcapDto.getTimeLink(),"mirrorUsagePointListLink", dcapDto.getMirrorUsagePointListLink(),"selfDeviceLink", dcapDto.getSelfDeviceLink(),"endDeviceListLink", dcapDto.getEndDeviceListLink());
}


    @GetMapping(value = "/dcap", produces = "application/sep+xml")
    public ResponseEntity<String> getDeviceCapability(
        @RequestHeader(value = "X-Server-LFDI", required = false) String serverLfdi,
        @RequestHeader(value = "X-Server-SFDI", required = false) String serverSfdi
     ) throws Exception {

             Map<String, Object> body =
            deviceCapabilityService.getDeviceCapabilities().getBody();

           @SuppressWarnings("unchecked")
            List<DeviceCapabilityEntity> dcapList =
            (List<DeviceCapabilityEntity>) body.get("deviceCapabilityDtos");

            DeviceCapabilityEntity dcapDto =
            (dcapList == null || dcapList.isEmpty())
                    ? deviceCapabilityService.createDefaultDeviceCapability()
                    : dcapList.get(0);

            String dcapVal =
            deviceCapabilityService.getDeviceCapabilityHttp(dcapDto);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/sep+xml;level=S1");
            headers.set("Cache-Control", "no-cache");

          if (serverLfdi != null && serverSfdi != null) {
             headers.set("X-Server-LFDI", serverLfdi);
            headers.set("X-Server-SFDI", serverSfdi);
            }

           return new ResponseEntity<>(dcapVal, headers, HttpStatus.OK);
      }

       @GetMapping(value = "/dcap/tm", produces = "application/sep+xml")
        public ResponseEntity<String> getTimeHttp() throws JSONException{
            Map<String, Object> body = this.deviceCapabilityService.getDeviceCapabilities().getBody();
            @SuppressWarnings("unchecked")
            List<DeviceCapabilityEntity> dcapList = (List<DeviceCapabilityEntity>) body.get("deviceCapabilityDtos");
            DeviceCapabilityEntity dcapDto = null;
            if (dcapList == null || dcapList.isEmpty()) {
            LOGGER.info("No device capabilities found, creating default one");
            dcapDto = this.deviceCapabilityService.createDefaultDeviceCapability();
            } else {
            dcapDto = dcapList.get(0);
           }   
           String time_val = deviceCapabilityService.getTimeHttp(dcapDto.getTimeLink());
           LOGGER.info("the time_val is " + time_val);
    
           HttpHeaders headers = new HttpHeaders();
           headers.set("Content-Type", "application/sep+xml;level=S1");
           headers.set("Cache-Control", "no-cache");
           return new ResponseEntity<>(time_val, headers, HttpStatus.OK);

        }

public void updateDeviceCapability( JSONObject jsonObject) {
{ 
    LOGGER.info("Returned a 400 or 405 status message"); 
}
}

public String getTime(String payload) throws JSONException{
    return deviceCapabilityService.getTime(payload);
}


public void updateTime(String payload) throws JSONException{
    deviceCapabilityService.updateTime(payload);
}



}



