package interstore;

import com.google.inject.AbstractModule;
import interstore.DER.DERImpl;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EndDeviceImpl;
import interstore.SelfDevice.SelfDeviceImpl;
//import interstore.Identity.LinkService; 
public class DependencyInjection extends AbstractModule {
    @Override
    protected void configure() {
        bind(SelfDeviceImpl.class);
        // Use SpringBeanProvider to provide DeviceCapabilityImpl instances
        bind(DeviceCapabilityImpl.class).toProvider(new SpringBeanProvider<>(DeviceCapabilityImpl.class));
        //bind(LinkService.class).toProvider(new SpringBeanProvider<>(LinkService.class));
        bind(EndDeviceImpl.class).toProvider(new SpringBeanProvider<>(EndDeviceImpl.class));
        //bind(EndDeviceImpl.class);
        bind(DERImpl.class).toProvider(new SpringBeanProvider<>(DERImpl.class));
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