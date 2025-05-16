//package interstore.XmlStructure;
//
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
//import jakarta.xml.bind.annotation.*;
//
//import java.util.Map;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class ResourceLink {
//
//    @JacksonXmlProperty(isAttribute = true)
//    private String href;
//
//    @JacksonXmlProperty(isAttribute = true)
//    private int all;
//
//    public ResourceLink() {}
//
//    public ResourceLink(String href, Integer all) {
//        this.href = href;
//        this.all = all;
//    }
//
//    public ResourceLink(String href) {
//        this.href = href;
//    }
//
//    // Getters and setters
//    public String getHref() {
//        return href;
//    }
//
//    public void setHref(String href) {
//        this.href = href;
//    }
//
//    public Integer getAll() {
//        return all;
//    }
//
//    public void setAll(Integer all) {
//        this.all = all;
//    }
//
//}
