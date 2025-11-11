package interstore.EndDevice;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.ApplicationContextProvider;
import interstore.DER.*;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Registration.RegistrationDto;
import interstore.Registration.RegistrationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
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
    private DerRepository derRepository;
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
        String derListLink = endDeviceListLink +  idString + payload.optString("dERListLink", "defaultLink");
        String deviceStatusLink =  endDeviceListLink  + idString + payload.optString("deviceStatusLink", "defaultLink");
        String registrationLink = endDeviceListLink  + idString + payload.optString("registrationLink", "defaultLink");
        String subscriptionLink =  endDeviceListLink + idString + payload.optString("subscriptionLink", "defaultLink");
        String deviceCategory =  payload.optString("deviceCategory", "defaultDeviceCategory");
        String configurationLink = endDeviceListLink + idString + "cfg";
        String powerStatusLink = endDeviceListLink + idString + "ps";
        String fileStatusLink = endDeviceListLink + idString + "fs";
        String deviceInformationLink = endDeviceListLink + idString + "di";
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
       
        ListLink listLink = new ListLink();
        listLink.setListLink(functionsetAssignmentListLink);
        endDeviceDto.setFunctionSetAssignmentsListLink(listLink.getListLink());
        listLink.setListLink(derListLink);
        endDeviceDto.setDERListLink(listLink.getListLink());
        listLink.setListLink(subscriptionLink);
        endDeviceDto.setSubscriptionListLink(listLink.getListLink());
        link.setLink(configurationLink);
        endDeviceDto.setConfigurationLink(link.getLink());
        link.setLink(powerStatusLink);
        endDeviceDto.setPowerStatusLink(link.getLink());
        link.setLink(fileStatusLink);
        endDeviceDto.setFileStatusLink(link.getLink());
        link.setLink(deviceInformationLink);
        endDeviceDto.setDeviceInformationLink(link.getLink());

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

    public String getEndDeviceListHttp(Integer limit) {
       
        try {
            List<EndDeviceDto> endDeviceList = endDeviceRepository.findAll();

            if (endDeviceList.isEmpty()) {
                String emptyXml = "<EndDeviceList xmlns=\"http://ieee.org/2030.5\" " +
                                  "all=\"0\" href=\"/edev\" results=\"0\" subscribable=\"0\">\n" +
                                  "<message>No EndDevices found</message>\n" +
                                  "</EndDeviceList>";
                return emptyXml;
            }

            int totalCount = endDeviceList.size();
            if (limit != null && limit > 0 && limit < totalCount) {
                endDeviceList = endDeviceList.subList(0, limit);
            }

            StringBuilder xml = new StringBuilder();
            xml.append("<EndDeviceList xmlns=\"http://ieee.org/2030.5\" ")
               .append("all=\"").append(totalCount).append("\" ")
               .append("href=\"/edev\" ")
               .append("results=\"").append(endDeviceList.size()).append("\" ")
               .append("subscribable=\"0\">\n");
            String[] elementOrder = {
                "configurationLink", "deviceInformationLink", "linkDstat", "linkDerList", "fileStatusLink", "powerStatusLink", "linkFsa", "linkRg", "linkSub"
            };
            String[] xmlNames = {
                "ConfigurationLink", "DeviceInformationLink", "DeviceStatusLink", "DERListLink","FileStatusLink", "PowerStatusLink",
                 "FunctionSetAssignmentsListLink", "RegistrationLink", "SubscriptionListLink"
            };

            for (EndDeviceDto endDeviceDto : endDeviceList) {
                xml.append(" <EndDevice href=\"")
                   .append(stripHost(endDeviceDto.getEndDeviceLink())).append("\" ")
                   .append("subscribable=\"0\">\n");

                for (int i = 0; i < elementOrder.length; i++) {
                    String fieldName = elementOrder[i];
                    String xmlName = xmlNames[i];

                    if (xmlName.equals("FunctionSetAssignmentsListLink")){
                        // Add sFDI value
                        if (endDeviceDto.getsfdi() != null) {
                            xml.append("<sFDI>").append(endDeviceDto.getsfdi()).append("</sFDI>\n");
                        }

                        xml.append("<changedTime>").append(System.currentTimeMillis() / 1000).append("</changedTime>\n");
                    }

                    try {
                        Field field = EndDeviceDto.class.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(endDeviceDto);

                        if (value != null && value instanceof String) {
                            xml.append("<").append(xmlName);
                            xml.append(" href=\"").append(stripHost((String) value)).append("\"");

                            // Add 'all' attribute for list links
                            if (xmlName.toLowerCase().contains("listlink")) {
                                if (xmlName.equals("DERListLink")) {
                                    // Query the actual count of DER resources for this EndDevice
                                    int derCount = derRepository.findByEndDeviceId(endDeviceDto.getId()).size();
                                    xml.append(" all=\"").append(derCount).append("\"");
                                } else {
                                    xml.append(" all=\"0\"");
                                }
                            }

                            xml.append("/>\n");
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        LOGGER.log(Level.WARNING, "Field not found or accessible: " + fieldName, e);
                    }
                }
                xml.append(" </EndDevice>\n");
            }
            xml.append("</EndDeviceList>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceList", e);
            return "<EndDeviceList xmlns=\"http://ieee.org/2030.5\" all=\"0\" href=\"/edev\" results=\"0\" subscribable=\"0\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</EndDeviceList>";
        }
    }
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
  
    private String stripHost(String url) {
        if (url == null) return null;
        try {
            String path = new java.net.URL(url).getPath(); 
            int index = path.indexOf("/2030.5");
            if (index != -1) {
                return path.substring(index + "/2030.5".length()); 
            }
            return path;
        } catch (Exception e) {
           
            int index = url.indexOf("/2030.5");
            if (index != -1) {
                return url.substring(index + "/2030.5".length());
            }
            return url;
        }
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
     
      public String getEndDeviceHttp(Long id)
    {
        try {
        EndDeviceDto endDeviceDto = endDeviceRepository.findById(id).orElse(null);
        if (endDeviceDto == null) {
            return "<EndDevice xmlns=\"http://ieee.org/2030.5\"><message>No endDevice found.</message></EndDevice>";
        }
        StringBuilder xml = new StringBuilder();
        xml.append("<EndDevice xmlns=\"http://ieee.org/2030.5\" href=\"")
           .append(stripHost(endDeviceDto.getEndDeviceLink()))
           .append("\" subscribable=\"0\">\n");
        // Fields in IEEE 2030.5 preferred order
        String[] elementOrder = {
            "configurationLink", "deviceInformationLink", "linkDstat", "linkDerList", "fileStatusLink", "powerStatusLink", "linkFsa", "linkRg", "linkSub"
        };
        String[] xmlNames = {
            "ConfigurationLink", "DeviceInformationLink", "DeviceStatusLink", "DERListLink", "FileStatusLink", "PowerStatusLink", "FunctionSetAssignmentsListLink", "RegistrationLink", "SubscriptionListLink"
        };
        for (int i = 0; i < elementOrder.length; i++) {
            String fieldName = elementOrder[i];
            String xmlName = xmlNames[i];
            if (xmlName.equals("FunctionSetAssignmentsListLink")){
                if (endDeviceDto.getsfdi() != null) {
                    xml.append("<sFDI>").append(endDeviceDto.getsfdi()).append("</sFDI>\n");
                }
                xml.append("<changedTime>").append(System.currentTimeMillis() / 1000).append("</changedTime>\n");

            }
            try {
                Field field = EndDeviceDto.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(endDeviceDto);
                if (value != null && value instanceof String) {
                    xml.append("<").append(xmlName);
                    xml.append(" href=\"").append(stripHost((String) value)).append("\"");
                    if (xmlName.toLowerCase().contains("listlink")) {
                        if (xmlName.equals("DERListLink")) {
                            // Query the actual count of DER resources for this EndDevice
                            int derCount = derRepository.findByEndDeviceId(id).size();
                            xml.append(" all=\"").append(derCount).append("\"");
                        } else {
                            xml.append(" all=\"0\"");
                        }
                    }
                    xml.append("/>\n");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.WARNING, "Field not found or accessible: " + fieldName, e);
            }
        }
        xml.append("</EndDevice>");
        return xml.toString();
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
        return "<EndDevice xmlns=\"http://ieee.org/2030.5\"><message>Error retrieving EndDevice</message></EndDevice>";
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

    @Transactional
    public Map<String, Object> registerEndDevice(Long registrationPinLong, Long endDeviceID)
    {

        List<RegistrationDto> registrationDtos = registrationRepository.findByEndDeviceId(endDeviceID);
        if (!registrationDtos.isEmpty()) {
            throw new IllegalStateException("EndDevice with ID " + endDeviceID + " is already registered.");
        }
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
        registrationDto.setDateTimeRegistered(String.valueOf(System.currentTimeMillis()));
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

    public String getRegisteredEndDevice(Long endDeviceID )
   {
        try {
        List<RegistrationDto> registrationDtos = registrationRepository.findByEndDeviceId(endDeviceID);

        if (registrationDtos.isEmpty()) {
            return "<Registration xmlns=\"http://ieee.org/2030.5\">" +
                   "<message>No RegisteredEndDevices found for EndDevice ID " + endDeviceID + "</message>" +
                   "</Registration>";
        }

        StringBuilder xml = new StringBuilder();
        
        for (RegistrationDto reg : registrationDtos) {
            xml.append("<Registration xmlns=\"http://ieee.org/2030.5\" href=\"")
               .append(stripHost(endDeviceRepository.findById(endDeviceID).get().getRegistrationLink()))
               .append("\">\n");

            // dateTimeRegistered (use actual value or default)
            Long dateTime = (reg.getDateTimeRegistered() != null)
                    ? Long.valueOf(reg.getDateTimeRegistered())
                    : System.currentTimeMillis() / 1000; // default to now in seconds
            xml.append("  <dateTimeRegistered>").append(dateTime).append("</dateTimeRegistered>\n");

            // PIN
            String pin = (reg.getPin() != null) 
                ? String.valueOf(reg.getPin()) 
                : "UnknownPIN";

            xml.append("  <pIN>").append(pin).append("</pIN>\n");

            xml.append("</Registration>\n");
        }

        return xml.toString();

        } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevices", e);
        return "<Registration xmlns=\"http://ieee.org/2030.5\">" +
               "<message>Error retrieving RegisteredEndDevices</message>" +
               "</Registration>";
        }
    }

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

    public String getFunctionSetAssignmentHttp(Long id) {
        try {
            // Mock FunctionSetAssignmentsList for EndDevice with given id
            StringBuilder xml = new StringBuilder();
            xml.append("<FunctionSetAssignmentsList xmlns=\"http://ieee.org/2030.5\" ")
               .append("all=\"1\" href=\"/edev/").append(id).append("/fsa\" ")
               .append("results=\"1\" subscribable=\"0\">\n");
            
            xml.append("  <FunctionSetAssignments href=\"/edev/").append(id).append("/fsa/1\">\n");
            xml.append("    <mRID>FSA_").append(id).append("_001</mRID>\n");
            xml.append("    <description>Function Set Assignment for EndDevice ").append(id).append("</description>\n");
            xml.append("    <version>1</version>\n");
            xml.append("  </FunctionSetAssignments>\n");
            
            xml.append("</FunctionSetAssignmentsList>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving FunctionSetAssignmentsList", e);
            return "<FunctionSetAssignmentsList xmlns=\"http://ieee.org/2030.5\" all=\"0\" href=\"/edev/" + id + "/fsa\" results=\"0\" subscribable=\"0\">\n" +
                   "<message>Error retrieving FunctionSetAssignments</message>\n" +
                   "</FunctionSetAssignmentsList>";
        }
    }

    public ResponseEntity<Map<String, Object>> getAllFunctionSetAssignment(Long id) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Mock response for FunctionSetAssignments
            Map<String, Object> fsa = new HashMap<>();
            fsa.put("id", 1L);
            fsa.put("mRID", "FSA_" + id + "_001");
            fsa.put("description", "Function Set Assignment for EndDevice " + id);
            fsa.put("version", 1);
            fsa.put("href", "/edev/" + id + "/fsa/1");
            
            responseMap.put("functionSetAssignments", List.of(fsa));
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving FunctionSetAssignments", e);
            responseMap.put("message", "Error retrieving FunctionSetAssignments");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

}



