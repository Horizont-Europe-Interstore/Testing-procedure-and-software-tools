package interstore.DERProgram;

import interstore.Identity.SubscribableIdentifiedObject;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Entity
public class DERProgram extends SubscribableIdentifiedObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "primacy")
    private String primacy;

    @Column(name = "description")
    private String description;

    @Column(name = "m_RID")
    private String mRID;

    @Column(name = "version")
    private String version;

    @Column(name = "subscribable")
    private String subscribable;

    @ManyToOne
    @JoinColumn(name = "derp_list_id")
    private DERPList derpList;

    @Column(name = "derp_list_link")
    private String derpListLink;
//    private ListLink link;
//    private Link theLink;
//    public String href;

    @Column(name = "derp_link")
    private String derpLink;

    @Column(name = "default_DER_Control_Link")
    private String DefaultDERControlLink;

    @Column(name = "active_DER_Control_List_Link")
    private String ActiveDERControlListLink;

    @Column(name = "DER_Control_List_Link")
    private String DERControlListLink;

    @Column(name = "DER_Curve_List_Link")
    private String DERCurveListLink;
//    private static Map<String, List<Object>> DERProgramDetailsMap = new HashMap<>();
//    private static Map<String, List<Object>> DERProgramsByListLinkMap = new HashMap<>();

