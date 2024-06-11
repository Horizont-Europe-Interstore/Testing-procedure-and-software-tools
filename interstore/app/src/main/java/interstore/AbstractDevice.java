package interstore;
import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Types.DeviceCategoryType;
import interstore.Types.HexBinary160;
import interstore.Types.SFDIType;
import java.util.logging.Logger; // this is the logger class
public interface AbstractDevice {
    Logger LOGGER = Logger.getLogger(AbstractDevice.class.getName());
    DeviceCategoryType deviceCategory = new DeviceCategoryType();
    HexBinary160 lFDI = new HexBinary160();
    SFDIType sFDI = new SFDIType();
    Link link = new Link(); 
    ListLink listLink = new ListLink(); 
    
     // "3E4F45AB31EDFE5B67E343E5E4562E31984E23E5" hexBinary160 
     // 167261211391L sfid 
     
    public void setsfdi(long sfid);
    public Long getsfdi();
    public void sethexBinary160(String hexBinary160);
    public String gethexBinary160();
    public String getDeviceCategory();
    public void setDeviceCategory(String Category);
    public String getConfigurationLink(); 
    public void setConfigurationLink(String Link);
    public String getDeviceInformationLink(); 
    public void setDeviceInformationLink(String Link);
    public String getDeviceStatusLink();
    public void setDeviceStatusLink(String Link);
    public String getRegistrationLink();  
    public void setRegistrationLink(String Link);
    public String getPowerStatusLink(); 
    public void setPowerStatusLink(String Link);
    public String getFileStatusLink(); 
    public void setFileStatusLink(String Link);
    public String getIPInterfaceListLink();
    public void setIPInterfaceListLink(String Link);
    public String getDERListLink();
    public void setDERListLink(String Link);
    public String getSubscriptionListLink();
    public void setSubscriptionListLink(String Link);
    public String getFunctionSetAssignmentsListLink();
    public void setFunctionSetAssignmentsListLink(String Link);
    public String getLoadShedAvailabilityListLink();
    public void setLoadShedAvailabilityListLink(String Link);
    public String getLogEventListLink();
    public void setLogEventListLink(String Link);
   

    
    
   
 


}

