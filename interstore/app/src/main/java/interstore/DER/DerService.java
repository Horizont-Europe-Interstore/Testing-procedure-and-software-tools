package interstore.DER;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceRepository;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.Identity.Link;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DerService {
    @Autowired
    private DerRepository derRepository;
    @Autowired
    private EndDeviceRepository endDeviceRepository;
    private static final Logger LOGGER = Logger.getLogger(DerService.class.getName());

    @Transactional
    public DerEntity createDer(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException  {
        DerEntity derEntity = new DerEntity();
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId"));
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        String derListLink = endDevice.getDERListLink();
        String endDeviceLink = endDevice.getEndDeviceLink();
        derEntity.setEndDevice(endDevice); 
        try {
            derEntity  = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }
        setDerCapability(derEntity  , payload,  derListLink , endDeviceLink, endDeviceId );
        derEntity = derRepository.save(derEntity);
        return  derEntity;
        
        
    }
    /*The DER Availability is a unique profile that each end device has when it's manufactured 
     * this can't be changed because these are the rated values of an End Device , In the test 
     * application while create a DER resource it needs these rated values to be set which is
     * called  DER capablity . every time when the uri hit with /dercap this will bring the 
     * capability of that end device . Der Settigns on the other hand is the values that can be
     * changed by the user . The issue is while creating the DER resource other attributes of 
     * DER resource intialized as null but later the values intailised as null has to editable  , 
     *  
     */
    @SuppressWarnings("null")
    public void setDerCapability(DerEntity derEntity, JSONObject payloadWrapper, String derListLink , String endDeviceLink ,Long endDeviceId)
    {
        
        Long Derid = derEntity.getId();
        String DeridString = "/"+ String.valueOf(Derid);
        String EndDeviceIdString  = "/"+ String.valueOf(endDeviceId);
        JSONObject payload = payloadWrapper.optJSONObject("payload");
        String derCapabilityLink = endDeviceLink + EndDeviceIdString + derListLink + DeridString+ payload.optString("DerCapabilityLink", "defaultLink");
        String derStatusLink = endDeviceLink + EndDeviceIdString + derListLink + DeridString + payload.optString("DerStatusLink", "defaultLink");
        String derAvailabilityLink = endDeviceLink +EndDeviceIdString + derListLink + DeridString+ payload.optString("DerAvailabilityLink", "defaultLink");
        String derSettingsLink = endDeviceLink + EndDeviceIdString + derListLink + DeridString + payload.optString("DerSettingsLink", "defaultLink");
        derEntity.setDerCapabilityLink(derCapabilityLink);
        Link link = new Link();

       

        link.setLink(derCapabilityLink);
       
      
  


    }

   
}