//    private Map<String, String> values;

    public DERProgram(){

    }

    public DERProgram(DERPList derpList){
        this.derpList = derpList;
    }

    public Long getId() {
        return id;
    }

    public String getPrimacy() {
        return primacy;
    }

    public void setPrimacy(String primacy) {
        this.primacy = primacy;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(String subscribable) {
        this.subscribable = subscribable;
    }

    public String getDerpLink() {
        return derpLink;
    }

    public void setDerpLink(String derpLink) {
        this.derpLink = derpLink;
    }

    public String getDefaultDERControlLink() {
        return DefaultDERControlLink;
    }

    public void setDefaultDERControlLink(String defaultDERControlLink) {
        DefaultDERControlLink = defaultDERControlLink;
    }

    public String getActiveDERControlListLink() {
        return ActiveDERControlListLink;
    }

    public void setActiveDERControlListLink(String activeDERControlListLink) {
        ActiveDERControlListLink = activeDERControlListLink;
    }

    public String getDERControlListLink() {
        return DERControlListLink;
    }

    public void setDERControlListLink(String DERControlListLink) {
        this.DERControlListLink = DERControlListLink;
    }

    public String getDERCurveListLink() {
        return DERCurveListLink;
    }

    public void setDERCurveListLink(String DERCurveListLink) {
        this.DERCurveListLink = DERCurveListLink;
    }

    public String getDerpListLink() {
        return derpListLink;
    }

    public void setDerpListLink(String derpListLink) {
        this.derpListLink = derpListLink;
    }

    public DERPList getDerpList() {
        return derpList;
    }

    public void setDerpList(DERPList derpList) {
        this.derpList = derpList;
    }

    public Map<String, Object> getAll() {
        Map<String, Object> attributes = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                attributes.put(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return attributes;
    }

    //    public DERProgram(mRIDType mRID, PrimacyType primacy,VersionType version, String32 description, SubscribableType subscribable, String generatedDerpListLink,String generatedLink){
//        this.mRID = mRID;
//        this.primacy = primacy;
//        this.description = description;
//        this.version = version;
//        this.subscribable = subscribable;
//        this.theLink = new Link();
//        this.link =  new ListLink();
//        String derpLink = link.createListOfLink(generatedLink);
//        String derpListLink = link.createListOfLink(generatedDerpListLink);
//        this.DefaultDERControlLink = theLink.generateLink(generatedLink, "/dderc");
//        this.ActiveDERControlListLink = theLink.generateLink(generatedLink, "/actderc");
//        this.DERControlListLink = theLink.generateLink(generatedLink, "/derc");
//        this.DERCurveListLink = theLink.generateLink(generatedLink, "/dc");
//        setDERProgramDetailsMap(derpLink, derpListLink,this.primacy.getString(), this.mRID.getHexValue(), this.description.toString(), this.version.toString(), this.subscribable.getString(), this.DefaultDERControlLink, this.ActiveDERControlListLink, this.DERControlListLink, this.DERCurveListLink);
//    }
//    public static void setDERProgramDetailsMap(String derpLink, String derpListLink,String primacy, String mRID, String description, String version, String subscribable, String DefaultDERControlLink, String ActiveDERControlListLink, String DERControlListLink, String DERCurveListLink) {
//        List<Object> derProgramDetailsList = new ArrayList<>();
//        derProgramDetailsList.add(primacy);
//        derProgramDetailsList.add(mRID);
//        derProgramDetailsList.add(description);
//        derProgramDetailsList.add(version);
//        derProgramDetailsList.add(subscribable);
//        derProgramDetailsList.add(DefaultDERControlLink);
//        derProgramDetailsList.add(ActiveDERControlListLink);
//        derProgramDetailsList.add(DERControlListLink);
//        derProgramDetailsList.add(DERCurveListLink);
//        DERProgramDetailsMap.put(derpLink, derProgramDetailsList);
//
//        if(DERProgramsByListLinkMap.containsKey(derpListLink)){
//            if(!DERProgramsByListLinkMap.get(derpListLink).contains(derpLink)){
//                DERProgramsByListLinkMap.get(derpListLink).add(derpLink);
//            }
//        }
//        else{
//            DERProgramsByListLinkMap.put(derpListLink, new ArrayList<>());
//            DERProgramsByListLinkMap.get(derpListLink).add(derpLink);
//        }
//    }
//    public Map<String, List<Object>> getDERProgramDetailsMap(){
//        return DERProgramDetailsMap;
//    }
//    public HashMap<String, Object> getDERProgramLists(Object derpLinks){
//        String value = derpLinks.toString();
//        ObjectMapper mapper = new ObjectMapper();
//        HashMap<String, Object> response = new HashMap<>();
//        try {
//            List<String> links = mapper.readValue(value, new TypeReference<List<String>>() {});
//            for (String link : links) {
//                LOGGER.info("DERProgramListLink: " + link + " DERProgramList: " + DERProgramsByListLinkMap.get(link));
//
//                for(Object derplink : DERProgramsByListLinkMap.get(link)){
//                    LOGGER.info("DERProgram Link: " + derplink + " DERProgram Instance: " + DERProgramDetailsMap.get(derplink));
//                    response.put(derplink.toString(),DERProgramDetailsMap.get(derplink));
//                }
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }

//    public PrimacyType getPrimacy() {
//        return primacy;
//    }
//
//    public void setPrimacy(PrimacyType primacy) {
//        this.primacy = primacy;
//    }

//    public Map<String, String> getDERProgram(){
//        this.values.put("primacy", this.primacy.toString());
//        this.values.put("description", this.description.toString());
//        this.values.put("mRID", this.mRID.toString());
//        this.values.put("version", this.version.toString());
//        this.values.put("subscribable", this.subscribable.toString());
//        this.values.put("href", this.href);
//        this.values.put("DefaultDERControlLink", this.DefaultDERControlLink.toString());
//        this.values.put("ActiveDERControlListLink", this.ActiveDERControlListLink.toString());
//        this.values.put("DERControlListLink", this.DERControlListLink.toString());
//        this.values.put("DERCurveListLink",  this.DERCurveListLink.toString());
//        return this.values;
//    }


//    public List<DERProgram> createDERProgram(String generatedLink) {
//        Map<String, List<String>> derProgramAttributeMap = this.getDERProgramMap();
//        if (derProgramAttributeMap == null) {
//            LOGGER.info("the DER Program map is empty");
//            return Collections.emptyList();
//        }
//        List<String> mRIDList = derProgramAttributeMap.get("mRIDList");
//        List<String> primacyList = derProgramAttributeMap.get("primacyList");
//        List<String> descriptionList = derProgramAttributeMap.get("descriptionList");
//        List<String> versionList = derProgramAttributeMap.get("versionList");
//        List<String> subscribableList = derProgramAttributeMap.get("subscribableList");
//        List<DERProgram> derProgramList = new ArrayList<>();
//        for (int i = 0; i < mRIDList.size(); i++) {
//            String mRID = mRIDList.get(i);
//            String primacy = primacyList.get(i);
//            String description = descriptionList.get(i);
//            String version = versionList.get(i);
//            String subscribable = subscribableList.get(i);
//            int id2 = i + 1;
//            String newGeneratedLink = generatedLink + "/derp/" + id2;
//            String derpListLink = generatedLink + "/derp";
//            try{
//                DERProgram derProgram = new DERProgram(new mRIDType(mRID), new PrimacyType((short) Integer.parseInt(primacy)),new VersionType(Integer.parseInt(version)), new String32(description), new SubscribableType((short) Integer.parseInt(subscribable)), derpListLink,newGeneratedLink);
//                derProgramList.add(derProgram);
//            }
//            catch (Exception err){
//                LOGGER.info("Error: "+err);
//            }
//
//        }
//        return derProgramList;
//    }

//    public Map<String, List<String>> getDERProgramMap(){
//        Map<String, List<String>> derProgramAttributeMap = new HashMap<>();
//        List<String> mRIDList = new ArrayList<>(Arrays.asList("B01000000","B01100000","B01111110"));
//        List<String> primacyList = new ArrayList<>(Arrays.asList("89","88","84"));
//        List<String> descriptionList = new ArrayList<>(Arrays.asList("SYS-A1","SUBTX-A1-B1","TRANS-A1-B1-C1-D1-E1-F1"));
//        List<String> versionList = new ArrayList<>(Arrays.asList("2","1","1"));
//        List<String> subscribableList = new ArrayList<>(Arrays.asList("1","0","1"));
//        derProgramAttributeMap.put("mRIDList", mRIDList);
//        derProgramAttributeMap.put("primacyList", primacyList);
//        derProgramAttributeMap.put("descriptionList", descriptionList);
//        derProgramAttributeMap.put("versionList", versionList);
//        derProgramAttributeMap.put("subscribableList", subscribableList);
//        return derProgramAttributeMap;
//    }







}
