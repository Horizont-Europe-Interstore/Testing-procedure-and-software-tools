package interstore;

import com.google.inject.Inject;
import com.google.inject.Provider;
import interstore.DER.DERImpl;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.SelfDevice.SelfDeviceImpl; 


public class ServiceFactory {
    

    private final Provider<DeviceCapabilityImpl> deviceCapabilityProvider; 
    private final Provider<EndDeviceImpl> endDeviceProvider;
    private final Provider <SelfDeviceImpl> selfDeviceProvider;
    private final Provider<DERImpl> derProvider;

    
    @Inject
    public ServiceFactory(Provider<DeviceCapabilityImpl> deviceCapabilityProvider,
                          Provider<EndDeviceImpl> endDeviceProvider, Provider<SelfDeviceImpl> selfDeviceProvider, Provider<DERImpl> derProvider) {

        this.deviceCapabilityProvider = deviceCapabilityProvider;
        this.endDeviceProvider = endDeviceProvider;
        this.selfDeviceProvider = selfDeviceProvider;
        this.derProvider = derProvider;
    }
    
    public Provider<DeviceCapabilityImpl> getDeviceCapabilityProvider() {
        return deviceCapabilityProvider;
    }

    public Provider<EndDeviceImpl> getEndDeviceProvider(){
        return endDeviceProvider;
    }
    public Provider<SelfDeviceImpl> getSelfDeviceProvider(){
        return selfDeviceProvider;
    }
    public Provider<DERImpl> getDERProvider(){
        return derProvider;
    }
}




