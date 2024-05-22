//package interstore.DER;
//
//import interstore.Identity.Link;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Service
//public class DERServiceImpl implements DERService{
//
//    private static final Logger LOGGER = Logger.getLogger(DERServiceImpl.class.getName());
//
//    @Autowired
//    private DERRepository derRepository;
//
//    @Autowired
//    private DERListRepository derListRepository;
//
//    @Override
//    @Transactional
//    public DERDto createDER(JSONObject payloadWrapper, DERList derList) {
//        JSONObject payload = payloadWrapper.optJSONObject("payload");
//        DERDto derDto = new DERDto();
//        derList = derListRepository.save(derList);  // Save DERList first
//        derDto.setDerList(derList);
//        derDto = derRepository.save(derDto);
//        LOGGER.log(Level.INFO, "DERDto saved successfully " + derDto);
//        setDER(derDto, payload);
//        return derDto;
//    }
//
//    @SuppressWarnings("null")
//    public void setDER(DERDto derDto, JSONObject payloadWrapper)
//    {
//
//        Long id = derDto.getId();
//        String idString = "/"+ String.valueOf(id);
//        JSONObject payload = payloadWrapper.optJSONObject("payload");
//        String derListLink =  payload.optString("DERListLink", "defaultLink");
//        String derCapabilityLink =  derListLink + idString + payload.optString("DERCapabilityLink", "defaultLink");
//        String derStatusLink = derListLink + idString + payload.optString("DERStatusLink", "defaultLink");
//        String derAvailabilityLink = derListLink + idString + payload.optString("DERAvailabilityLink", "defaultLink");
//        String derSettingsLink = derListLink + idString + payload.optString("DERSettingsLink", "defaultLink");
//
//        Link link = new Link();
//
//        link.setLink(derCapabilityLink);
//        derDto.setDerCapabilityLink(link.getLink());
//
//        link.setLink(derStatusLink);
//        derDto.setDerStatusLink(link.getLink());
//
//        link.setLink(derAvailabilityLink);
//        derDto.setDerAvailabilityLink(link.getLink());
//
//        link.setLink(derSettingsLink);
//        derDto.setDerSettingsLink(link.getLink());
//
//
//    }
//}
