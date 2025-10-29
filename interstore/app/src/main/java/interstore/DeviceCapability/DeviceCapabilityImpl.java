package interstore.DeviceCapability;
import interstore.EndDevice.EndDeviceDto;
import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Time.TimeDto;
import interstore.Time.TimeDtoRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceCapabilityImpl {

    @Autowired
    private DeviceCapabilityRepository deviceCapabilityRepository;

    @Autowired
    TimeDtoRepository timeDtoRepository;
  
    
    private static final Logger LOGGER = Logger.getLogger(DeviceCapabilityImpl.class.getName());

    @Transactional
    public DeviceCapabilityDto createDeviceCapability(JSONObject jsonObject) throws MalformedURLException, JSONException {
        LOGGER.info("Inside DcapImpl: " + jsonObject);
        List<DeviceCapabilityDto> dcap = deviceCapabilityRepository.findAll();
        if(!dcap.isEmpty()){
            LOGGER.log(Level.INFO, "Dcap entity already exists!");
            return null;
        } else{
            JSONObject payload = jsonObject.getJSONObject("payload");
            DeviceCapabilityDto deviceCapabilityDto = new DeviceCapabilityDto();
            try {
                deviceCapabilityDto = deviceCapabilityRepository.save(deviceCapabilityDto);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error saving DeviceCapabilityDto", e);
                throw e; 
            }
            
            setDeviceCapability(deviceCapabilityDto, payload);
            LOGGER.info("Leaving DcapImpl");
            return deviceCapabilityDto;
        }
        
    }

    private String stripHost(String url) {
        if (url == null) return null;
        try {
            String path = new java.net.URL(url).getPath(); // e.g. "/2030.5/dcap/tm"
            int index = path.indexOf("/2030.5");
            if (index != -1) {
                return path.substring(index + "/2030.5".length()); // skip the "/2030.5"
            }
            return path;
        } catch (Exception e) {
            // If already relative or invalid URL, try trimming manually
            int index = url.indexOf("/2030.5");
            if (index != -1) {
                return url.substring(index + "/2030.5".length());
            }
            return url;
        }
    }

    // public ResponseEntity<Map<String, Object>> getDeviceCapability(DeviceCapabilityDto dcapDto){
    //     Map<String, Object> responseMap = new HashMap<>();
    //     try {
    //         De endDeviceDto = deviceCapabilityRepository.findById(id).orElse(null);
    //         LOGGER.log(Level.INFO, "EndDeviceDto retrieved successfully" + endDeviceDto);
    //         if(endDeviceDto == null) {
    //             responseMap.put("message", "No endDevice found.");
    //         }
    //         else {
    //             responseMap.put("endDevice", endDeviceDto);
    //         }
    //         return ResponseEntity.ok(responseMap);
    //     } catch (Exception e) {
    //         LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
            
    //         return ResponseEntity.status(404).body(null);
    //     }
    // }

     public String getDeviceCapabilityHttp(DeviceCapabilityDto dcap) {
        return "<DeviceCapability xsi:schemaLocation=\"urn:ieee:std:2030.5:ns sep.xsd\" " +
               "xmlns=\"urn:ieee:std:2030.5:ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
               "href=\"/dcap\">\n" +
               "  <TimeLink href=\"/dcap/tm\"/>\n" +
               "  <EndDeviceListLink all=\"1\" href=\"/edev\"/>\n" +
               "</DeviceCapability>";
     }



    public void setDeviceCapability(DeviceCapabilityDto deviceCapabilityDto, JSONObject jsonObject) throws JSONException
    {
        LOGGER.info("Inside DcapImpl -> setDeviceCapability: " + jsonObject);
        if(deviceCapabilityDto.getId() == null)
        {
            throw new IllegalStateException("DeviceCapabilityDto must be persisted before adding links.");  
        }
        
        
        Link link = new Link(); 
        String selfDevcieLink = jsonObject.getString("selfDeviceLink");  
        link.setLink(selfDevcieLink); 
        deviceCapabilityDto.addLink(link.getLink());
        deviceCapabilityDto.setSelfDeviceLink(link.getLink());
        
         ListLink listLink = new ListLink(); 
         String endDeviceListLink = jsonObject.getString("endDeviceListLink");
         listLink.setListLink(endDeviceListLink);
         deviceCapabilityDto.addLink(listLink.getListLink());
        deviceCapabilityDto.setEndDeviceListLink(listLink.getListLink());
         String mirrorUsagePointListLink = jsonObject.getString("mirrorUsagePointListLink");
         listLink.setListLink(mirrorUsagePointListLink );
         deviceCapabilityDto.addLink(listLink.getListLink());
        deviceCapabilityDto.setMirrorUsagePointListLink(listLink.getListLink());
        String timeLink = jsonObject.getString("timeLink");
        link.setLink("dcap/"+timeLink);
        deviceCapabilityDto.addLink(link.getLink());
        deviceCapabilityDto.setTimeLink(link.getLink());

        TimeDto time = new TimeDto();
        time.setTimeLink(link.getLink());
        timeDtoRepository.save(time);
        LOGGER.info("Leaving DcapImpl -> setDeviceCapability");
    }

    public Map<Long, Object> getAllLinks(DeviceCapabilityDto deviceCapabilityDto) {
        if (deviceCapabilityDto == null || deviceCapabilityDto.getId() == null) {
            throw new IllegalArgumentException("DeviceCapabilityDto is null or does not have a valid ID.");
        }
    
        
        ListLink listLink = new ListLink();
        Object urls = listLink.getListLinks(deviceCapabilityDto);
        Map<Long, Object> result = new HashMap<>();
        result.put(deviceCapabilityDto.getId(), urls);
        LOGGER.log(Level.INFO, "the result from getAllLinks: " + result.toString()); 
        return result;
    }
    

    // write a method to get all the id's or device capablities registered on the 
    // devcie capabality server , like a get all query to the jpa repository a map with 
    // id as the key and put list as the value with this list contains objects in a deserilized 
    // version 

    public ResponseEntity<Map<String, Object>> getDeviceCapabilities()
    {
        Map<String, Object> responseMap = new HashMap<>();
        try{
            List<DeviceCapabilityDto> deviceCapabilityDtos = deviceCapabilityRepository.findAll();
            if(deviceCapabilityDtos.isEmpty()
            || deviceCapabilityDtos == null)
            {
                responseMap.put("message", "No DeviceCapabilityDtos found.");
                return ResponseEntity.ok(responseMap);
            }
            else
                {
                    responseMap.put("deviceCapabilityDtos", deviceCapabilityDtos);
                    return ResponseEntity.ok(responseMap); 
                }
        }
        
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE, "Error getting all DeviceCapabilityDtos", e);
        }
       
        return null; 
    }

    @Transactional
    public String getTime(String payload) throws JSONException{
        TimeDto timeDto = timeDtoRepository.findByTimeLink(payload);
        JSONObject object = new JSONObject();
        object.put("time_instance", timeDto.getCurrentTime());
        object.put("quality", timeDto.getQuality());
        return object.toString();
    }

    @Transactional
    public String getTimeHttp(String payload) throws JSONException{
            TimeDto timeDto = timeDtoRepository.findByTimeLink(payload);

            return "<Time xmlns=\"urn:ieee:std:2030.5:ns\" " +
           "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
           "xsi:schemaLocation=\"urn:ieee:std:2030.5:ns sep.xsd\" " +
           "href=\"/dcap/tm\">\n" +
           "  <currentTime>" + timeDto.getCurrentTime() + "</currentTime>\n" +
           "  <quality>" + timeDto.getQuality() + "</quality>\n" +
           "</Time>";
    }

    @Transactional
    public String updateTime(String payload) throws JSONException{
        JSONObject jsonObject = new JSONObject(payload);
        long updatedTimeInstance = jsonObject.getLong("updated_time_instance");
        String timeLink = jsonObject.getString("timeLink");
        TimeDto timeDto = timeDtoRepository.findByTimeLink(timeLink);
        timeDto.setCurrentTime(String.valueOf(updatedTimeInstance));
        timeDtoRepository.save(timeDto);
        return "";
    }

    @Transactional
    public DeviceCapabilityDto createDefaultDeviceCapability() {
        DeviceCapabilityDto defaultDcap = new DeviceCapabilityDto();
        defaultDcap.setSelfDeviceLink("/sdev");
        defaultDcap.setEndDeviceListLink("/edev");
        defaultDcap.setMirrorUsagePointListLink("/mup");
        defaultDcap.setTimeLink("/tm");
        
        try {
            defaultDcap = deviceCapabilityRepository.save(defaultDcap);
            
            TimeDto time = new TimeDto();
            time.setTimeLink("/tm");
            time.setCurrentTime(String.valueOf(System.currentTimeMillis() / 1000));
            time.setQuality("7");
            timeDtoRepository.save(time);
            
            LOGGER.info("Created default device capability for polling");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating default device capability", e);
        }
        
        return defaultDcap;
    }
   
}












   

    
    


