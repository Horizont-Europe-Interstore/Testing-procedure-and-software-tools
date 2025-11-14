package interstore;
import com.google.inject.AbstractModule;
import interstore.DER.DerService;
import interstore.DERCurve.DERCurveService;
import interstore.DERProgram.DERProgramService;
import interstore.DeviceCapability.DeviceCapabilityService;
import interstore.EndDevice.EndDeviceService;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.SelfDevice.SelfDeviceService;
import interstore.DERControl.DERControlService;

public class DependencyInjection extends AbstractModule {
    @Override
    protected void configure() {
        bind(SelfDeviceService.class);
        bind(DeviceCapabilityService.class).toProvider(new SpringBeanProvider<>(DeviceCapabilityService.class));
        bind(EndDeviceService.class).toProvider(new SpringBeanProvider<>(EndDeviceService.class));
        bind(DerService.class).toProvider(new SpringBeanProvider<>(DerService.class));
        bind(FunctionSetAssignmentsService.class).toProvider(new SpringBeanProvider<>(FunctionSetAssignmentsService.class));
        bind(DERProgramService.class).toProvider(new SpringBeanProvider<>(DERProgramService.class));
        bind(DERCurveService.class).toProvider(new SpringBeanProvider<>(DERCurveService.class));
        bind(DERControlService.class).toProvider(new SpringBeanProvider<>(DERControlService.class));

    }
}












