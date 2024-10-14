package interstore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import interstore.DER.DerService;
import interstore.DERCurve.DERCurveService;
import interstore.DERProgram.DERProgramService;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.SelfDevice.SelfDeviceImpl;


public class ServiceFactory {
    

    private final Provider<DeviceCapabilityImpl> deviceCapabilityProvider; 
    private final Provider<EndDeviceImpl> endDeviceProvider;
    private final Provider <SelfDeviceImpl> selfDeviceProvider;
    private final Provider<DerService> derProvider;
    private final Provider<FunctionSetAssignmentsService> fsaProvider;
    private final Provider<DERProgramService> derProgramProvider;
    private final Provider<DERCurveService> derCurveServiceProvider;

    

    
    @Inject
    public ServiceFactory(Provider<DeviceCapabilityImpl> deviceCapabilityProvider,
                          Provider<EndDeviceImpl> endDeviceProvider, Provider<SelfDeviceImpl> selfDeviceProvider, Provider<DerService> derProvider, 
                          Provider<FunctionSetAssignmentsService> fsaProvider,
                           Provider<DERProgramService> derProgramProvider, Provider<DERCurveService> derCurveServiceProvider) {

        this.deviceCapabilityProvider = deviceCapabilityProvider;
        this.endDeviceProvider = endDeviceProvider;
        this.selfDeviceProvider = selfDeviceProvider;
        this.derProvider = derProvider;
        this.fsaProvider = fsaProvider;
        this.derProgramProvider = derProgramProvider;
        this.derCurveServiceProvider = derCurveServiceProvider;
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
    public Provider<DerService> getDERProvider(){
        return derProvider;
    }
    public Provider<FunctionSetAssignmentsService> getFsaProvider(){
        return fsaProvider;
    }
    
    public Provider<DERProgramService> getDerProgramProvider(){
        return derProgramProvider;
    }
   
    public Provider<DERCurveService> getDerCurveServiceProvider(){
        return derCurveServiceProvider;
    }

}

