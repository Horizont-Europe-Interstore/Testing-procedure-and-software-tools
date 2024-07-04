package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceImpl;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionSetAssignmentTest {
    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentTest.class.getName());
    private static String serviceName;
    //    EndDeviceImpl endDeviceImpl;
//    FsaImpl fsaImpl;
//    FsaListImpl fsaListImpl;
    static String fsaIds;
    static Object fsaInstances;
    static List<String> derpListLinks = new ArrayList<>();
    static String derProgramInstance;

    public FunctionSetAssignmentTest(){
//        endDeviceImpl = new EndDeviceImpl();
//        fsaImpl = new FsaImpl();
//        fsaListImpl = new FsaListImpl();
    }

//    public Object getEndDeviceID(String regId){
//
//
//        String url = endDeviceImpl.getEndDeviceByRegistration(regId);
//        Pattern pattern = Pattern.compile(".*/edev/(\\d+)");
//        Matcher matcher = pattern.matcher(url);
//        if (matcher.find()) {
//            String id = matcher.group(1);
//            return id;
//        } else {
//            System.out.println("ID: "+matcher.group(1)+" was not found");
//            return null;
//        }
//    }

    public Object getFSAList(String responsePayload) {
        LOGGER.info("The FSAList ID's for the EndDevice is "+ responsePayload);
        fsaIds = responsePayload;
        return responsePayload;
    }

    public Object getFSAInstance(Object responsePayload){
        LOGGER.info("The FSA instances for the EndDevice is " + responsePayload);
        fsaInstances = responsePayload;
        return responsePayload;
    }

//    public Map<String, String> getDerpMap(){
//        Map<String, String> derpMap = fsaImpl.getderpMap();
//        return derpMap;
//    }


    public static String getDerProgramInstance() {
        return derProgramInstance;
    }

    public static void setDerProgramInstance(String derProgramInstance) {
        FunctionSetAssignmentTest.derProgramInstance = derProgramInstance;
    }

    public static String getserviceName(){
        return serviceName;
    }
    public static void setServicename(String serviceName)
    {
        FunctionSetAssignmentTest.serviceName = serviceName;
    }
    public static String  getFSAListQuery(String regPin, Long id){
        String refClientPin = "111115";
        if (regPin.equals(refClientPin)) {
            LOGGER.info("PIN value is the same PIN value the REF-Client device has preregistered with pin: " + regPin);
            return queryServiceFSAList("get", id);
        } else {
            LOGGER.info("PIN value is not the same PIN value the REF-Client device has preregistered." + id);
            return queryServiceFSAList("get",  Integer.toUnsignedLong(0));
        }
    }
    public static List<Integer> getPin(String payload){
        JSONObject jsonObject = new JSONObject(payload);
        JSONObject registeredEndDevice = jsonObject.getJSONObject("RegisteredEndDevice");
        int id = registeredEndDevice.getInt("id");
        int pin = registeredEndDevice.getInt("pin");
        List<Integer> values = new ArrayList<>();
//        String regex = "\"http://localhost/interstore/edev/(\\d+)/rg\":\"(\\d+)\"";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(regIDString);
//        if(matcher.find()) {
//            String edevID = matcher.group(1);
//            String regID = matcher.group(2);
//            values.add(edevID);
//            values.add(regID);
//        }
        values.add(id);
        values.add(pin);
        return values;
    }

//    public List<String> getDerpLinks(){
//        Map<String, String> derpMap = this.getDerpMap();
//        List<String> derplinks = new ArrayList<>();
//        for (String fsaLink : extractFsaLinks(fsaList)) {
//            if (derpMap.containsKey(fsaLink)) {
//                String derpValue = derpMap.get(fsaLink);
//                derplinks.add(derpValue);
//
//            } else {
//                LOGGER.info("No corresponding value found for link: " + fsaLink);
//            }
//        }
//        LOGGER.info("The DERProgramListLinks  for the EndDevice are: " + derplinks);
//        return derplinks;
//
//    }

    public static String getDERProgramInstance(String responsePayload){
        derProgramInstance = responsePayload;
        LOGGER.info("DERProgram Instance: " + derProgramInstance);
        return responsePayload;
    }
    public static void setDERPListLinks(){
        String val = fsaInstances.toString();
        Pattern pattern = Pattern.compile("DERProgramListLink=([^,}]+)");
        Matcher matcher = pattern.matcher(val);
        while (matcher.find()) {
            String link = matcher.group(1);
            derpListLinks.add(link);
        }
    }
    //    public static List<String> extractFsaLinks(String fsaListString) {
//        List<String> fsaLinks = new ArrayList<>();
//        Pattern pattern = Pattern.compile("\"(http://[^\\\"]+)\"");
//        Matcher matcher = pattern.matcher(fsaListString);
//
//        while (matcher.find()) {
//            fsaLinks.add(matcher.group(1));
//        }
//
//        return fsaLinks;
//    }
    public static String queryServiceFSAList( String action, Long regID)
    {
        EndDeviceImpl endDeviceImpl = ApplicationContextProvider.getApplicationContext().getBean(EndDeviceImpl.class);
        EndDeviceDto endDeviceDto = endDeviceImpl.getEndDeviceByRegistrationID(regID);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", action);
        attributes.put("payload", endDeviceDto.getFunctionSetAssignmentsListLink());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String  getFSAQuery(String payload){

        return queryServiceFSA("get", payload);
    }
    public static String queryServiceFSA( String action, String payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", action);
        attributes.put("payload", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDERProgramQuery(List<String> payload){
        return queryServiceDERProgram("get", payload);
    }
    public static String queryServiceDERProgram( String action, List<String> payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", action);
        attributes.put("payload", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
