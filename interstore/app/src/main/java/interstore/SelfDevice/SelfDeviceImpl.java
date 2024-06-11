package interstore.SelfDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SelfDeviceImpl {

    private Map<String, SelfDeviceDto> sdevs = new HashMap<String, SelfDeviceDto>();
    private static final Logger LOGGER = Logger.getLogger(SelfDeviceImpl.class.getName());
    
    public SelfDeviceDto getSelfDevice(String id){
        SelfDeviceDto sdev = sdevs.get(id);
        if(sdev != null) {
            LOGGER.info("SelfDevice found : " + sdev);
            return sdev;
        }
        else{
            return null;
        }
    }
}
