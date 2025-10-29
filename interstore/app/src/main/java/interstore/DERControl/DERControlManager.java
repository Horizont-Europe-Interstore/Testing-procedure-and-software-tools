package interstore.DERControl;
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
public class DERControlManager {
    private static final Logger LOGGER = Logger.getLogger(DERControlManager.class.getName());

    DERControlService derControlService;

    public DERControlManager(DERControlService derControlService) {
        this.derControlService = derControlService;
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
                return addDERControl(jsonObject);
            case "get":
                return getDERControl(jsonObject);
            case "put":
                updateDERControl(jsonObject);
                break;
            case "delete":
                deleteDERControl(jsonObject);
                break;
        }
        return "Operation completed successfully";
    }

    public Map<String, Object> addDERControl(JSONObject payload) throws NumberFormatException, JSONException, ChangeSetPersister.NotFoundException {

        LOGGER.info("the received payload in the DER Control Manager class  is " +  payload);
        DERControlEntity derControlEntity = this.derControlService.createDERControl(payload);
        LOGGER.info("the response from DER Control get ID is " +  derControlEntity.getId());
        LOGGER.info("the response from DER Control derControlBase " +  derControlEntity.getDer_control_base());
        return Map.of("id", derControlEntity.getId(), "deviceCategory", derControlEntity.getDeviceCategory(), "derControlLink", derControlEntity.getDerControlLink(),"derControlBase", derControlEntity.getDer_control_base());

    }

    // @GetMapping("/derp/{derpId}/derc/{dercId}")
    // public Map<String, Object> getDERControlHttp(@PathVariable Long derpId, @PathVariable Long dercId, HttpServletRequest request, HttpServletResponse response){
    //     // Case: called from HTTP
    //     if (RequestContextHolder.getRequestAttributes() != null) {
    //         try{
    //             String responseEntity = this.derControlService.getDERControlHttp(derpId, dercId);
    //             LOGGER.info("the der_curve_val is " + responseEntity);
    //             byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
    //             response.setStatus(HttpServletResponse.SC_OK);
    //             response.setContentType("application/sep+xml;level=S1");
    //             response.setHeader("Cache-Control", "no-cache");
    //             response.setHeader("Connection", "keep-alive");
    //             response.setContentLength(bytes.length);
    //             ServletOutputStream out = response.getOutputStream();
    //             out.write(bytes);
    //             out.flush();
    //         } catch(Exception e){
    //             LOGGER.severe("Error retrieving DERControl value: " + e.getMessage());
    //             response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //         }
    //         return null;
    //     } 
    //     // Case: called internally
    //     else {
    //         ResponseEntity<Map<String, Object>> responseEntity = this.derControlService.getDERControl(derpId, dercId);
    //         return  responseEntity.getBody(); 
    //     }
    // }

    public Map<String, Object> getDERControl(JSONObject payload) throws JSONException
    {
        LOGGER.info("Response received in DERControlManager: "+payload);
        if(payload.has("derpID") && payload.has("derControlID"))
        {
            Long derpID = payload.getLong("derpID");
            Long derControlID = payload.getLong("derControlID");
            ResponseEntity<Map<String, Object>> response = this.derControlService.getDERControl(derpID, derControlID);
            return response.getBody();
        }

        else if(payload.has("derpID"))
        {   Long derpID = payload.getLong("derpID");

            return getAllDERControlDetails(derpID);
        }

        return null ;
//        if(payload.has("derControlLink"))
//        {
//            return this.derControlService.
//                    getDERControl(Long.parseLong(payload.getJSONObject("payload").getString("der_program_id")),
//                            Long.parseLong(payload.getJSONObject("payload").getString("der_control_id")))
//                    .getBody();
//        }
//
//
//        return getAllDERControlDetails(Long.parseLong(payload.getJSONObject("payload").getString("der_program_id")));
    }

    @GetMapping("/derp/{derpId}/derc")
    public Map<String, Object> getAllDERCurveDetailsHttp(@PathVariable Long derpId, HttpServletRequest request, HttpServletResponse response){
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derControlService.getAllDERControlsHttp(derpId);
                LOGGER.info("the der_control_list_val is " + responseEntity);
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
                LOGGER.severe("Error retrieving DERControlList value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            return getAllDERControlDetails(derpId); 
        }
    }


    public Map<String, Object> getAllDERControlDetails(Long id){
        ResponseEntity<Map<String, Object>> responseEntity = this.derControlService.getAllDERControls(id);
        return  responseEntity.getBody();
    }

    public void updateDERControl( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteDERControl( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }

}
