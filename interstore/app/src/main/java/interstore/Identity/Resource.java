package interstore.Identity;

public class Resource {
    private String href;

    public Resource(String href) {
        this.href = href;
    }
    public Resource() {

    }

    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }


    // this resource has an option to subscribale or not like a boolean field to make this as 
    // subscribable resource how can i impliment this 
    

    // The number devivces which is registerd in the server or how many devices 
    // the server have this information is meant by the device capability of the server 
    // usually the devcies are registerd through a registeration process there are 2 
    // types of registeration process 1. inbound registration and outbound registration
    //inbound registeration is something the device it's self registered with the server 
    // through various authentication mechanisam and validation check and the outbound is the 
    // direct registration by the operator , registering devices manually . 

    // The device capability will have 3 foriegn attributes which are EndeviceListLink, Mirror Device LIstLink, 
    // Self DeviceLink , here these attributes are created by a class called Link and ListLink class 
    // Listlink of type EndDevice , Link of type Self Device, ListLink of type Mirror Device . 
     
    
}

/*
      class Dcapability()
      {
        private ListLink<EndDevice> endDeviceListLink;
        private Link<SelfDevice> selfDeviceLink;
        private ListLink<MirrorDevice> mirrorDeviceListLink;
        private Resource resource ; 
      }
 * 
 * 
 * 
 * 
 */