package interstore.FunctionSetAssignments;
import interstore.EndDevice.EndDeviceEntity;
import interstore.EndDevice.EndDeviceRepository;
import interstore.Types.HexBinary128;
import interstore.Types.mRIDType;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FunctionSetAssignmentsService {

    @Autowired
    FunctionSetAssignmentsRepository functionSetAssignmentsRepository;
    
    @Autowired
    private EndDeviceRepository endDeviceRepository;

    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsService.class.getName());

     
     @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId"));
        EndDeviceEntity endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        //endDevice.getFunctionSetAssignmentsListLink();
        String fsaLink = endDevice.getFunctionSetAssignmentsListLink();
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity(); 
        fsaEntity.setEndDevice(endDevice); 
        try {
            fsaEntity  = functionSetAssignmentsRepository.save(fsaEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving FSA entity", e);
        }
        setFunctionSetAssignmentEntity(fsaEntity  , payload,  fsaLink );
        fsaEntity  = functionSetAssignmentsRepository.save(fsaEntity);
        return fsaEntity;   
    }

    
    public void setFunctionSetAssignmentEntity(FunctionSetAssignmentsEntity fsaEntity , JSONObject payload, String functionsetassignmentLink) throws NotFoundException
      {    
        Long fsaId = fsaEntity.getId();
        JSONObject Fsapayload = payload.optJSONObject("payload");
        String idString = "/"+ String.valueOf(fsaId) ;
        String fsaLink = functionsetassignmentLink + idString;   
        String subScribable = Fsapayload.optString("subscribable");
        short shortSubscribable = Short.parseShort(subScribable);
        String mRID = Fsapayload.optString("mRID");
        String mRIDString = HexBinary128.validateAndFormatHexValue(mRID);
        mRIDType mRIDValue = new mRIDType(mRIDString);
        String version = Fsapayload.optString("version");
        int intVersion = Integer.parseInt(version);
        String description = Fsapayload.optString("description");
        fsaEntity.setSubscribable(shortSubscribable);
        fsaEntity.setmRID(mRIDValue.toString());
        fsaEntity.setVersion(intVersion );
        fsaEntity.setDescription(description);
        fsaEntity.setFunctionSetAssignmentsLink(fsaLink);
        findListLink(Fsapayload, fsaEntity);
        AddSubscribabaleFsa(shortSubscribable, fsaEntity);
        Fsapayload.clear();
      }
     
    public  void findListLink(JSONObject payload, FunctionSetAssignmentsEntity fsaEntity)
    {   
        try {
            Iterator<String> keys  = payload.keys();
            while(keys.hasNext())
            {
                String key = keys.next();
                String value = payload.optString(key);
                if(!value.isEmpty())
                {
                    switch(key) {
                        case "demandResponseProgramListLink":
                        fsaEntity.setDemandResponseProgramListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "fileListLink":
                        fsaEntity.setFileListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "traiffProfileListLink":
                        fsaEntity.setTariffProfileListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "messagingProgramListLink":
                        fsaEntity.setMessagingProgramListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "usagePointListLink":
                        fsaEntity.setUsagePointListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "dERProgramListLink":
                        fsaEntity.setDERProgramListLink( payload.optString(key));
                        LOGGER.info(fsaEntity.getDERProgramListLink().toString()); 
                        break;
                        case "customerAccountListLink":
                        fsaEntity.setCustomerAccountListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "prepaymentListLink":
                        fsaEntity.setPrepaymentListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                        break;
                        case "responseSetListLink":
                        fsaEntity.setResponseSetListLink(stripHost(fsaEntity.getFunctionSetAssignmentsLink()) + "/" + payload.optString(key));
                         default:
                            break;    
            }
                }
              
        }
    }
         catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
        }
       
    
}

private String stripHost(String url) {
        if (url == null) return null;
    try {
        java.net.URI uri = new java.net.URI(url);
        String path = uri.getPath(); // "/derp" or "/2030.5/dcap/tm"
        if (path == null || path.isEmpty()) return "/";

        // If you want to specifically remove "/2030.5" prefix
        String prefix = "/2030.5";
        if (path.startsWith(prefix)) {
            path = path.substring(prefix.length());
            if (path.isEmpty()) path = "/"; // ensure at least "/"
        }
        return path;
    } catch (Exception e) {
        // fallback: naive parsing
        int idx = url.indexOf("://");
        if (idx != -1) {
            String remainder = url.substring(idx + 3); // skip "http://"
            int slashIdx = remainder.indexOf("/");
            if (slashIdx != -1) {
                return remainder.substring(slashIdx); // return path after host
            } else {
                return "/"; // no path, return root
            }
        }
        return url; // unknown format
    }
    }

