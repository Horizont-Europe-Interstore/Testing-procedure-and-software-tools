package interstore.DERCurve;

import interstore.DERProgram.DERProgramEntity;
import interstore.FunctionSetAssignments.FsaManager;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class DERCurveManager {
    private static final Logger LOGGER = Logger.getLogger(DERCurveManager.class.getName());

    DERCurveService derCurveService;

    public DERCurveManager(DERCurveService derCurveService) {
        this.derCurveService = derCurveService;
    }

    public Object chooseMethod_basedOnAction(String payload) throws JSONException, NumberFormatException, ChangeSetPersister.NotFoundException {
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
                return addDERCurve(jsonObject);
            case "get":
                return getDERCurve(jsonObject);
            case "put":
                updateDERCurve(jsonObject);
                break;
            case "delete":
                deleteDERCurve(jsonObject);
                break;
        }
        return "Operation completed successfully";
    }

    public Map<String, Object> addDERCurve(JSONObject payload) throws NumberFormatException, JSONException, ChangeSetPersister.NotFoundException {

        if(payload.has("der_curve_data"))
        {
            return this.derCurveService.
                    setCurveData(Long.parseLong(payload.getJSONObject("der_curve_data").getString("der_program_id")),
                            Integer.parseInt(payload.getJSONObject("der_curve_data").getString("x_value")),
                            Integer.parseInt(payload.getJSONObject("der_curve_data").getString("y_value")))
                    .getBody();
        }
        LOGGER.info("the received payload in the DER Curve Manager class  is " +  payload);
        DERCurveEntity derCurveEntity = this.derCurveService.createDERCurve( payload);
        LOGGER.info("the response from DER Curve get ID is " +  derCurveEntity.getId());
        LOGGER.info("the response from DER Curve creationTime " +  derCurveEntity.getCreationTime());
        return Map.of("id", derCurveEntity.getId(), "creationTime", derCurveEntity.getCreationTime(), "curveType", derCurveEntity.getCurveType());

    }

    public Map<String, Object> getDERCurve(JSONObject payload) throws JSONException
    {   LOGGER.info("Response received in DERCurveManager: "+payload);
        if(payload.has("derpID") && payload.has("dercID"))
        {
            Long derpID = payload.getLong("derpID");
            Long dercID = payload.getLong("dercID");
            // ResponseEntity<Map<String, Object>> response = this.derCurveService.getDERCurve(derpID, dercID);
            return getDERCurve(derpID, dercID);
        }

        else if(payload.has("derpID"))
        {   Long derpID = payload.getLong("derpID");

            return getAllDERCurveDetails(derpID);
        }

        return null ;

//        return getAllDERCurveDetails(Long.parseLong(payload.getJSONObject("payload").getString("der_program_id")));
    }

    @GetMapping("/edev/{edevID}/fsa/{fsaID}/derp/{derpId}/dc")
    public Map<String, Object> getAllDERCurveDetailsHttp(@PathVariable Long derpId, HttpServletRequest request, HttpServletResponse response){
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derCurveService.getAllDERCurvesHttp(derpId);
                LOGGER.info("the der_curve_list_val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving DERCurveList value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.derCurveService.getAllDERCurves(derpId);
            return  responseEntity.getBody(); 
        }
    }

    public Map<String, Object> getAllDERCurveDetails(@PathVariable Long derpId){
        ResponseEntity<Map<String, Object>> responseEntity = this.derCurveService.getAllDERCurves(derpId);
        return  responseEntity.getBody();
    }

    @GetMapping("/edev/{edevID}/fsa/{fsaID}/derp/{derpId}/dc/{dcId}")
    public Map<String, Object> getDERCurveHttp(@PathVariable Long derpId, @PathVariable Long dcId, HttpServletRequest request, HttpServletResponse response){
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derCurveService.getDERCurveHttp(derpId, dcId);
                LOGGER.info("the der_curve_val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving DERCurve value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.derCurveService.getDERCurve(derpId, dcId);
            return  responseEntity.getBody(); 
        }
    }

    
    public Map<String, Object> getDERCurve(Long derpId, Long dcId){
        ResponseEntity<Map<String, Object>> responseEntity = this.derCurveService.getDERCurve(derpId, dcId);
        return  responseEntity.getBody();
    }

    public void updateDERCurve( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteDERCurve( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }


}
