package interstore;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XmlCache {
    
    private static final Map<String, String> CACHED_XMLS = new ConcurrentHashMap<>();
    private static final String XML_BASE_PATH = "/app/src/test/resources/example-xml/";
    
    // Regex to extract root element name from XML
    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile("<(\\w+)(?:\\s|>)");
    
    static {
        loadXmlFiles();
    }
    
    private static void loadXmlFiles() {
        try {
            System.out.println("🔄 Loading XML files into cache...");
            
            // Load XML files with your naming convention
            loadXmlFile("DeviceCapability");
            loadXmlFile("DERControlList");
            loadXmlFile("DERCurve");
            loadXmlFile("DERProgramList");
            loadXmlFile("EndDeviceList");
            loadXmlFile("EndDevice");
            loadXmlFile("FunctionSetAssignmentsList");
            loadXmlFile("Registration");
            // Add more as needed
            
            System.out.println("✅ XML Cache loaded with " + CACHED_XMLS.size() + " files");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to load XML cache: " + e.getMessage());
        }
    }
    
    private static void loadXmlFile(String rootElementName) {
        try {
            String filename = rootElementName + ".xml";
            Path xmlPath = Paths.get(XML_BASE_PATH + filename);
            
            if (Files.exists(xmlPath)) {
                String content = Files.readString(xmlPath);
                CACHED_XMLS.put(rootElementName, content);
                System.out.println("📄 Cached: " + rootElementName + ".xml");
            } else {
                System.err.println("⚠️ XML file not found: " + filename);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load " + rootElementName + ".xml: " + e.getMessage());
        }
    }
    
    // SUPER SIMPLE: Extract root element and get matching XML
    public static String selectByRootElement(String actualXml) {
        try {
            // Extract root element name from actual XML
            String rootElementName = extractRootElement(actualXml);
            
            if (rootElementName != null) {
                String expectedXml = CACHED_XMLS.get(rootElementName);
                
                if (expectedXml != null) {
                    System.out.println("✅ Found matching XML: " + rootElementName + ".xml");
                    return expectedXml;
                } else {
                    System.err.println("⚠️ No cached XML found for root element: " + rootElementName);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error extracting root element: " + e.getMessage());
        }
        
        // Fallback to DeviceCapability if no match
        System.out.println("⚠️ Using DeviceCapability.xml as fallback");
        return CACHED_XMLS.get("DeviceCapability");
    }
    
    // Extract root element name from XML string
    private static String extractRootElement(String xml) {
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(xml.trim());
        if (matcher.find()) {
            String rootElement = matcher.group(1);
            System.out.println("🔍 Detected root element: " + rootElement);
            return rootElement;
        }
        return null;
    }
    
    // Alternative method if you want to pass root element directly
    public static String getByRootElement(String rootElementName) {
        return CACHED_XMLS.getOrDefault(rootElementName, CACHED_XMLS.get("DeviceCapability"));
    }
    
    // Utility methods
    public static int getCacheSize() {
        return CACHED_XMLS.size();
    }
    
    public static void printLoadedFiles() {
        System.out.println("📊 Loaded XML files:");
        CACHED_XMLS.keySet().forEach(key -> 
            System.out.println("  ✓ " + key + ".xml")
        );
    }
}