public String getAllFunctionsetAssignmentsHttp(Long endDeviceId){
    try {
    List<FunctionSetAssignmentsEntity> fsaEntityList =
            functionSetAssignmentsRepository.findByEndDeviceId(endDeviceId);

    if (fsaEntityList.isEmpty()) {
        String emptyXml =
            "<FunctionSetAssignmentsList xmlns=\"http://ieee.org/2030.5\" " +
            "all=\"0\" href=\"/edev/" + endDeviceId + "/fsa\" results=\"0\" subscribable=\"0\">" +
            "<message>No FunctionSetAssignments found</message>" +
            "</FunctionSetAssignmentsList>";

        return emptyXml;
    }

    StringBuilder xml = new StringBuilder();
    xml.append("<FunctionSetAssignmentsList xmlns=\"http://ieee.org/2030.5\" ")
       .append("all=\"").append(fsaEntityList.size()).append("\" ")
       .append("href=\"").append(stripHost(endDeviceRepository.findById(endDeviceId).get().getFunctionSetAssignmentsListLink())).append("\" ")
       .append("results=\"").append(fsaEntityList.size()).append("\" ")
       .append("subscribable=\"0\">\n");

    for (FunctionSetAssignmentsEntity fsa : fsaEntityList) {
        xml.append("  <FunctionSetAssignments href=\"")
           .append(stripHost(fsa.getFunctionSetAssignmentsLink())).append("\" subscribable=\"").append(fsa.getSubscribable() != null ? fsa.getSubscribable(): 0).append("\">\n");

        // Map of fields to XML tag names
        Map<String, String> listLinks = Map.of(
            "getDemandResponseProgramListLink", "DemandResponseProgramListLink",
            "getMessagingProgramListLink", "MessagingProgramListLink",
            "getFileListLink", "FileListLink",
            "getTariffProfileListLink", "TariffProfileListLink",
            "getUsagePointListLink", "UsagePointListLink",
            "getDERProgramListLink", "DERProgramListLink",
            "getCustomerAccountListLink", "CustomerAccountListLink",
            "getPrepaymentListLink", "PrepaymentListLink",
            "getResponseSetListLink", "ResponseSetListLink"
        );

        for (Map.Entry<String, String> entry : listLinks.entrySet()) {
            Method method = FunctionSetAssignmentsEntity.class.getMethod(entry.getKey());
            Object value = method.invoke(fsa);

            if (value != null) {
                xml.append("    <").append(entry.getValue()).append(" href=\"")
                   .append(stripHost(value.toString())).append("\" all=\"0\"/>\n");
            }
        }

        // Core attributes
        xml.append("    <mRID>").append(fsa.getmRID() != null ? fsa.getmRID() : "N/A").append("</mRID>\n");
        xml.append("    <description>")
           .append(fsa.getDescription() != null ? fsa.getDescription() : "No description")
           .append("</description>\n");

        xml.append("  </FunctionSetAssignments>\n");
    }

    xml.append("</FunctionSetAssignmentsList>");

    return xml.toString();

} catch (Exception e) {
    LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);
    return "<FunctionSetAssignmentsList xmlns=\"http://ieee.org/2030.5\" all=\"0\" href=\"/edev/" + endDeviceId + "/fsa\" results=\"0\" subscribable=\"0\">\n" +
           "<error>Some error occurred</error>\n" +
           "</FunctionSetAssignmentsList>";
}
}
   
