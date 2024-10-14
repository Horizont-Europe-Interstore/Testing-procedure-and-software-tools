package interstore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import interstore.DER.DERImpl;
import interstore.DER.DERListImpl;
import interstore.DERControl.DERControlService;
import interstore.DERCurve.DERCurveService;
import interstore.DERProgram.DERProgramService;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
//import interstore.FunctionSetAssignments.FsaListImpl;
import interstore.SelfDevice.SelfDeviceImpl;


public class ServiceFactory {
    

    private final Provider<DeviceCapabilityImpl> deviceCapabilityProvider; 
    private final Provider<EndDeviceImpl> endDeviceProvider;
    private final Provider <SelfDeviceImpl> selfDeviceProvider;
    private final Provider<DERImpl> derProvider;
    private final Provider<FunctionSetAssignmentsService> fsaProvider;
    private final Provider<DERProgramService> derProgramProvider;
    private final Provider<DERCurveService> derCurveServiceProvider;
    private final Provider<DERControlService> derControlServiceProvider;

    private final Provider<DERListImpl> derListProvider;

    
    @Inject
    public ServiceFactory(Provider<DeviceCapabilityImpl> deviceCapabilityProvider,
                          Provider<EndDeviceImpl> endDeviceProvider, Provider<SelfDeviceImpl> selfDeviceProvider, Provider<DERImpl> derProvider, 
                          Provider<FunctionSetAssignmentsService> fsaProvider,
                           Provider<DERProgramService> derProgramProvider, Provider<DERListImpl> derListProvider, Provider<DERCurveService> derCurveServiceProvider, Provider<DERControlService> derControlServiceProvider) {

        this.deviceCapabilityProvider = deviceCapabilityProvider;
        this.endDeviceProvider = endDeviceProvider;
        this.selfDeviceProvider = selfDeviceProvider;
        this.derProvider = derProvider;
        this.fsaProvider = fsaProvider;
        this.derProgramProvider = derProgramProvider;
        this.derListProvider = derListProvider;
        this.derCurveServiceProvider = derCurveServiceProvider;
        this.derControlServiceProvider = derControlServiceProvider;
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
    public Provider<FunctionSetAssignmentsService> getFsaProvider(){
        return fsaProvider;
    }
    
    public Provider<DERProgramService> getDerProgramProvider(){
        return derProgramProvider;
    }
    public Provider<DERListImpl> getDERListProvider(){
        return derListProvider;
    }
    public Provider<DERCurveService> getDerCurveServiceProvider(){
        return derCurveServiceProvider;
    }
    public Provider<DERControlService> getDerControlServiceProvider(){
        return derControlServiceProvider;
    }

}

