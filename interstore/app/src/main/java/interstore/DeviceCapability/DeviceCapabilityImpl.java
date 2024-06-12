package interstore.DeviceCapability;

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
       
        DeviceCapabilityDto deviceCapabilityDto = new DeviceCapabilityDto();
        try {
            deviceCapabilityDto = deviceCapabilityRepository.save(deviceCapabilityDto);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DeviceCapabilityDto", e);
            throw e; 
        }
         
        setDeviceCapability(deviceCapabilityDto, jsonObject);
        return deviceCapabilityDto;
    }

    
           
   


    public void setDeviceCapability(DeviceCapabilityDto deviceCapabilityDto, JSONObject jsonObject) throws JSONException
    {  
        
        if(deviceCapabilityDto.getId() == null)
        {
            throw new IllegalStateException("DeviceCapabilityDto must be persisted before adding links.");  
        }
        
        
        Link link = new Link(); 
        String selfDevcieLink = jsonObject.getString("selfDeviceLink");  
        link.setLink(selfDevcieLink); 
        deviceCapabilityDto.addLink(link.getLink()); 
        
         ListLink listLink = new ListLink(); 
         String endDeviceListLink = jsonObject.getString("endDeviceListLink");
         listLink.setListLink(endDeviceListLink);
         deviceCapabilityDto.addLink(listLink.getListLink());
         String mirrorUsagePointListLink = jsonObject.getString("mirrorUsagePointListLink");
         listLink.setListLink(mirrorUsagePointListLink );
         deviceCapabilityDto.addLink(listLink.getListLink());

        String timeLink = jsonObject.getString("timeLink");
        link.setLink(timeLink);
        deviceCapabilityDto.addLink(link.getLink());
        deviceCapabilityDto.setTimeLink(link.getLink());

        TimeDto time = new TimeDto();
        time.setTimeLink(link.getLink());
        timeDtoRepository.save(time);
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
    public String getTime(String payload){
        TimeDto timeDto = timeDtoRepository.findByTimeLink(payload);
        JSONObject object = new JSONObject();
        object.put("time_instance", timeDto.getCurrentTime());
        object.put("quality", timeDto.getQuality());
        return object.toString();
    }
   
}





/*
 *  public List<String> getDefaultDeviceCapabilityListLink()
    { 
       List<String> defaultEndPointsList = new ArrayList<>();
       defaultEndPointsList.add("/edev");
       defaultEndPointsList.add("/mup");
       return defaultEndPointsList; 
 
    }
   
    public List<String> getDefaultCapabilityLink()
    {
        List<String> defaultEndPointsLink = new ArrayList<>();
        
        defaultEndPointsLink.add("/sdev");
        return defaultEndPointsLink;

    }
 * 
 * 
 *  for (String defaultendPointLink:  getDefaultCapabilityLink()) {
             link.setLink(defaultendPointLink);  
             deviceCapabilityDto.addLink(link.getLink());  
        }
       
         ListLink listLink = new ListLink(); 
        for(String defaultendPointListLink: getDefaultDeviceCapabilityListLink()) {
            listLink.setListLink(defaultendPointListLink);
            LOGGER.log(Level.INFO, "ListLink from device capability Implimentation: " + listLink.getListLink());
            deviceCapabilityDto.addLink(listLink.getListLink());
        } 
 * 
 * 
 */










   

    
    


