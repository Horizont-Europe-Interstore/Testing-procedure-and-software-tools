package interstore.SelfDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SelfDeviceService {

    private Map<String, SelfDeviceEntity> sdevs = new HashMap<String, SelfDeviceEntity>();
    private static final Logger LOGGER = Logger.getLogger(SelfDeviceService.class.getName());
    
    public SelfDeviceEntity getSelfDevice(String id){
        SelfDeviceEntity sdev = sdevs.get(id);
        if(sdev != null) {
            LOGGER.info("SelfDevice found : " + sdev);
            return sdev;
        }
        else{
            return null;
        }
    }
}
