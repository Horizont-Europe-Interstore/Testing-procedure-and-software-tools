//package interstore.XmlStructure;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
//import jakarta.xml.bind.annotation.*;
//
//@JacksonXmlRootElement(localName = "DeviceCapability", namespace = "http://ieee.org/2030.5")
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@XmlType(propOrder = {
//        "demandResponseProgramListLink",
//        "derProgramListLink",
//        "messagingProgramListLink",
//        "responseSetListLink",
//        "tariffProfileListLink",
//        "timeLink",
//        "usagePointListLink",
//        "endDeviceListLink",
//        "mirrorUsagePointListLink",
//        "selfDeviceLink"
//})
//@XmlAccessorType(XmlAccessType.FIELD)
//public class DeviceCapabilityXml {
//    @JacksonXmlProperty(isAttribute = true)
//    private String href;
//
//    @JacksonXmlProperty(localName = "DemandResponseProgramListLink")
//    private ResourceLink demandResponseProgramListLink;
//
//    @JacksonXmlProperty(localName = "DERProgramListLink")
//    private ResourceLink derProgramListLink;
//
//    @JacksonXmlProperty(localName = "MessagingProgramListLink")
//    private ResourceLink messagingProgramListLink;
//
//    @JacksonXmlProperty(localName = "ResponseSetListLink")
//    private ResourceLink responseSetListLink;
//
//    @JacksonXmlProperty(localName = "TariffProfileListLink")
//    private ResourceLink tariffProfileListLink;
//
//    @JacksonXmlProperty(localName = "TimeLink")
//    private ResourceLink timeLink;
//
//    @JacksonXmlProperty(localName = "UsagePointListLink")
//    private ResourceLink usagePointListLink;
//
//    @JacksonXmlProperty(localName = "EndDeviceListLink")
//    private ResourceLink endDeviceListLink;
//
//    @JacksonXmlProperty(localName = "MirrorUsagePointListLink")
//    private ResourceLink mirrorUsagePointListLink;
//
//    @JacksonXmlProperty(localName = "SelfDeviceLink")
//    private ResourceLink selfDeviceLink;
//
//    public String getHref() {
//        return href;
//    }
//
//    public void setHref(String href) {
//        this.href = href;
//    }
//
//    public ResourceLink getDemandResponseProgramListLink() {
//        return demandResponseProgramListLink;
//    }
//
//    public void setDemandResponseProgramListLink(ResourceLink demandResponseProgramListLink) {
//        this.demandResponseProgramListLink = demandResponseProgramListLink;
//    }
//
//    public ResourceLink getDerProgramListLink() {
//        return derProgramListLink;
//    }
//
//    public void setDerProgramListLink(ResourceLink derProgramListLink) {
//        this.derProgramListLink = derProgramListLink;
//    }
//
//    public ResourceLink getMessagingProgramListLink() {
//        return messagingProgramListLink;
//    }
//
//    public void setMessagingProgramListLink(ResourceLink messagingProgramListLink) {
//        this.messagingProgramListLink = messagingProgramListLink;
//    }
//
//    public ResourceLink getResponseSetListLink() {
//        return responseSetListLink;
//    }
//
//    public void setResponseSetListLink(ResourceLink responseSetListLink) {
//        this.responseSetListLink = responseSetListLink;
//    }
//
//    public ResourceLink getTariffProfileListLink() {
//        return tariffProfileListLink;
//    }
//
//    public void setTariffProfileListLink(ResourceLink tariffProfileListLink) {
//        this.tariffProfileListLink = tariffProfileListLink;
//    }
//
//    public ResourceLink getTimeLink() {
//        return timeLink;
//    }
//
//    public void setTimeLink(ResourceLink timeLink) {
//        this.timeLink = timeLink;
//    }
//
//    public ResourceLink getUsagePointListLink() {
//        return usagePointListLink;
//    }
//
//    public void setUsagePointListLink(ResourceLink usagePointListLink) {
//        this.usagePointListLink = usagePointListLink;
//    }
//
//    public ResourceLink getEndDeviceListLink() {
//        return endDeviceListLink;
//    }
//
//    public void setEndDeviceListLink(ResourceLink endDeviceListLink) {
//        this.endDeviceListLink = endDeviceListLink;
//    }
//
//    public ResourceLink getMirrorUsagePointListLink() {
//        return mirrorUsagePointListLink;
//    }
//
//    public void setMirrorUsagePointListLink(ResourceLink mirrorUsagePointListLink) {
//        this.mirrorUsagePointListLink = mirrorUsagePointListLink;
//    }
//
//    public ResourceLink getSelfDeviceLink() {
//        return selfDeviceLink;
//    }
//
//    public void setSelfDeviceLink(ResourceLink selfDeviceLink) {
//        this.selfDeviceLink = selfDeviceLink;
//    }
//}
