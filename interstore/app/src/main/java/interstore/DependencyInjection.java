package interstore;
import com.google.inject.AbstractModule;
import interstore.DER.DERImpl;
import interstore.DERProgram.DERProgramService;
import interstore.DER.DERListImpl;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.SelfDevice.SelfDeviceImpl;

public class DependencyInjection extends AbstractModule {
    @Override
    protected void configure() {
        bind(SelfDeviceImpl.class);
        bind(DeviceCapabilityImpl.class).toProvider(new SpringBeanProvider<>(DeviceCapabilityImpl.class));
        bind(EndDeviceImpl.class).toProvider(new SpringBeanProvider<>(EndDeviceImpl.class));
        bind(DERImpl.class).toProvider(new SpringBeanProvider<>(DERImpl.class));
        bind(DERListImpl.class).toProvider(new SpringBeanProvider<>(DERListImpl.class));
        bind(FunctionSetAssignmentsService.class).toProvider(new SpringBeanProvider<>(FunctionSetAssignmentsService.class));
        bind(DERProgramService.class).toProvider(new SpringBeanProvider<>(DERProgramService.class));
    }
}













/*
 * package interstore;
import com.google.inject.AbstractModule;

import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.SelfDevice.SelfDeviceImpl;

public class DependencyInjection extends AbstractModule{
    @Override
    protected void configure() {
        bind(SelfDeviceImpl.class);
        bind(DeviceCapabilityImpl.class); 
        bind(EndDeviceImpl.class);
    }
}
 * 
 * 
 */