package interstore;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import interstore.DER.DerManager;
import interstore.DERControl.DERControlManager;
import interstore.DERCurve.DERCurveManager;
import interstore.DERProgram.DERProgramManager;
import interstore.DeviceCapability.DcapManager;
import interstore.EndDevice.EdevManager;
import interstore.FunctionSetAssignments.FsaManager;
import interstore.SelfDevice.SdevManager;

import java.util.Map;
import java.util.logging.Logger;

@RestController
public class ApiController {
    private static final Logger LOGGER = Logger.getLogger(ApiController.class.getName());

    @Autowired
    private EdevManager edevManager;

    @Autowired
    private DcapManager dcapManager;

    @Autowired
    private SdevManager sdevManager;

    @Autowired
    private FsaManager fsaManager;

    @Autowired
    private DerManager derManager;

    @Autowired
    private DERProgramManager derProgramManager;

    @Autowired
    private DERCurveManager derCurveManager;

    @Autowired
    private DERControlManager derControlManager;

    @PostMapping("/api")
    public Object handleApiRequest(@RequestBody String payload) {
        try {
            LOGGER.info("Received API request: " + payload);
            JSONObject jsonObject = new JSONObject(payload);
            String serviceName = jsonObject.optString("servicename", "");

            switch (serviceName) {
                case "enddevicemanager":
                    return edevManager.chooseMethod_basedOnAction(payload);
                case "devicecapabilitymanager":
                    return dcapManager.chooseMethod_basedOnAction(payload);
                case "selfdevicemanager":
                    sdevManager.chooseMethod_basedOnAction(payload);
                    return Map.of("status", "success");
                case "functionsetassignmentsmanager":
                    return fsaManager.chooseMethod_basedOnAction(payload);
                case "dermanager":
                    return derManager.chooseMethod_basedOnAction(payload);
                case "derprogrammanager":
                    return derProgramManager.chooseMethod_basedOnAction(payload);
                case "dercurvemanager":
                    return derCurveManager.chooseMethod_basedOnAction(payload);
                case "dercontrolmanager":
                    return derControlManager.chooseMethod_basedOnAction(payload);
                default:
                    LOGGER.warning("Unknown service: " + serviceName);
                    return Map.of("error", "Unknown service: " + serviceName);
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing API request: " + e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
}
