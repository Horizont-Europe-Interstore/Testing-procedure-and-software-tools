package interstore.DeviceCapability;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import interstore.Util.SfdiUtil;


@RestController
public class DcapManager {
    private  DeviceCapabilityService deviceCapabilityService;
    private static final Logger LOGGER = Logger.getLogger(DcapManager.class.getName());
    
    public DcapManager(DeviceCapabilityService deviceCapabilityService) {
        this.deviceCapabilityService = deviceCapabilityService;
    } 
public Object chooseMethod_basedOnAction(String payload) throws InterruptedException, JSONException, IOException, CertificateEncodingException, NoSuchAlgorithmException{
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
    LOGGER.info("the json is " + jsonObject); 
    DeviceCapabilityEntity deviceCapabilityDto = deviceCapabilityService.createDeviceCapability(jsonObject);
    LOGGER.info("the response is " + deviceCapabilityDto);
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
    // LOGGER.info("the dcapDto is: " + dcapDto);
    return Map.of("timeLink", dcapDto.getTimeLink(),"mirrorUsagePointListLink", dcapDto.getMirrorUsagePointListLink(),"selfDeviceLink", dcapDto.getSelfDeviceLink(),"endDeviceListLink", dcapDto.getEndDeviceListLink());
}


@GetMapping(value = "/dcap")
public Object getDeviceCapability(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException, CertificateEncodingException, NoSuchAlgorithmException {
    
    if (RequestContextHolder.getRequestAttributes() != null) {
        try {
            // Get LFDI/SFDI from C++ proxy headers
            String serverLfdi = request.getHeader("X-Server-LFDI");
            String serverSfdi = request.getHeader("X-Server-SFDI");
            
            if (serverLfdi != null && serverSfdi != null) {
                LOGGER.info("Using LFDI from proxy - LFDI: " + serverLfdi + ", SFDI: " + serverSfdi);
                response.setHeader("X-Server-LFDI", serverLfdi);
                response.setHeader("X-Server-SFDI", serverSfdi);
            } else {
                LOGGER.warning("No LFDI/SFDI headers from proxy");
            }
            
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
           
            String dcap_val = this.deviceCapabilityService.getDeviceCapabilityHttp(dcapDto);
            
            LOGGER.info("the dcap_val is " + dcap_val);
            byte[] bytes = dcap_val.getBytes(StandardCharsets.UTF_8);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/sep+xml;level=S1");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");
            response.setContentLength(bytes.length);
            
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            LOGGER.severe("Error retrieving device capabilities: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    } else {
        ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityService.getDeviceCapabilities();
        return responseEntity;
    }

} 



public void updateDeviceCapability( JSONObject jsonObject) {
{ 
    LOGGER.info("Returned a 400 or 405 status message"); 
}
}

public String getTime(String payload) throws JSONException{
    return deviceCapabilityService.getTime(payload);
}

@GetMapping(value = "/dcap/tm")
public Object getTimeHttp(HttpServletResponse response) throws JSONException{
    try{
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
        String time_val = deviceCapabilityService.getTimeHttp(dcapDto.getTimeLink());
        LOGGER.info("the time_val is " + time_val);
        byte[] bytes = time_val.getBytes(StandardCharsets.UTF_8);
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/sep+xml;level=S1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setContentLength(bytes.length);
        
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
    } catch (Exception e) {
        LOGGER.severe("Error retrieving device capabilities: " + e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    return null;
}

public void updateTime(String payload) throws JSONException{
    deviceCapabilityService.updateTime(payload);
}



}



