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
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class DcapManager {
    private  DeviceCapabilityImpl deviceCapabilityImpl;
    private static final Logger LOGGER = Logger.getLogger(DcapManager.class.getName());
    
    public DcapManager(DeviceCapabilityImpl deviceCapabilityImpl) {
        this.deviceCapabilityImpl = deviceCapabilityImpl;
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



   public Map<Long, Object>addDeviceCapability( JSONObject jsonObject) throws MalformedURLException, InterruptedException, JSONException  {
    LOGGER.info("the json is " + jsonObject); 
    DeviceCapabilityDto deviceCapabilityDto = deviceCapabilityImpl.createDeviceCapability(jsonObject);
    LOGGER.info("the response is " + deviceCapabilityDto);
    return deviceCapabilityImpl.getAllLinks(deviceCapabilityDto);
  
}


@GetMapping(value = "/dcap")
public Object getDeviceCapability(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException, CertificateEncodingException, NoSuchAlgorithmException {
    X509Certificate[] certs = (X509Certificate[]) 
        request.getAttribute("javax.servlet.request.X509Certificate");
    if(certs != null && certs.length >0)
    {
        String Lfdi = calculateLFDI(certs[0]);
        LOGGER.info("the Lfdi is " + Lfdi);
    }
    if (RequestContextHolder.getRequestAttributes() != null) {
        try {
            Map<String, Object> body = this.deviceCapabilityImpl.getDeviceCapabilities().getBody();
            @SuppressWarnings("unchecked")
            List<DeviceCapabilityDto> dcapList = (List<DeviceCapabilityDto>) body.get("deviceCapabilityDtos");
            
            DeviceCapabilityDto dcapDto;
            if (dcapList == null || dcapList.isEmpty()) {
                LOGGER.info("No device capabilities found, creating default one for Schneider polling");
                dcapDto = this.deviceCapabilityImpl.createDefaultDeviceCapability();
            } else {
                dcapDto = dcapList.get(0);
            }
           
            String dcap_val = this.deviceCapabilityImpl.getDeviceCapability(dcapDto);
            
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
        ResponseEntity<Map<String, Object>> responseEntity = this.deviceCapabilityImpl.getDeviceCapabilities();
        return responseEntity.getBody();
    }

} 


public String calculateLFDI(X509Certificate cert) throws InterruptedException, IOException, NoSuchAlgorithmException, CertificateEncodingException{
    byte [] certBytes = cert.getEncoded();
    java.security.MessageDigest sha256 = java.security.MessageDigest.getInstance("SHA-256");
    byte[] fingerprint = sha256.digest(certBytes);
    StringBuilder hex = new StringBuilder();
    for(byte b: fingerprint)
    {
        hex.append(String.format("%02X", b));
    }
    String lfdiRaw = hex.substring(0, 40);
    return lfdiRaw;
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



