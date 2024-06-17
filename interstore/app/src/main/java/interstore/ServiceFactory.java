package interstore;

import com.google.inject.Inject;
import com.google.inject.Provider;
import interstore.DER.DERImpl;
import interstore.DERProgram.DERPListImpl;
import interstore.DERProgram.DERProgramImpl;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.FunctionSetAssignments.FsaImpl;
import interstore.FunctionSetAssignments.FsaListImpl;
import interstore.SelfDevice.SelfDeviceImpl;


public class ServiceFactory {
    

    private final Provider<DeviceCapabilityImpl> deviceCapabilityProvider; 
    private final Provider<EndDeviceImpl> endDeviceProvider;
    private final Provider <SelfDeviceImpl> selfDeviceProvider;
    private final Provider<DERImpl> derProvider;
    private final Provider<FsaImpl> fsaProvider;
    private final Provider<FsaListImpl> fsaListProvider;
    private final Provider<DERProgramImpl> derProgramProvider;
    private final Provider<DERPListImpl> derpListProvider;

    
    @Inject
    public ServiceFactory(Provider<DeviceCapabilityImpl> deviceCapabilityProvider,
                          Provider<EndDeviceImpl> endDeviceProvider, Provider<SelfDeviceImpl> selfDeviceProvider, Provider<DERImpl> derProvider, Provider<FsaImpl> fsaProvider, Provider<FsaListImpl> fsaListProvider, Provider<DERProgramImpl> derProgramProvider, Provider<DERPListImpl> derpListProvider) {

        this.deviceCapabilityProvider = deviceCapabilityProvider;
        this.endDeviceProvider = endDeviceProvider;
        this.selfDeviceProvider = selfDeviceProvider;
        this.derProvider = derProvider;
        this.fsaProvider = fsaProvider;
        this.fsaListProvider = fsaListProvider;
        this.derProgramProvider = derProgramProvider;
        this.derpListProvider = derpListProvider;

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
    public Provider<FsaImpl> getFsaProvider(){
        return fsaProvider;
    }
    public Provider<FsaListImpl> getFsaListProvider(){
        return fsaListProvider;
    }
    public Provider<DERProgramImpl> getDerProgramProvider(){
        return derProgramProvider;
    }
    public Provider<DERPListImpl> getDerpListProvider(){
        return derpListProvider;
    }
}




