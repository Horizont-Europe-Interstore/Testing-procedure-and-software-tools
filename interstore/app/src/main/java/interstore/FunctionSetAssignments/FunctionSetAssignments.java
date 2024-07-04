package interstore.FunctionSetAssignments;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Entity
public class FunctionSetAssignments extends FunctionSetAssignmentBase{
//    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignments.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fsa_link")
    private String fsalink;

    @ManyToOne
    @JoinColumn(name = "fsa_list_id")
    private FunctionSetAssignmentsList fsaList;

    @Column(name = "description")
    private String description;

    @Column(name = "m_RID")
    private String mRID;

    @Column(name = "version")
    private String version;

    @Column(name = "subscribable")
    private String subscribable;

    @Column(name = "der_program_list_link")
    private String DERProgramListLink;

//    private ListLink link;
//    private Link theLink;
//    private static Map<String, List<Object>> fsaDetailsMap = new HashMap<>();
//
//    private static Map<String, String> derpMap = new HashMap<>();

    //    Map<String, String> values = new HashMap<>();
//    public FunctionSetAssignments(String href){
//        super(href);
//    }
    public FunctionSetAssignments(){

    }

    public FunctionSetAssignments(FunctionSetAssignmentsList fsaList){
        this.fsaList = fsaList;
    }

    public Long getId() {
        return id;
    }

    public FunctionSetAssignmentsList getFsaList() {
        return fsaList;
    }

    public void setFsaList(FunctionSetAssignmentsList fsaList) {
        this.fsaList = fsaList;
    }

    public String getFsalink() {
        return fsalink;
    }

    public void setFsalink(String fsalink) {
        this.fsalink = fsalink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(String subscribable) {
        this.subscribable = subscribable;
    }

    public String getDERProgramListLink() {
        return DERProgramListLink;
    }

    public void setDERProgramListLink(String DERProgramListLink) {
        this.DERProgramListLink = DERProgramListLink;
    }

    public Map<String, Object> getAll(){
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

    //    public FunctionSetAssignments(String mRID, String description, String version, String subscribable){
//
//        this.mRID = new mRIDType(mRID);
//        this.description = new String32(description);
//        this.version = new VersionType(Integer.parseInt(version));
//        this.subscribable = new SubscribableType((short) Integer.parseInt(subscribable));
//        this.link =  new ListLink();
//        this.theLink = new Link();
//        String fsaLink = link.createListOfLink(generatedLink);
//
//        this.DERProgramListLink = theLink.generateLink(generatedLink, "/derp");
//        setFsaDetailsMap(fsaLink,  this.mRID.getHexValue(), this.description.getValue(), this.version.toString(), this.subscribable.getString(), this.DERProgramListLink);
//    }
//    public static void setFsaDetailsMap(String fsaLink, String mRID, String description, String version, String subscribable, String DERProgramListLink) {
//        List<Object> fsaDeatilsList = new ArrayList<>();
//        fsaDeatilsList.add(mRID);
//        fsaDeatilsList.add(description);
//        fsaDeatilsList.add(version);
//        fsaDeatilsList.add(subscribable);
//        fsaDeatilsList.add(DERProgramListLink);
//        fsaDetailsMap.put(fsaLink, fsaDeatilsList);
//        derpMap.put(fsaLink, DERProgramListLink);
////        LOGGER.info(" the FSA details map from the method is " +  fsaDetailsMap );
//
//    }
//    public static Map<String, List<Object>> getFsaDetailsMap() {
//        return fsaDetailsMap;
//    }
//    public static Map<String, String> getDerpMap() {
//        return derpMap;
//    }
//    public Object getFSA(String FSALinkList){
//        List<Object> instances = new ArrayList<>();
//        String regex = "http:\\/\\/[^\\\"]+\\/\\d+\\/fsa\\/\\d+";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(FSALinkList);
//
//        while (matcher.find()) {
//            String link = matcher.group();
//            instances.add(getFsaDetailsMap().get(link));
//        }
//        return instances;
//    }

//    public List<FunctionSetAssignments> createFSA(String id1){
//        Map<String, List<String>> fsaAttributeMap = this.getFSAMap();
//        if (fsaAttributeMap == null) {
//            LOGGER.info("the fsa map is empty");
//            return Collections.emptyList();
//        }
//        List<String> mRIDList = fsaAttributeMap.get("mRIDList");
//        List<String> descriptionList = fsaAttributeMap.get("descriptionList");
//        List<String> versionList = fsaAttributeMap.get("versionList");
//        List<String> subscribableList = fsaAttributeMap.get("subscribableList");
//        List<FunctionSetAssignments> fsaList = new ArrayList<>();
//        for (int i = 0; i < mRIDList.size(); i++) {
//            String mRID = mRIDList.get(i);
//            String description = descriptionList.get(i);
//            String version = versionList.get(i);
//            String subscribable = subscribableList.get(i);
//            int id2 = i + 1;
//            String generatedLink = "/edev" + "/" + id1+"/"+"fsa/"+id2;
//            FunctionSetAssignments dto = new FunctionSetAssignments(mRID, description, version, subscribable, generatedLink);
//
////            DERProgram derProgram = new DERProgram();
////            derProgram.createDERProgram(generatedLink);
//            fsaList.add(dto);
//        }
//        return fsaList;
//    }

//    public Map<String, List<String>> getFSAMap(){
//        Map<String, List<String>> fsaAttributeMap = new HashMap<>();
//        List<String> mRIDList = new ArrayList<>(Arrays.asList("A1000000","A1000001","A1000003"));
//        List<String> descriptionList = new ArrayList<>(Arrays.asList("FSA 1","FSA 2","FSA 3"));
//        List<String> versionList = new ArrayList<>(Arrays.asList("1","2","3"));
//        List<String> subscribableList = new ArrayList<>(Arrays.asList("1","0","1"));
//        fsaAttributeMap.put("mRIDList", mRIDList);
//        fsaAttributeMap.put("descriptionList", descriptionList);
//        fsaAttributeMap.put("versionList", versionList);
//        fsaAttributeMap.put("subscribableList", subscribableList);
//        return fsaAttributeMap;
//    }

//    public Object getDERProgramListLink(String fsalink) {
//        Object values = fsaDetailsMap.get(fsalink);
//        return values;
//    }

}
