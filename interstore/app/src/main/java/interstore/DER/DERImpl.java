package interstore.DER;

import interstore.Identity.Link;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DERImpl {

//    @Autowired
//    private DERService derService;

    @Autowired
    private DERRepository derRepository;

    @Autowired
    private DERListRepository derListRepository;

    @Autowired
    private DERCapabilityRepository derCapabilityRepository;

    @Autowired
    private DERAvailabilityRepository derAvailabilityRepository;

    @Autowired
    private DERSettingsRepository derSettingsRepository;

    @Autowired
    private DERStatusRepository derStatusRepository;
    private static final Logger LOGGER = Logger.getLogger(DERImpl.class.getName());

//    public DERDto createDER(JSONObject payloadWrapper, DERList derList) {
//        return derService.createDER(payloadWrapper, derList);
//    }
    @Transactional
    public DERDto createDER(JSONObject payloadWrapper, DERList derList) {
        JSONObject payload = payloadWrapper.optJSONObject("payload");
        DERDto derDto = new DERDto();
        derDto.setDerList(derList);
        derDto = derRepository.save(derDto);
        derList.addDerDto(derDto);
        derListRepository.save(derList);
        LOGGER.log(Level.INFO, "DERDto saved successfully " + derDto);
        setDER(derDto, payload);
        return derDto;
    }

    @SuppressWarnings("null")
    public void setDER(DERDto derDto, JSONObject payloadWrapper)
    {

        Long id = derDto.getId();
        String idString = "/"+ String.valueOf(id);
        JSONObject payload = payloadWrapper.optJSONObject("payload");
        String derListLink =  payload.optString("DERListLink", "defaultLink");
        String derLink = derListLink + idString;
        String derCapabilityLink =  derListLink + idString + payload.optString("DERCapabilityLink", "defaultLink");
        String derStatusLink = derListLink + idString + payload.optString("DERStatusLink", "defaultLink");
        String derAvailabilityLink = derListLink + idString + payload.optString("DERAvailabilityLink", "defaultLink");
        String derSettingsLink = derListLink + idString + payload.optString("DERSettingsLink", "defaultLink");

        Link link = new Link();

        link.setLink(derLink);
        derDto.setDerLink(link.getLink());

        link.setLink(derCapabilityLink);
        DERCapability derCapability = new DERCapability(derDto);
        derCapabilityRepository.save(derCapability);
        setDERCapability(derCapability);
        derDto.setDerCapability(derCapability);
        derDto.setDerCapabilityLink(derCapabilityLink);

        link.setLink(derStatusLink);
        DERStatus derStatus = new DERStatus(derDto);
        derStatusRepository.save(derStatus);
        setDERStatus(derStatus);
        derDto.setDerStatus(derStatus);
        derDto.setDerStatusLink(link.getLink());

        link.setLink(derAvailabilityLink);
        DERAvailability derAvailability = new DERAvailability(derDto);
        derAvailabilityRepository.save(derAvailability);
        setDERAvailability(derAvailability);
        derDto.setDerAvailabilityLink(link.getLink());
        derDto.setDerAvailability(derAvailability);

        link.setLink(derSettingsLink);
        DERSettings derSettings = new DERSettings(derDto);
        derSettingsRepository.save(derSettings);
        setDERSettings(derSettings);
        derDto.setDerSettings(derSettings);
        derDto.setDerSettingsLink(link.getLink());
    }

    public void setDERCapability(DERCapability derCapability){
        derCapability.setModesSupported(15);
        derCapability.setRtgAbnormalCategory(2);
        derCapability.setRtgNormalCategory(0);
    }

    public void setDERAvailability(DERAvailability derAvailability){
        Random r = new Random();
        int low = 0;
        int high = 50000;
        derAvailability.setAvailabilityDuration(r.nextInt(high-low) + low);
        derAvailability.setMaxChargeDuration(r.nextInt(high-low) + low);
    }

    public void setDERSettings(DERSettings derSettings){
        derSettings.setModesEnabled(9);
        derSettings.setSetESDelay(2000);
        derSettings.setSetGradW(75);
    }

    public void setDERStatus(DERStatus derStatus){
        Random r = new Random();
        int low = 0;
        int high = 10;
        derStatus.setAlarmStatus(r.nextInt(high-low) + low);
        derStatus.setGenConnectStatus(r.nextInt(high-low) + low);
        high = 4;
        derStatus.setInverterStatus(r.nextInt(high-low) + low);
    }
}
