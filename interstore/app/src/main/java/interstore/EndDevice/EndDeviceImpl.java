package interstore.EndDevice;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.ApplicationContextProvider;
import interstore.DER.*;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsList;
//import interstore.FunctionSetAssignments.FunctionSetAssignmentsListRepository;
import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Registration.RegistrationDto;
import interstore.Registration.RegistrationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
@SuppressWarnings("unused")
@Service 
public class EndDeviceImpl {


    @Autowired
    private EndDeviceRepository endDeviceRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private DERRepository derRepository;

    @Autowired
    private DERListRepository derListRepository;

    @Autowired
    //private FunctionSetAssignmentsListRepository functionSetAssignmentsListRepository;

    private static final Logger LOGGER = Logger.getLogger(EndDeviceImpl.class.getName());

    @Transactional 
    public EndDeviceDto createEndDevice(JSONObject  payload) {
        EndDeviceDto endDeviceDto = new EndDeviceDto(); 
        endDeviceDto = endDeviceRepository.save(endDeviceDto);
        LOGGER.log(Level.INFO, "EndDeviceDto saved successfully" + endDeviceDto);
        setEndDevice(endDeviceDto, payload);   
        return endDeviceDto; 
    }


    public void setEndDevice(EndDeviceDto endDeviceDto, JSONObject payloadWrapper)
    {   
        Long id = endDeviceDto.getId(); 
        String idString = "/"+ String.valueOf(id) + "/";
        JSONObject payload = payloadWrapper.optJSONObject("payload");
        setEndDeviceAttributesEndPoints(payload);
        String endDeviceListLink =  payload.optString("endDeviceListLink", "defaultLink") ;
        String endDeviceLink =  endDeviceListLink +  idString;
        String functionsetAssignmentListLink =   endDeviceListLink + idString + payload.optString("functionsetAssignmentLink", "defaultLink");
        String derListLink = endDeviceListLink +  idString + payload.optString("derListLink", "defaultLink");
        String deviceStatusLink =  endDeviceListLink  + idString + payload.optString("deviceStatusLink", "defaultLink");
        String registrationLink = endDeviceListLink  + idString + payload.optString("registrationLink", "defaultLink");
        String subscriptionLink =  endDeviceListLink + idString + payload.optString("subscriptionLink", "defaultLink");
        String deviceCategory =  payload.optString("deviceCategory", "defaultDeviceCategory");
        String sfdiString = payload.optString("sfdi", null);
        Long sfdi = null; 
        sfdiString = sfdiString.replaceAll("\\D", ""); 
        try{
            
            sfdi = Long.parseLong(sfdiString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Falied to parse sfdi to Long " +  sfdiString); 
        } 
       
        String lfdi = payload.optString("lfdi", "defaultLFDI");
        endDeviceDto.setDeviceCategory(deviceCategory);
        endDeviceDto.setsfdi(sfdi);
        endDeviceDto.sethexBinary160(lfdi);
        Link link = new Link(); 
        link.setLink(endDeviceLink);
        endDeviceDto.setEndDeviceLink(link.getLink());
        link.setLink(deviceStatusLink);
        endDeviceDto.setDeviceStatusLink(link.getLink());
        link.setLink(registrationLink);
        endDeviceDto.setRegistrationLink(link.getLink());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("payload",getDERPayload(link.getLink()));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String postPayload = objectMapper.writeValueAsString(attributes);
            JSONObject jsonPayloadWrapper = new JSONObject(postPayload);
            DERList derList = new DERList(link.getLink());
            derList = derListRepository.save(derList);
            generateDER(jsonPayloadWrapper, derList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListLink listLink = new ListLink();
        listLink.setListLink(functionsetAssignmentListLink);
        endDeviceDto.setFunctionSetAssignmentsListLink(listLink.getListLink());
        try {
          //  FunctionSetAssignmentsList fsaList = new FunctionSetAssignmentsList(listLink.getListLink());
           // fsaList = functionSetAssignmentsListRepository.save(fsaList);
           // generateFSA(fsaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listLink.setListLink(derListLink);
        endDeviceDto.setDERListLink(listLink.getListLink());
        listLink.setListLink(subscriptionLink);
        endDeviceDto.setSubscriptionListLink(listLink.getListLink());


    }
    

    public void setEndDeviceAttributesEndPoints(JSONObject payload)
    {
        String endDeviceListLink =  payload.optString("endDeviceListLink", "defaultLink") ;
        if(endDeviceListLink .startsWith("/"))
        {
            endDeviceListLink = endDeviceListLink.substring(1);
            EdevManager.SetEndDeviceEndpoint(endDeviceListLink);
        }
        String registrationLink = payload.optString("registrationLink", "defaultLink");
        if(registrationLink.startsWith("/"))
        {
            registrationLink = registrationLink.substring(1);
            EdevManager.setregistrationEndpoint( registrationLink );
        }

    }
    public void generateDER(JSONObject jsonPayloadWrapper, DERList derList){
        for(int i = 0; i < 3; i++) {
            DERDto derDto = new DERDto(derList);
            derDto = derRepository.save(derDto);
            derList.addDerDto(derDto);
            DERImpl derImpl = ApplicationContextProvider.getApplicationContext().getBean(DERImpl.class);
            derImpl.setDER(derDto, jsonPayloadWrapper);
        }
    }

    public Map<String, String> getDERPayload(String derListLink){
        Map<String, String> payload = new HashMap<>();
        payload.put("DERListLink", derListLink);
        payload.put("DERCapabilityLink", "/dercap");
        payload.put("DERStatusLink", "/ders");
        payload.put("DERAvailabilityLink", "/dera");
        payload.put("DERSettingsLink", "/derg");
        return payload;
    }




     /* this is not database id , this is the id of the device it'self like SFDI, LFDI */
     public Map<String, Object> getEndDeviceID(EndDeviceDto endDeviceDto)
     {
        if (endDeviceDto == null || endDeviceDto.getId() == null) {
            throw new IllegalArgumentException("DeviceCapabilityDto is null or does not have a valid ID.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("SFDI", endDeviceDto.getsfdi());  
        result.put("LFDI", endDeviceDto.gethexBinary160());
        LOGGER.log(Level.INFO, "the result from getEndDeviceID: " + result.toString());
        return result;
    }

    public ResponseEntity<Map<String, Object>> getEndDevice(Long id)
    {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            EndDeviceDto endDeviceDto = endDeviceRepository.findById(id).orElse(null);
            LOGGER.log(Level.INFO, "EndDeviceDto retrieved successfully" + endDeviceDto);
            if(endDeviceDto == null) {
                responseMap.put("message", "No endDevice found.");
            }
            else {
                responseMap.put("endDevice", endDeviceDto);
            }
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
            
            return ResponseEntity.status(404).body(null);
        }

    } 
    /*get all end devices has two outcomes one is no end devices found
     * and other one is end devices found, among the end devcies found
     * the end devices found shall response the list of the end devices
     * those are only with the uri , the list of uri of the end devices .
     * the list of EndDevices are {"endDevices":[{"id":1,"deviceCategory":"0","hexBinary160":"3E4F45","endDeviceLink":"http://localhost/edev/1","deviceStatusLink":"http://localhost/edev/1/dstat",
     * "registrationLink":"http://localhost/edev/1/rg","functionSetAssignmentsListLink":"http://localhost/edev/1/fsa","derlistLink":"http://localhost/edev/1","subscriptionListLink":"http://localhost/edev/1/sub","sfdi":16726121139},
     * {"id":2,"deviceCategory":"1","hexBinary160":"3E4F46","endDeviceLink":"http://localhost/edev/2","deviceStatusLink":"http://localhost/edev/2/dstat","registrationLink":"http://localhost/edev/2/rg",
     * "functionSetAssignmentsListLink":"http://localhost/edev/2/fsa","derlistLink":"http://localhost/edev/2","subscriptionListLink":"http://localhost/edev/2/sub","sfdi":16726121111}]}
     *
     */
    @SuppressWarnings("unlikely-arg-type")
    public ResponseEntity<Map<String, Object>> getAllEndDevices() {
        Map<String, Object> responseMap = new HashMap<>();
        try {
           
            List<EndDeviceDto> endDeviceDtos  = endDeviceRepository.findAll();
            if(endDeviceDtos.isEmpty()) {
                responseMap.put("message", "No endDevices found.");
            }
            else {
                responseMap.put("endDevices", endDeviceDtos);

            }
            
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDtos", e);
            return ResponseEntity.status(404).body(null);
        }
    }
    
    public EndDeviceDto findEndDeviceById(Long id) {
        LOGGER.log(Level.INFO, "Finding EndDeviceDto for ID: " + id);
        try {
            EndDeviceDto endDeviceDto = endDeviceRepository.findById(id).orElse(null);
            LOGGER.log(Level.INFO, "EndDeviceDto retrieved: " + endDeviceDto);
            return endDeviceDto;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto for ID: " + id, e);
            return null;
        }
    }


    /*
     */
    @Transactional
    public Map<String, Object> registerEndDevice(Long registrationPinLong, Long endDeviceID)
    {


        EndDeviceDto endDeviceDto = this.findEndDeviceById(endDeviceID);
        String endDeviceRegistrationLink = endDeviceDto.getRegistrationLink();   // the registration link has to be present for cross check
        RegistrationDto registrationDto = new RegistrationDto();
        try {
            LOGGER.log(Level.INFO, "Registration Repository", registrationRepository);
            registrationDto = registrationRepository.save(registrationDto);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save RegistrationDto", e);
            throw e;
        }
        registrationDto.setPin(registrationPinLong);
        registrationDto.setEndDevice(endDeviceDto);
        endDeviceRegistrationLink = endDeviceRegistrationLink + "/" + registrationDto.getId();
        registrationDto.setLinkRgid( endDeviceRegistrationLink);
        return getEndDeviceRegistrationID(registrationDto);
    }



    public Map<String, Object> getEndDeviceRegistrationID(RegistrationDto registrationDto)
    {
       if (registrationDto == null || registrationDto.getId() == null) {
           throw new IllegalArgumentException("DeviceCapabilityDto is null or does not have a valid ID.");
       }
       Map<String, Object> result = new HashMap<>();
       result.put("Pin", registrationDto.getPin());
       result.put("id", registrationDto.getId());
       result.put("registrationLinkid", registrationDto.getLinkRgid());
       return result;
   }


   /* watch out for the null because if anyof the attributes are null it throws an error
    * this method is intended to get all registerd ende devices under an end devcie
    for xample an end device with an id 1 can have one or many registeration many registration is
    subjective atlease one registration , usually the enddevice is registered only once.
   */
   public ResponseEntity<Map<String, Object>> getAllRegisteredEndDevice(Long endDeviceID )
   {
        try {
            List<RegistrationDto> registrationDtos  = registrationRepository.findByEndDeviceId(endDeviceID);
            LOGGER.log(Level.INFO, "RegisteredEndDevices retrieved successfully" + registrationDtos);
            List<Map<String, Object>> registrationDetails = registrationDtos.stream()
            .map(reg -> Map.<String, Object>of(
              "id", Optional.ofNullable(reg.getId()).orElse(null),
              "pin", Optional.ofNullable(reg.getPin()).orElse(null),
              "dateTimeRegistered",  Optional.ofNullable(reg.getDateTimeRegistered()).orElse("Default DateTime"), // not implimented
              "registrationLinkid", Optional.ofNullable(reg.getLinkRgid()).orElse(null)
            ))
            .collect(Collectors.<Map<String, Object>>toList());
        LOGGER.log(Level.INFO, "RegisteredEndDevices retrieved successfully" + registrationDetails);
            Map<String, Object> result = new HashMap<>();
        if (registrationDetails.isEmpty()) {
            result.put("message", "No RegisteredEndDevices found for EndDevice ID " + endDeviceID);
        } else {
            result.put("RegisteredEndDevices", registrationDetails);
        }
        LOGGER.log(Level.INFO, "the result from getAllRegisteredEndDevice: " + result.toString());
        return ResponseEntity.ok(result);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevices", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    /* this is the case where the end device is registerd once , the details of the registration is
     * the reponse of this mehtod
     */
    public ResponseEntity<Map<String, Object>>getRegisterdEndDeviceDetails(Long endDeviceID, Long registrationID)
    {
        
        try {
            Optional<RegistrationDto> registrationDto  = registrationRepository.findFirstByEndDeviceIdAndId( endDeviceID,  registrationID) ;  //findFirstByEndDeviceId(endDeviceID);
            Map<String, Object> result = new HashMap<>();
            if (registrationDto == null) {
                result.put("message", "No RegisteredEndDevice found for EndDevice ID " + endDeviceID + " and Registration ID " + registrationID);
            } else {
                result.put("RegisteredEndDevice", registrationDto.get());
            }
            return ResponseEntity.ok(result);
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevice", e);
            return ResponseEntity.status(404).body(null);
        }



}

    public EndDeviceDto getEndDeviceByRegistrationID(Long id){
        Optional<RegistrationDto> registrationDto = registrationRepository.findById(id);
        EndDeviceDto endDeviceDto = registrationDto.get().getEndDevice();
        return endDeviceDto;
    }

}




/*
ResponseEntity<Map<String, Object>> responseEntity =  this.getEndDevice(endDeviceID);
    Map<String, Object> responseMap = responseEntity.getBody();
    if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

##### 

ResponseEntity<Map<String, Object>> responseEntity =  this.getEndDevice(endDeviceID);
        Map<String, Object> responseMap = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }





 * public void generateFSA(FunctionSetAssignmentsList fsaList){
        for(int i = 0; i < 3; i++) {
            FunctionSetAssignmentsService fsaImpl = ApplicationContextProvider.getApplicationContext().getBean(FunctionSetAssignmentsService.class);
            fsaImpl.createFSA(fsaList);
        }
    }
 * 
 * 
 * 
 * 
 * 
 */











    