public ResponseEntity<Map<String, Object>> getAllFunctionsetAssignments(Long endDeviceId) {
    try {
        Map<String, Object> responseMap = new HashMap<>();
        List<FunctionSetAssignmentsEntity> fsaEntityList = functionSetAssignmentsRepository.findByEndDeviceId(endDeviceId);

        List<Map<String, Object>> fsaDetails = fsaEntityList.stream()
            .map(fsaEntity -> {
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("id", fsaEntity.getId());
                entityMap.put("mRID", fsaEntity.getmRID() != null ? fsaEntity.getmRID() : "N/A");
                entityMap.put("description", fsaEntity.getDescription() != null ? fsaEntity.getDescription() : "No description");
                entityMap.put("subscribable", fsaEntity.getSubscribable());
                entityMap.put("version", fsaEntity.getVersion());
                if (fsaEntity.getDemandResponseProgramListLink() != null) {
                    entityMap.put("demandResponseProgramListLink", fsaEntity.getDemandResponseProgramListLink());
                }
                if (fsaEntity.getFileListLink() != null) {
                    entityMap.put("fileListLink", fsaEntity.getFileListLink());
                }
                if (fsaEntity.getTariffProfileListLink() != null) {
                    entityMap.put("tariffProfileListLink", fsaEntity.getTariffProfileListLink());
                }
                if (fsaEntity.getMessagingProgramListLink() != null) {
                    entityMap.put("messagingProgramListLink", fsaEntity.getMessagingProgramListLink());
                }
                if (fsaEntity.getUsagePointListLink() != null) {
                    entityMap.put("usagePointListLink", fsaEntity.getUsagePointListLink());
                }
                if (fsaEntity.getDERProgramListLink() != null) {
                    entityMap.put("dERProgramListLink", fsaEntity.getDERProgramListLink());
                }
                if (fsaEntity.getCustomerAccountListLink() != null) {
                    entityMap.put("customerAccountListLink", fsaEntity.getCustomerAccountListLink());
                }
                if (fsaEntity.getPrepaymentListLink() != null) {
                    entityMap.put("prepaymentListLink", fsaEntity.getPrepaymentListLink());
                }
                if (fsaEntity.getResponseSetListLink() != null) {
                    entityMap.put("responseSetListLink", fsaEntity.getResponseSetListLink());
                }
                if (fsaEntity.getFunctionSetAssignmentsLink() != null) {
                    entityMap.put("functionSetAssignmentsLink", fsaEntity.getFunctionSetAssignmentsLink());
                }

                return entityMap;
            })
            .collect(Collectors.toList());

        if (fsaDetails.isEmpty()) {
            responseMap.put("message", "No functionSetAssignments found.");
        } else {
            responseMap.put("functionSetAssignments", fsaDetails);
        }

        return ResponseEntity.ok(responseMap);
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);
        return ResponseEntity.status(404).body(null);
    }
}

    public void AddSubscribabaleFsa(Short shortSubscribable,FunctionSetAssignmentsEntity fsaEntity )
        {
          if(shortSubscribable!= 0)
          {
            fsaEntity.setFSASubscribableList(fsaEntity);
          }
        }

     /*get a single function set assignments for an end device  */
    @SuppressWarnings("unused")
    public ResponseEntity<Map<String, Object>> getFunctionsetAssignments(Long endDeviceId, Long fsaId)

    {
        try {
            Map<String, Object> result = new HashMap<>();
           Optional <FunctionSetAssignmentsEntity> fsaEntityOptional = functionSetAssignmentsRepository.findFirstByEndDeviceIdAndId( endDeviceId , fsaId );
           FunctionSetAssignmentsEntity fsaEntity = fsaEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", fsaEntity.getId());
            entityMap.put("mRID", fsaEntity.getmRID() != null ? fsaEntity.getmRID() : "N/A");
            entityMap.put("description", fsaEntity.getDescription() != null ? fsaEntity.getDescription() : "No description");
            entityMap.put("subscribable", fsaEntity.getSubscribable());
            entityMap.put("version", fsaEntity.getVersion());

            if (fsaEntity.getDemandResponseProgramListLink() != null) {
                entityMap.put("demandResponseProgramListLink", fsaEntity.getDemandResponseProgramListLink());
            }
            if (fsaEntity.getFileListLink() != null) {
                entityMap.put("fileListLink", fsaEntity.getFileListLink());
            }
            if (fsaEntity.getTariffProfileListLink() != null) {
                entityMap.put("tariffProfileListLink", fsaEntity.getTariffProfileListLink());
            }
            if (fsaEntity.getMessagingProgramListLink() != null) {
                entityMap.put("messagingProgramListLink", fsaEntity.getMessagingProgramListLink());
            }
            if (fsaEntity.getUsagePointListLink() != null) {
                entityMap.put("usagePointListLink", fsaEntity.getUsagePointListLink());
            }
            if (fsaEntity.getDERProgramListLink() != null) {
                entityMap.put("dERProgramListLink", fsaEntity.getDERProgramListLink());
            }
            if (fsaEntity.getCustomerAccountListLink() != null) {
                entityMap.put("customerAccountListLink", fsaEntity.getCustomerAccountListLink());
            }
            if (fsaEntity.getPrepaymentListLink() != null) {
                entityMap.put("prepaymentListLink", fsaEntity.getPrepaymentListLink());
            }
            if (fsaEntity.getResponseSetListLink() != null) {
                entityMap.put("responseSetListLink", fsaEntity.getResponseSetListLink());
            }
            if (fsaEntity.getFunctionSetAssignmentsLink() != null) {
                entityMap.put("functionSetAssignmentsLink", fsaEntity.getFunctionSetAssignmentsLink());
            }


            if (fsaEntity == null) {
                result.put("message", "No functionsetassignment found for EndDevice ID " +  endDeviceId  + " and FSA ID " +   fsaId );
            } else {
                result.put("FunctionSetAssignments", entityMap);
            }
            return ResponseEntity.ok( result );
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevice", e);
            return ResponseEntity.status(404).body(null);
        }

       
       
    }

    public String getFunctionsetAssignmentsHttp(Long endDeviceId, Long fsaId) {
        try {
            Optional<FunctionSetAssignmentsEntity> fsaEntityOptional = functionSetAssignmentsRepository.findFirstByEndDeviceIdAndId(endDeviceId, fsaId);

            if (fsaEntityOptional.isEmpty()) {
                return "<FunctionSetAssignments xmlns=\"http://ieee.org/2030.5\" " +
                       "href=\"" + stripHost(endDeviceRepository.findById(endDeviceId).get().getFunctionSetAssignmentsListLink()) + "/" + fsaId + "\" " +
                       "subscribable=\"0\">\n" +
                       "<message>No FunctionSetAssignments found for EndDevice ID " + endDeviceId + " and FSA ID " + fsaId + "</message>\n" +
                       "</FunctionSetAssignments>";
            }

            FunctionSetAssignmentsEntity fsa = fsaEntityOptional.get();
            StringBuilder xml = new StringBuilder();
            xml.append("<FunctionSetAssignments xmlns=\"http://ieee.org/2030.5\" ")
               .append("href=\"").append(stripHost(fsa.getFunctionSetAssignmentsLink())).append("\" subscribable=\""+ fsa.getSubscribable() + "\">\n");

            // Map of fields to XML tag names
            Map<String, String> listLinks = Map.of(
                "getDemandResponseProgramListLink", "DemandResponseProgramListLink",
                "getMessagingProgramListLink", "MessagingProgramListLink",
                "getFileListLink", "FileListLink",
                "getTariffProfileListLink", "TariffProfileListLink",
                "getUsagePointListLink", "UsagePointListLink",
                "getDERProgramListLink", "DERProgramListLink",
                "getCustomerAccountListLink", "CustomerAccountListLink",
                "getPrepaymentListLink", "PrepaymentListLink",
                "getResponseSetListLink", "ResponseSetListLink"
            );

            for (Map.Entry<String, String> entry : listLinks.entrySet()) {
                try {
                    Method method = FunctionSetAssignmentsEntity.class.getMethod(entry.getKey());
                    Object value = method.invoke(fsa);
                    if (value != null) {
                        xml.append(" <").append(entry.getValue()).append(" href=\"")
                           .append(stripHost(value.toString())).append("\" all=\"0\"/>\n");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                    LOGGER.log(Level.WARNING, "Error processing method " + entry.getKey(), e);
                }
            }

            // Core attributes
            xml.append(" <mRID>").append(fsa.getmRID() != null ? fsa.getmRID() : "N/A").append("</mRID>\n");
            xml.append(" <description>")
               .append(fsa.getDescription() != null ? fsa.getDescription() : "No description")
               .append("</description>\n");
               
            // xml.append(" <").append("TimeLink").append(" href=\"")
            //                .append("/tm").append("\"/>\n");
            xml.append("</FunctionSetAssignments>\n");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving FunctionSetAssignment", e);
            return "<FunctionSetAssignments xmlns=\"http://ieee.org/2030.5\" " +
                   "href=\"" + stripHost(endDeviceRepository.findById(endDeviceId).get().getFunctionSetAssignmentsListLink()) + "/" + fsaId + "\" " +
                   "subscribable=\"0\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</FunctionSetAssignments>";
        }
    }


}


