package interstore;

import com.google.inject.Guice;
import com.google.inject.Injector;
import interstore.DER.DERImpl;
import interstore.DER.DERListImpl;
import interstore.DER.DERListManager;
import interstore.DER.DERManager;
import interstore.DERCurve.DERCurveManager;
import interstore.DERCurve.DERCurveService;
import interstore.DERProgram.DERProgramService;
import interstore.DERProgram.DERProgramManager;
import interstore.DeviceCapability.DcapManager;
import interstore.DeviceCapability.DeviceCapabilityImpl;
import interstore.EndDevice.EdevManager;
import interstore.EndDevice.EndDeviceImpl;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
//import interstore.FunctionSetAssignments.FsaListImpl;
//import interstore.FunctionSetAssignments.FsaListManager;
import interstore.FunctionSetAssignments.FsaManager;
import interstore.SelfDevice.SdevManager;
import interstore.SelfDevice.SelfDeviceDto;
import interstore.SelfDevice.SelfDeviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;  
 


public class MicroServiceFactory {
    
    private Map<String, Object> microservices = new HashMap<>();
    private Map<String, Object> dtoMap;  
    private static final Logger LOGGER = Logger.getLogger(MicroServiceFactory.class.getName());
    public MicroServiceFactory() {
        this.microservices = new HashMap<>();
        this.microservices.clear(); 
        this.dtoMap = new HashMap<>(); 
        this.dtoMap.clear(); 
        microServiceFactory(); 
        createDto(); 
    }
        

    public void microServiceFactory()
    {  
        Injector injector = Guice.createInjector(new DependencyInjection());    
        ServiceFactory serviceFactory = injector.getInstance(ServiceFactory.class); 
        DeviceCapabilityImpl dcap = serviceFactory.getDeviceCapabilityProvider().get();
        DcapManager dcapManager = new DcapManager(dcap);
        SelfDeviceImpl selfDev = serviceFactory.getSelfDeviceProvider().get();
        SdevManager sdevManager = new SdevManager(selfDev);
        EndDeviceImpl endDev = serviceFactory.getEndDeviceProvider().get();
        EdevManager edevManager = new EdevManager( endDev );
        DERImpl derImpl = serviceFactory.getDERProvider().get();
        DERManager derManager = new DERManager(derImpl);
        DERListImpl derListImpl = serviceFactory.getDERListProvider().get();
        DERListManager derListManager = new DERListManager(derListImpl);
        FunctionSetAssignmentsService fsaService = serviceFactory.getFsaProvider().get();
        FsaManager fsaManager = new FsaManager(fsaService);
        DERProgramService derProgramImpl = serviceFactory.getDerProgramProvider().get();
        DERProgramManager derProgramManager = new DERProgramManager(derProgramImpl);
        DERCurveService derCurveService = serviceFactory.getDerCurveServiceProvider().get();
        DERCurveManager derCurveManager = new DERCurveManager(derCurveService);

        this.microservices.put("getalldcapmanager", dcapManager); 
        this.microservices.put("selfdevicemanager", sdevManager);
        this.microservices.put("dcapmanager", dcapManager); 
        this.microservices.put("enddevicemanager", edevManager); 
        this.microservices.put("selfenddevicemanager", dcapManager); 
        this.microservices.put("enddevicelinkmanager", edevManager); 
        this.microservices.put("enddeviceregistrationmanager", edevManager);
        this.microservices.put("createnewenddevice", edevManager); 
        this.microservices.put("enddeviceinstancemanager", edevManager); 
        this.microservices.put("findallregistrededendevice", edevManager);
        this.microservices.put("findregistrededendevice", edevManager);
      
        this.microservices.put("getDERList", derListManager);
        this.microservices.put("getDER_properties", derManager);
        this.microservices.put("update_DER_properties", derManager);
        this.microservices.put("getallFsamanager", fsaManager);    
        this.microservices.put("createFsamanager", fsaManager);
        this.microservices.put("getASingleFsamanager", fsaManager);
        this.microservices.put("fsamanager", fsaManager);
        this.microservices.put("getallDerprogrammanager", derProgramManager );
        this.microservices.put("createDerprogrammanager", derProgramManager);
        this.microservices.put("getASingleDerprogrammanager", derProgramManager);
        this.microservices.put("derprogrammanager", derProgramManager);
        this.microservices.put("timemanager", dcapManager);
        this.microservices.put("advancedtimemanager", dcapManager);
        this.microservices.put("createDerCurveManager", derCurveManager);



    } 



    public Map<String, Object> getMicroservices() {
       return microservices; 

    }
    public void createDto() {

        SelfDeviceDto selfDeviceDto = new SelfDeviceDto();
        DeviceCapabilityTest deviceCapabilitytest = new DeviceCapabilityTest();
        EndDeviceTest endDeviceTest = new EndDeviceTest();
        FunctionSetAssignmentsTest functionSetAssignmentTest = new FunctionSetAssignmentsTest();
        DerProgramTest DerProgramTest = new DerProgramTest();
        TimeTest timeTest = new TimeTest();
        DerCurveTest derCurveTest = new DerCurveTest();
        this.dtoMap.put("getalldcapmanager", deviceCapabilitytest);
        this.dtoMap.put("dcapmanager", deviceCapabilitytest); 
        this.dtoMap.put("enddevicemanager", endDeviceTest); 
        this.dtoMap.put("selfdevicemanager", selfDeviceDto);  // this is because there is no test class for selfDevice 
        this.dtoMap.put("selfenddevicemanager", deviceCapabilitytest);
        this.dtoMap.put("enddevicelinkmanager", endDeviceTest); 
        this.dtoMap.put("enddeviceregistrationmanager", endDeviceTest); 
        this.dtoMap.put("createnewenddevice", endDeviceTest); 
        this.dtoMap.put("enddeviceinstancemanager", endDeviceTest);
        this.dtoMap.put("findallregistrededendevice", endDeviceTest);
        this.dtoMap.put("findregistrededendevice", endDeviceTest);
        this.dtoMap.put("getDERList", endDeviceTest);
        this.dtoMap.put("getDER_properties", endDeviceTest);
        this.dtoMap.put("update_DER_properties", endDeviceTest);
        this.dtoMap.put("getallFsamanager", functionSetAssignmentTest);
        this.dtoMap.put("createFsamanager", functionSetAssignmentTest);
        this.dtoMap.put("getASingleFsamanager", functionSetAssignmentTest); 
        this.dtoMap.put("fsamanager", functionSetAssignmentTest);
        this.dtoMap.put("getallDerprogrammanager", DerProgramTest);
        this.dtoMap.put("getASingleDerprogrammanager", DerProgramTest);
        this.dtoMap.put("createDerprogrammanager", DerProgramTest);
      
        this.dtoMap.put("derprogrammanager", functionSetAssignmentTest);
        this.dtoMap.put("timemanager", timeTest);
        this.dtoMap.put("advancedtimemanager", timeTest);
        this.dtoMap.put("createDerCurveManager", derCurveTest);
    }
   
    public Map<String, Object> getDtoMap() {
        return this.dtoMap; 
} 



}

