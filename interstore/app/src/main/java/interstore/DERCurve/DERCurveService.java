package interstore.DERCurve;

import interstore.DERProgram.DERProgramEntity;
import interstore.DERProgram.DERProgramRepository;
import interstore.Identity.IdentifiedObjectEntity;
import interstore.Identity.IdentifiedObjectRepository;
import interstore.Types.DERCurveType;
import interstore.Types.DERUnitRefType;
import interstore.Types.HexBinary128;
import interstore.Types.PowerOfTenMultiplierType;
import interstore.Types.mRIDType;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DERCurveService {
    private static final Logger LOGGER = Logger.getLogger(DERCurveService.class.getName());

    @Autowired
    DERCurveRepository derCurveRepository;

    @Autowired
    DERProgramRepository derProgramRepository;

    @Autowired
    IdentifiedObjectRepository identifiedObjectRepository;

    @Autowired
    CurveDataRepository curveDataRepository;


    @Transactional
    public DERCurveEntity createDERCurve(JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
//        LOGGER.info("reached here in curve service class");
        DERCurveEntity derCurveEntity = new DERCurveEntity();
        Long derProgramId = Long.parseLong(payload.getJSONObject("payload").getString("derProgramId"));
        DERProgramEntity derProgram = derProgramRepository.findById(derProgramId)
                .orElseThrow(() -> new NotFoundException());
        derCurveEntity.setDerProgram(derProgram);
        String derCurveListLink = derProgram.getDERCurveListLink();
        try {
            derCurveEntity  = derCurveRepository.save(derCurveEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DERCurve entity", e);
        }

        if(setDERCurveEntity(derCurveEntity  , payload,  derCurveListLink )){
            derCurveEntity  = derCurveRepository.save(derCurveEntity);
//        LOGGER.info("reached here in curve service class ending");
            return derCurveEntity;
        }
        return null;
    }

    @Transactional
    public boolean setDERCurveEntity(DERCurveEntity derCurveEntity, JSONObject payload, String derCurveListLink) throws NumberFormatException, IllegalArgumentException, NotFoundException {
        Long derCurveEntityId = derCurveEntity.getId();
        JSONObject derCurvepayload = payload.optJSONObject("payload");
        String idString = "/"+ String.valueOf(derCurveEntityId) ;
        String derCurveLink = derCurveListLink + idString;
        String mRID = derCurvepayload.optString("mRID");
        String mRIDString = HexBinary128.validateAndFormatHexValue(mRID);
        mRIDType mRIDValue = new mRIDType(mRIDString);
        String version = derCurvepayload.optString("version");
        int intVersion = Integer.parseInt(version);
        String description = derCurvepayload.optString("description");
        derCurveEntity.setDerCurveLink(derCurveLink);
        if (DERCurveType.isValidDERCurveType((short) Integer.parseInt(derCurvepayload.optString("curveType")))){
            derCurveEntity.setCurveType(Integer.parseInt(derCurvepayload.optString("curveType")));
        }
        else {
            LOGGER.log(Level.SEVERE, "Incorrect DERCurveType Provided");
            throw new IllegalArgumentException("Invalid DERCurveType provided: " + derCurvepayload.optString("curveType"));
        }

        if (PowerOfTenMultiplierType.isValidPowerOfTenMultiplier(Integer.parseInt(derCurvepayload.optString("x_multiplier_type")))){
            derCurveEntity.setxMultiplier(Integer.parseInt(derCurvepayload.optString("x_multiplier_type")));
        }
        else {
            LOGGER.log(Level.SEVERE, "Incorrect PowerOfTenMultiplierType Provided");
            throw new IllegalArgumentException("Incorrect PowerOfTenMultiplierType Provided: " + derCurvepayload.optString("x_multiplier_type"));
        }

        if (PowerOfTenMultiplierType.isValidPowerOfTenMultiplier(Integer.parseInt(derCurvepayload.optString("y_multiplier_type")))){
            derCurveEntity.setyMultiplier(Integer.parseInt(derCurvepayload.optString("y_multiplier_type")));
        }
        else {
            LOGGER.log(Level.SEVERE, "Incorrect PowerOfTenMultiplierType Provided");
            throw new IllegalArgumentException("Incorrect PowerOfTenMultiplierType Provided: " + derCurvepayload.optString("y_multiplier_type"));
        }

        if (DERUnitRefType.isValidDERUnitRefType((short) Integer.parseInt(derCurvepayload.optString("y_ref_type")))){
            derCurveEntity.setyRefType(Integer.parseInt(derCurvepayload.optString("y_ref_type")));
        }
        else {
            LOGGER.log(Level.SEVERE, "Incorrect DERUnitRefType Provided");
            throw new IllegalArgumentException("Incorrect DERUnitRefType Provided: " + derCurvepayload.optString("y_ref_type"));
        }

        Instant instant = Instant.now();
        long currentTime =  instant.getEpochSecond();
        derCurveEntity.setCreationTime(String.valueOf(currentTime));
//        derCurveEntity.setCurveData();
        IdentifiedObjectEntity identifiedObjectEntity = new IdentifiedObjectEntity();
        identifiedObjectEntity.setmRID(mRIDValue.toString());
        identifiedObjectEntity.setDescription(description);
        identifiedObjectEntity.setVersion(intVersion);
        identifiedObjectRepository.save(identifiedObjectEntity);
        derCurveEntity.setIdentifiedObjectEntity(identifiedObjectEntity);

        int pairIndex = 1;
        boolean allDataSaved = true;
        while(derCurvepayload.has("x_value_" + pairIndex) && !Objects.equals(derCurvepayload.optString("x_value_" + pairIndex), "") && derCurvepayload.has("y_value_" + pairIndex) && !Objects.equals(derCurvepayload.optString("y_value_" + pairIndex), "")){
            LOGGER.info("Reached in while");
            // Validate x_value and y_value
            try {
                int xValue = Integer.parseInt(derCurvepayload.optString("x_value_" + pairIndex));
                int yValue = Integer.parseInt(derCurvepayload.optString("y_value_" + pairIndex));
                setCurveData(derCurveEntityId, xValue, yValue);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Invalid number format for x_value or y_value at index " + pairIndex, e);
                allDataSaved = false; // Mark as failed for this index
            }
//            setCurveData(derCurveEntityId,Integer.parseInt(derCurvepayload.optString("x_value_" + pairIndex)), Integer.parseInt(derCurvepayload.optString("y_value_" + pairIndex)));
            pairIndex++;
        }

        return allDataSaved;


    }

    @Transactional
    public ResponseEntity<Map<String, Object>> setCurveData(long id, int x_value, int y_value) throws NotFoundException{
        LOGGER.info("Reached in setCurveData");
        Map<String, Object> responseMap = new HashMap<>();
        DERCurveEntity derCurveEntity = derCurveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        LOGGER.info("Reached in setCurveData 2");
        CurveData curveData = new CurveData();
        LOGGER.info("Reached in setCurveData 3");
        curveData.setX_value(x_value);
        curveData.setY_value(y_value);
//        curveData.setDerCurveEntity(derCurveEntity);
        LOGGER.info("Reached in setCurveData 4");
        curveDataRepository.save(curveData);
        LOGGER.info("Reached in setCurveData 5");
        List<CurveData> curveList = derCurveEntity.getCurveData();
        LOGGER.info("Reached in setCurveData 6");
        if(curveList == null){
            curveList = new ArrayList<>();
        }
        curveList.add(curveData);
        derCurveEntity.setCurveData(curveList);
        derCurveRepository.save(derCurveEntity); // Save DERCurveEntity again to update the reference
        responseMap.put("curve_data_list", curveList);
        LOGGER.info("Leaving setCurveData");
        return ResponseEntity.ok(responseMap);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getAllDERCurves(Long derProgramId) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<DERCurveEntity> derCurveEntityList = derCurveRepository.findByDerProgramId(derProgramId);

            List<Map<String, Object>> derCurveDetails = derCurveEntityList.stream()
                    .map(derCurveEntity -> {
                        Map<String, Object> entityMap = new HashMap<>();
                        entityMap.put("id", derCurveEntity.getId());
                        entityMap.put("mRID", derCurveEntity.getIdentifiedObjectEntity().getmRID() != null ? derCurveEntity.getIdentifiedObjectEntity().getmRID() : "N/A");
                        entityMap.put("description", derCurveEntity.getIdentifiedObjectEntity().getDescription() != null ? derCurveEntity.getIdentifiedObjectEntity().getDescription() : "No description");
                        entityMap.put("version", derCurveEntity.getIdentifiedObjectEntity().getVersion());
                        entityMap.put("curveType", derCurveEntity.getCurveType());
                        entityMap.put("xMultiplier", derCurveEntity.getxMultiplier());
                        entityMap.put("yMultiplier", derCurveEntity.getyMultiplier());
                        entityMap.put("yRefType", derCurveEntity.getyRefType());
                        entityMap.put("derCurveLink", derCurveEntity.getDerCurveLink());
                        entityMap.put("curveData", derCurveEntity.getCurveData());

                        return entityMap;
                    })
                    .collect(Collectors.toList());

            if (derCurveDetails.isEmpty()) {
                responseMap.put("message", "No DERCurves found.");
            } else {
                responseMap.put("DERCurves", derCurveDetails);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCurves", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    public String getAllDERCurvesHttp(Long derProgramId) {
        try {
            List<DERCurveEntity> derCurveEntityList = derCurveRepository.findByDerProgramId(derProgramId);
            
            StringBuilder xml = new StringBuilder();
            xml.append("<DERCurveList xmlns=\"http://ieee.org/2030.5\" ")
               .append("href=\"").append(stripHost(derProgramRepository.findById(derProgramId).get().getDERCurveListLink())).append("\" ")
               .append("all=\"").append(derCurveEntityList.size()).append("\" ")
               .append("results=\"").append(derCurveEntityList.size()).append("\">\n");

            if (derCurveEntityList.isEmpty()) {
                xml.append(" <message>No DERCurves found for DERProgram ").append(derProgramId).append("</message>\n");
            } else {
                for (DERCurveEntity derCurve : derCurveEntityList) {
                    IdentifiedObjectEntity identifiedObject = derCurve.getIdentifiedObjectEntity();
                    xml.append(" <DERCurve href=\"").append(stripHost(derCurve.getDerCurveLink())).append("\">\n");

                    // Core attributes from IdentifiedObjectEntity
                    xml.append("  <mRID>")
                       .append(identifiedObject.getmRID() != null ? identifiedObject.getmRID() : "N/A")
                       .append("</mRID>\n");
                    xml.append("  <description>")
                       .append(identifiedObject.getDescription() != null ? identifiedObject.getDescription() : "No description")
                       .append("</description>\n");

                    // Creation time
                    xml.append("  <creationTime>")
                       .append(derCurve.getCreationTime() != null ? derCurve.getCreationTime() : "0")
                       .append("</creationTime>\n");

                    // CurveData elements
                    List<CurveData> curveDataList = derCurve.getCurveData();
                    if (curveDataList != null && !curveDataList.isEmpty()) {
                        for (CurveData data : curveDataList) {
                            xml.append("  <CurveData>\n");
                            xml.append("   <xvalue>").append(data.getX_value() != null ? data.getX_value() : "0").append("</xvalue>\n");
                            xml.append("   <yvalue>").append(data.getY_value() != null ? data.getY_value() : "0").append("</yvalue>\n");
                            xml.append("  </CurveData>\n");
                        }
                    }

                    // DERCurve specific attributes
                    xml.append("  <curveType>").append(derCurve.getCurveType() != null ? derCurve.getCurveType() : "0").append("</curveType>\n");
                    xml.append("  <xMultiplier>").append(derCurve.getxMultiplier() != null ? derCurve.getxMultiplier() : "0").append("</xMultiplier>\n");
                    xml.append("  <yMultiplier>").append(derCurve.getyMultiplier() != null ? derCurve.getyMultiplier() : "0").append("</yMultiplier>\n");
                    xml.append("  <yRefType>").append(derCurve.getyRefType() != null ? derCurve.getyRefType() : "0").append("</yRefType>\n");

                    xml.append(" </DERCurve>\n");
                }
            }

            xml.append("</DERCurveList>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCurves", e);
            return "<DERCurveList xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/sep2/A1/derp/" + derProgramId + "/dc\" all=\"0\" results=\"0\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</DERCurveList>";
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getDERCurve(Long derProgramId, Long derCurveId) {
//        LOGGER.info("Reached here in DerCurve Service Class");
        try {
            Map<String, Object> result = new HashMap<>();
            Optional<DERCurveEntity> derCurveEntityOptional = derCurveRepository.findFirstByDerProgramIdAndId(derProgramId, derCurveId);
            if (derCurveEntityOptional.isEmpty()) {
                result.put("message", "No DERCurve found for DERProgram " + derProgramId + " and DERCurve ID " + derCurveId);
                return ResponseEntity.ok(result);
            }

            DERCurveEntity derCurveEntity = derCurveEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derCurveEntity.getId());
            entityMap.put("mRID", derCurveEntity.getIdentifiedObjectEntity().getmRID() != null ? derCurveEntity.getIdentifiedObjectEntity().getmRID() : "N/A");
            entityMap.put("description", derCurveEntity.getIdentifiedObjectEntity().getDescription() != null ? derCurveEntity.getIdentifiedObjectEntity().getDescription() : "No description");
            entityMap.put("version", derCurveEntity.getIdentifiedObjectEntity().getVersion());
            entityMap.put("curveType", derCurveEntity.getCurveType());
            entityMap.put("xMultiplier", derCurveEntity.getxMultiplier());
            entityMap.put("yMultiplier", derCurveEntity.getyMultiplier());
            entityMap.put("yRefType", derCurveEntity.getyRefType());
            entityMap.put("derCurveLink", derCurveEntity.getDerCurveLink());
            entityMap.put("curveData", derCurveEntity.getCurveData());

            result.put("DERCurve", entityMap);
//            LOGGER.info("Leaving DERCurveService class");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCurve", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    public String getDERCurveHttp(Long derProgramId, Long derCurveId) {
        try {
            Optional<DERCurveEntity> derCurveEntityOptional = derCurveRepository.findFirstByDerProgramIdAndId(derProgramId, derCurveId);
            
            if (derCurveEntityOptional.isEmpty()) {
                return "<DERCurve xmlns=\"http://ieee.org/2030.5\" href=\"/derp/" + derProgramId + "/dc/" + derCurveId + "\">\n" +
                       "<message>No DERCurve found for DERProgram " + derProgramId + " and DERCurve ID " + derCurveId + "</message>\n" +
                       "</DERCurve>";
            }

            DERCurveEntity derCurve = derCurveEntityOptional.get();
            IdentifiedObjectEntity identifiedObject = derCurve.getIdentifiedObjectEntity();
            
            StringBuilder xml = new StringBuilder();
            xml.append("<DERCurve xmlns=\"http://ieee.org/2030.5\" ")
               .append("href=\"").append(stripHost(derCurve.getDerCurveLink())).append("\">\n");

            // Core attributes from IdentifiedObjectEntity
            xml.append(" <mRID>")
               .append(identifiedObject.getmRID() != null ? identifiedObject.getmRID() : "N/A")
               .append("</mRID>\n");
            xml.append(" <description>")
               .append(identifiedObject.getDescription() != null ? identifiedObject.getDescription() : "No description")
               .append("</description>\n");
            
            // Creation time (assuming it's in DERCurveEntity; adjust if in IdentifiedObjectEntity or unavailable)
            xml.append(" <creationTime>")
               .append(derCurve.getCreationTime() != null ? derCurve.getCreationTime() : "0")
               .append("</creationTime>\n");

            // CurveData elements
            List<CurveData> curveDataList = derCurve.getCurveData();
            if (curveDataList != null && !curveDataList.isEmpty()) {
                for (CurveData data : curveDataList) {
                    xml.append(" <CurveData>\n");
                    xml.append("  <xvalue>").append(data.getX_value()).append("</xvalue>\n");
                    xml.append("  <yvalue>").append(data.getY_value()).append("</yvalue>\n");
                    xml.append(" </CurveData>\n");
                }
            }

            // DERCurve specific attributes
            xml.append(" <curveType>").append(derCurve.getCurveType() != null ? derCurve.getCurveType() : "0").append("</curveType>\n");
            // xml.append(" <rampDecTms>").append(derCurve.getRampDecTms() != null ? derCurve.getRampDecTms() : "0").append("</rampDecTms>\n");
            // xml.append(" <rampIncTms>").append(derCurve.getRampIncTms() != null ? derCurve.getRampIncTms() : "0").append("</rampIncTms>\n");
            // xml.append(" <rampPT1Tms>").append(derCurve.getRampPT1Tms() != null ? derCurve.getRampPT1Tms() : "0").append("</rampPT1Tms>\n");
            xml.append(" <xMultiplier>").append(derCurve.getxMultiplier() != null ? derCurve.getxMultiplier() : "0").append("</xMultiplier>\n");
            xml.append(" <yMultiplier>").append(derCurve.getyMultiplier() != null ? derCurve.getyMultiplier() : "0").append("</yMultiplier>\n");
            xml.append(" <yRefType>").append(derCurve.getyRefType() != null ? derCurve.getyRefType() : "0").append("</yRefType>\n");

            xml.append("</DERCurve>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCurve", e);
            return "<DERCurve xmlns=\"http://ieee.org/2030.5\" href=\"/derp/" + derProgramId + "/dc/" + derCurveId + "\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</DERCurve>";
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
}
