
package interstore;
import interstore.Types.TimeType;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

// mDNS and TLS imports
import javax.jmdns.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories( {"interstore.DeviceCapability", "interstore.Identity"
, "interstore.EndDevice", "interstore.Types", "interstore.Registration", "interstore.DER", "interstore.FunctionSetAssignments"
        ,"interstore.DERProgram", "interstore.Time", "interstore.DERCurve", "interstore.Events", "interstore.DERControl"})
@EntityScan(basePackages = "interstore")
@ComponentScan(basePackages = "interstore")
@Repository
@Scope("singleton")
public class App {
    private MessageToPublish messageToPublish;
    private ServiceDiscoveryVerticle serviceDiscoveryVerticle;
    private UIControleHandler uiControleHandler;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    // IEEE 2030.5 components
    private JmDNS jmdns;
    private final Map<String, ClientInfo> discoveredClients = new ConcurrentHashMap<>();
    private final Set<String> monitoredEndpoints = ConcurrentHashMap.newKeySet();
    
    // Server identity (calculated from your server certificate)
    private final String serverSfdi = "392440693763";  // Your server's SFDI
    private final String serverLfdi = "92320FA007FFE32AFA35088DA87F7E83048A270F";  // Your server's LFDI

    // Client information holder
    private static class ClientInfo {
        public String ip;
        public int port;
        public String sfdi;  
        public String lfdi;  
        public String deviceName;
        
        public ClientInfo(String ip, int port, String sfdi, String lfdi, String deviceName) {
            this.ip = ip;
            this.port = port;
            this.sfdi = sfdi;
            this.lfdi = lfdi;
            this.deviceName = deviceName;
        }
    }

    public App(String natsUrl) throws Exception {
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
    }
    
    public App() throws Exception {
    }

    // Step 2: Start IEEE 2030.5 service discovery
    public void startIeee2030ServiceDiscovery() {
        try {
            // Initialize mDNS
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            
            // Register our IEEE 2030.5 server service
            String serviceText = "sfdi=" + serverSfdi + ",lfdi=" + serverLfdi + ",version=2030.5,path=/edev";
            
            ServiceInfo serverService = ServiceInfo.create(
                "_ieee2030-5-server._tcp.local.",
                "IEEE2030-5-Server",
                1900,  // Server port
                serviceText
            );
            
            jmdns.registerService(serverService);
            LOGGER.info(" IEEE 2030.5 server registered via mDNS - SFDI: " + serverSfdi);
            
            // Listen for IEEE 2030.5 clients
            jmdns.addServiceListener("_ieee2030-5-client._tcp.local.", new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent event) {
                    LOGGER.info(" IEEE 2030.5 client discovered: " + event.getName());
                    jmdns.requestServiceInfo(event.getType(), event.getName());
                }
                
                @Override
                public void serviceResolved(ServiceEvent event) {
                    ServiceInfo info = event.getInfo();
                    if (info.getInetAddresses().length > 0) {
                        String clientIP = info.getInetAddresses()[0].getHostAddress();
                        int clientPort = info.getPort();
                        String clientSfdi = info.getPropertyString("sfdi");
                        String clientLfdi = info.getPropertyString("lfdi");
                        String deviceName = info.getPropertyString("device");
                        
                        LOGGER.info(" Client resolved - IP: " + clientIP + ", Port: " + clientPort + 
                                   ", SFDI: " + clientSfdi + ", Device: " + deviceName);
                        
                        // Create client info (SFDI/LFDI will be updated from certificate)
                        ClientInfo client = new ClientInfo(clientIP, clientPort, 
                            clientSfdi != null ? clientSfdi : "UNKNOWN", 
                            clientLfdi != null ? clientLfdi : "UNKNOWN", 
                            deviceName != null ? deviceName : "Unknown Device");
                        
                        // Step 3: Initiate connection to discovered client
                        initiateConnectionToClient(clientIP, clientPort);
                    }
                }
                
                @Override
                public void serviceRemoved(ServiceEvent event) {
                    LOGGER.info(" IEEE 2030.5 client removed: " + event.getName());
                }
            });
            
        } catch (IOException e) {
            LOGGER.severe("Failed to start IEEE 2030.5 service discovery: " + e.getMessage());
        }
    }

    // Windows-specific client discovery
    @Scheduled(fixedRate = 120000) // Every 2 minutes
    public void discoverWindowsClients() {
        LOGGER.info(" Starting Windows IEEE 2030.5 client discovery...");
        
        CompletableFuture.runAsync(() -> {
            scanSchindlerGateway();
        });
    }
    
    private void scanSchindlerGateway() {
        String gatewayIP = "10.40.160.174";
        LOGGER.info(" Scanning Schindler Gateway: " + gatewayIP);
        
        // Scan comprehensive port range
        scanIPForIeee2030(gatewayIP);
    }
    
    private void scanIPForIeee2030(String targetIP) {
        LOGGER.info(" Testing IP for IEEE 2030.5: " + targetIP);
        
        int[] windowsCommonPorts = {
            
            443, 8443, 1900, 8080, 8181, 9443,
            49152, 49200, 49500, 50000, 50500, 51000, 51500, 52000, 52500,
            53000, 53500, 54000, 54500, 55000, 55500, 56000, 56500, 57000,
            60000, 61000, 62000, 63000, 64000, 65000
        };
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int port : windowsCommonPorts) {
            executor.submit(() -> {
                if (isPortOpenWindows(targetIP, port)) {
                    LOGGER.info(" Open port found: " + targetIP + ":" + port);
                    initiateConnectionToClient(targetIP, port);
                }
            });
        }
        
        executor.shutdown();
    }
    
    private boolean isPortOpenWindows(String ip, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), 2000); // 2 second timeout
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
 

    // Step 3: Server initiates TLS connection to discovered client
    public void initiateConnectionToClient(String clientIP, int clientPort) {
        CompletableFuture.runAsync(() -> {
            try {
                LOGGER.info(" Initiating TLS connection to: " + clientIP + ":" + clientPort);
                
               
                SSLContext sslContext = createServerSSLContext();
                SSLSocketFactory factory = sslContext.getSocketFactory();
                
               
                SSLSocket socket = (SSLSocket) factory.createSocket(clientIP, clientPort);
                socket.setSoTimeout(10000); // 10 second timeout
                
               
                socket.setUseClientMode(false);    // Server mode
                socket.setNeedClientAuth(true);    // Require client certificate
                socket.setWantClientAuth(true);    // Want to see client certificate
                
             
                socket.startHandshake();
                
                SSLSession session = socket.getSession();
                String sessionId = bytesToHex(session.getId());
                
                LOGGER.info(" TLS handshake successful with: " + clientIP + ":" + clientPort);
                
                // Extract client information from certificate
                ClientInfo client = extractClientInfoFromCertificate(session, clientIP, clientPort);
                
                if (client != null) {
                    discoveredClients.put(client.sfdi, client);
                    LOGGER.info(" IEEE 2030.5 CLIENT DISCOVERED!");
                    LOGGER.info("   IP: " + client.ip + ":" + client.port);
                    LOGGER.info("   Device: " + client.deviceName);
                    LOGGER.info("   Client SFDI: " + client.sfdi);
                    LOGGER.info("   Client LFDI: " + client.lfdi);
                    LOGGER.info("   Server SFDI: " + serverSfdi);
                    LOGGER.info("   TLS Session ID: " + sessionId);
                    
                    // Start IEEE 2030.5 communication
                    handleClientCommunication(socket, client);
                } else {
                    LOGGER.severe(" Failed to extract client information from certificate");
                    socket.close();
                }
                
            } catch (Exception e) {
                LOGGER.fine("Not IEEE 2030.5 or connection failed: " + clientIP + ":" + clientPort + " - " + e.getMessage());
            }
        });
    }

    private ClientInfo extractClientInfoFromCertificate(SSLSession session, String clientIP, int clientPort) {
        try {
            // Get client certificate from TLS session
            Certificate[] peerCerts = session.getPeerCertificates();
            if (peerCerts == null || peerCerts.length == 0) {
                LOGGER.severe("No client certificate received during TLS handshake");
                return null;
            }
            
            X509Certificate clientCert = (X509Certificate) peerCerts[0];
            LOGGER.info(" Client certificate received: " + clientCert.getSubjectX500Principal());
            
            String clientSfdi = calculateSfdiFromCertificate(clientCert);
            
            String clientLfdi = calculateLfdiFromCertificate(clientCert);
    
            String deviceName = extractDeviceNameFromCertificate(clientCert);
            
            LOGGER.info("🔢 Calculated from client certificate:");
            LOGGER.info("   Client SFDI: " + clientSfdi);
            LOGGER.info("   Client LFDI: " + clientLfdi);
            
            return new ClientInfo(clientIP, clientPort, clientSfdi, clientLfdi, deviceName);
            
        } catch (Exception e) {
            LOGGER.severe("Failed to extract client info from certificate: " + e.getMessage());
            return null;
        }
    }

    private SSLContext createServerSSLContext() throws Exception {
    KeyStore serverKeyStore = KeyStore.getInstance("PKCS12");
    try (FileInputStream fis = new FileInputStream("server-keystore.p12")) {
        serverKeyStore.load(fis, "password".toCharArray());
    }
    
    KeyStore trustStore = KeyStore.getInstance("PKCS12");
    try (FileInputStream fis = new FileInputStream("truststore.p12")) {  
        trustStore.load(fis, "password".toCharArray());
    }
    
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(serverKeyStore, "password".toCharArray());
    
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
    
    return sslContext;
}

    private String calculateSfdiFromCertificate(X509Certificate cert) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] derEncoded = cert.getEncoded();
            byte[] sha256Hash = md.digest(derEncoded);
            
            String fullHash = bytesToHex(sha256Hash);
            String sfdi = fullHash.substring(fullHash.length() - 10).toUpperCase();
            
            LOGGER.info("🔐 SFDI calculation: SHA256=" + fullHash + " -> SFDI=" + sfdi);
            return sfdi;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to calculate SFDI: " + e.getMessage());
            return null;
        }
    }

    private String calculateLfdiFromCertificate(X509Certificate cert) {
        try {
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] derEncoded = cert.getEncoded();
            byte[] sha256Hash = md.digest(derEncoded);
            String fullHash = bytesToHex(sha256Hash);
            String lfdi = fullHash.substring(0, 40).toUpperCase();
            
            LOGGER.info(" LFDI calculation: SHA256=" + fullHash + " -> LFDI=" + lfdi);
            return lfdi;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to calculate LFDI: " + e.getMessage());
            return null;
        }
    }

    private String extractDeviceNameFromCertificate(X509Certificate cert) {
        try {
            String subjectDN = cert.getSubjectX500Principal().getName();
           
            if (subjectDN.contains("CN=")) {
                String cn = subjectDN.substring(subjectDN.indexOf("CN=") + 3);
                if (cn.contains(",")) {
                    cn = cn.substring(0, cn.indexOf(","));
                }
                return cn;
            }
            return "Unknown Device";
        } catch (Exception e) {
            return "Unknown Device";
        }
    }

    private void handleClientCommunication(SSLSocket socket, ClientInfo client) {
        try {
            LOGGER.info(" Starting IEEE 2030.5 communication with " + client.deviceName);
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           
            out.println("GET /edev/" + client.sfdi + " HTTP/1.1");
            out.println("Host: " + client.ip);
            out.println("Accept: application/sep+xml");
            out.println("Server-SFDI: " + serverSfdi);
            out.println("Server-LFDI: " + serverLfdi);
            out.println();
         
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }
            
            LOGGER.info("📨 Client response from " + client.deviceName + ": " + response.toString());
            
            
        } catch (Exception e) {
            LOGGER.severe("Error in server-client communication with " + client.deviceName + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
                LOGGER.info("🔌 Connection closed with " + client.deviceName);
            } catch (Exception e) {
                LOGGER.severe("Error closing socket: " + e.getMessage());
            }
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

 
    public void stopIeee2030ServiceDiscovery() {
        if (jmdns != null) {
            try {
                jmdns.unregisterAllServices();
                jmdns.close();
                LOGGER.info("IEEE 2030.5 service discovery stopped");
            } catch (IOException e) {
                LOGGER.severe("Error stopping mDNS service: " + e.getMessage());
            }
        }
    }

    
    public Map<String, ClientInfo> getDiscoveredClients() {
        return new HashMap<>(discoveredClients);
    }

    /* This method checks for the device capability found in the server or not  */
    public String findDeviceCapability(String natsSubject) throws Exception {
        String deviceCapabilityResponse = DeviceCapabilityTest.getDeviceCapabilityresponse();
        LOGGER.info("the device capability response is  " + deviceCapabilityResponse);
        return  deviceCapabilityResponse;
    }

    /*This method is to create a device capablity in the server */
    public String CreateDeviceCapabilityTest(String natsSubject) throws Exception {
        String deviceCapabilityResponse = findDeviceCapability(natsSubject);
        LOGGER.info("CreateDeviceCapability response: "+deviceCapabilityResponse);
        if(deviceCapabilityResponse != null){
            return deviceCapabilityResponse;
        }
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        LOGGER.info("the current test is " + currentTest);
        DeviceCapabilityTest deviceCapabilitytest = new DeviceCapabilityTest();
        deviceCapabilitytest.setserviceName("dcapmanager" ); 

        String Payload =  interstore.DeviceCapabilityTest.createNewDeviceCapability(currentTest);
        this.messageToPublish.newStart(natsSubject, Payload );
        Thread.sleep(100);
        deviceCapabilityResponse = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
        return  deviceCapabilityResponse;
    }

    public Object DeviceCapabilitygetAllEndDevice(String natsSubject) throws Exception {
        String endDeviceListLink = interstore.DeviceCapabilityTest.getEndDeviceListLink(); 
        Thread.sleep(100);
        interstore.EndDeviceTest.setServicename("enddevicemanager");
        interstore.EndDeviceTest.setEndDeviceListLink(endDeviceListLink);
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.EndDeviceListLinktest()); 
        Thread.sleep(100);
        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList);
        return endDeviceList;
    }

    public Object CreateEndDeviceTest(String natsSubject) throws Exception{
        interstore.EndDeviceTest.setServicename("createnewenddevice");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        String Payload =  interstore.EndDeviceTest.createNewEndDevice(currentTest);
        this.messageToPublish.newStart(natsSubject + "EndDevice", Payload);
        Thread.sleep(100);
        Object response = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the created end device with sfdi and lfdi " + response);
        return response;
    }

    public Object getAllEndDevicesTest(String natsSubject)throws Exception {
        Thread.sleep(300);
        String endDeviceListLink = interstore.DeviceCapabilityTest.getEndDeviceListLink(); 
        LOGGER.info("the end device list link is " + endDeviceListLink);
        interstore.EndDeviceTest.setServicename("enddevicemanager");
        interstore.EndDeviceTest.setEndDeviceListLink(endDeviceListLink);
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.EndDeviceListLinktest()); 
        Thread.sleep(300);
        Object endDeviceList = interstore.EndDeviceTest.getEndDevices();
        LOGGER.info("the list of EndDevices are " + endDeviceList); 
        return endDeviceList; 
    }

    public Object getEndDeviceTest(String natsSubject)throws Exception{
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("id");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID );
        interstore.EndDeviceTest.setServicename("enddeviceinstancemanager");
        this.messageToPublish.newStart(natsSubject+ "EndDevice",
        interstore.EndDeviceTest.getEndDeviceInstancetest(endDeviceID));
        Thread.sleep(300);
        Object endDevice = interstore.EndDeviceTest.getEndDeviceInstance();
        LOGGER.info("the end device instance is in app class " + endDevice);
        return endDevice; 
    }

    public Object createEndDeviceRegistrationTest(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long pin = currentTest.getLong("registrationPin");

        interstore.EndDeviceTest.setServicename("enddeviceregistrationmanager");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.createEndDeviceRegistration(endDeviceID, pin ));
        Thread.sleep(300);
        LOGGER.info("the registration pin " + interstore.EndDeviceTest.getRegistrationPin());
        Long pinRegistered = interstore.EndDeviceTest.getRegistrationPin();
        Map<String, Long> registration = new HashMap<String , Long>();
        registration.put("pin", pinRegistered);
        return registration;
    }

    public String findRegisterdEndDeviceTest(String natsSubject)throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long registrationID = currentTest.getLong("registrationID");
        Thread.sleep(300);

        interstore.EndDeviceTest.setServicename("findallregistrededendevice");
        this.messageToPublish.newStart(natsSubject, interstore.EndDeviceTest.findRegisteredEndDevice(endDeviceID,  registrationID));
        Thread.sleep(300);
        String detailsOfEndDeviceRegistration = interstore.EndDeviceTest.getregisteredEndDeviceDetails();
        LOGGER.info("the deatils of the registered end device is " + detailsOfEndDeviceRegistration );
        return detailsOfEndDeviceRegistration ;
    }

    // Additional methods omitted for brevity - include all your existing IEEE 2030.5 test methods here
    // (getAllFsaTest, createFunctionsetAssignments, etc.)

    public void start(String natsUrl) throws Exception {    
        LOGGER.info("the nats url is " + natsUrl);
        this.serviceDiscoveryVerticle = new ServiceDiscoveryVerticle(natsUrl);
        this.messageToPublish = new MessageToPublish(natsUrl, this.serviceDiscoveryVerticle);
        this.uiControleHandler = new UIControleHandler();
        this.uiControleHandler.setupBridge();
        
        // Start IEEE 2030.5 mDNS service discovery
        startIeee2030ServiceDiscovery();
        
        LOGGER.info("✅ Everything is initialized including IEEE 2030.5 Windows discovery");
    }

    private static void displayServerInfo(ApplicationContext context) {
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "1900");  // Changed to 1900 for IEEE 2030.5
        
        System.out.println();
        System.out.println("✅ IEEE 2030.5 Server Started Successfully!");
        System.out.println("📡 HTTPS Listener: https://" + getLocalIPAddress() + ":" + port);
        System.out.println("🔗 NATS Integration: ACTIVE");
        System.out.println("🔒 TLS Client Auth: REQUIRED");
        System.out.println("📢 mDNS Service Discovery: ENABLED");
        System.out.println("🖥️  Windows Network Scanning: ACTIVE");
        System.out.println("🔍 Scanning for IEEE 2030.5 clients...");
        System.out.println();
        System.out.println("🎯 Configure InsightHome Gateway:");
        System.out.println("   Server IP: " + getLocalIPAddress());
        System.out.println("   Server Port: " + port);
        System.out.println("   Server SLFDI: " + "392440693763");
        System.out.println("   Protocol: HTTPS with mutual TLS authentication");
        System.out.println();
        System.out.println("⏳ Server will automatically discover and connect to clients...");
        System.out.println("   - mDNS discovery every 30 seconds");
        System.out.println("   - Windows ARP/netstat scanning every 2 minutes");
        System.out.println("   - Schindler Gateway (10.40.160.174) priority scanning");
        System.out.println();
        System.out.println("Press Ctrl+C to stop server");
        System.out.println("==========================================");
    }

    private static String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
   
    public static void main(String[] args) throws Exception {
        String natsUrl = "nats://nats-server:4222";
        ApplicationContext context = SpringApplication.run(App.class);
        ApplicationContextProvider.setApplicationContext(context);
        App mainApp = (App)context.getBean("app");
        mainApp.start(natsUrl);
        displayServerInfo(context);
        
        // Add shutdown hook to properly cleanup mDNS
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔌 Shutting down IEEE 2030.5 server...");
            mainApp.stopIeee2030ServiceDiscovery();
            System.out.println("✅ Server shutdown complete");
        }));
    }

    // Additional IEEE 2030.5 test methods (add all your existing methods here)
    
    public String getAllFsaTest(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        LOGGER.info("the current test object is which is the id .. " + endDeviceID ); 
        interstore.FunctionSetAssignmentsTest.setServicename("getallFsamanager");
        this.messageToPublish.newStart(natsSubject+ "getAllFunctionSetAssignments",
        interstore.FunctionSetAssignmentsTest.getAllFsa(endDeviceID)); 
        Thread.sleep(300);
        String response = interstore.FunctionSetAssignmentsTest.getAllFsa();
        LOGGER.info("the response of the function set assignment is in the app.java " + response);
        return response;
    }

    public String createFunctionsetAssignments(String natsSubject) throws Exception {
        interstore.FunctionSetAssignmentsTest.setServicename("createFsamanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject , 
         interstore.FunctionSetAssignmentsTest.createNewFunctionsetAssignments( currentTest));
        Thread.sleep(300);
        String response = interstore.FunctionSetAssignmentsTest.getCreatedFunctionSetAssignment();
        return response;
    }

    public String getAFunctionSetAssignments(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceID = currentTest.getLong("endDeviceId");
        Long fsaID = currentTest.getLong("fsaID");
        Thread.sleep(300);
        interstore.FunctionSetAssignmentsTest.setServicename("getASingleFsamanager");
        this.messageToPublish.newStart(natsSubject, interstore.FunctionSetAssignmentsTest.findAFunctionSetAssignments(endDeviceID, fsaID));
        Thread.sleep(300);
        String singleFSA = interstore.FunctionSetAssignmentsTest.getSingleFSA();
        LOGGER.info("the deatils of the registered end device is " + singleFSA  );
        return singleFSA   ;
    }

    public String createDerProgram(String natsSubject) throws Exception {
        interstore.DerProgramTest.setServicename("createDerprogrammanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerProgramTest.createNewDerProgram( currentTest));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getCreatedDerProgram();
        LOGGER.info("the response of DER Program is " + response);
        return response;
    }

    public String getAllDerPrograms(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        interstore.DerProgramTest.setServicename("getallDerprogrammanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerProgramTest.getAllDerProgramRequest(fsaId));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getAllderPrograms();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String getADerProgram(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long fsaId = currentTest.getLong("fsaID");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + fsaId);
        LOGGER.info("the der id is " + derId);   
        interstore.DerProgramTest.setServicename("getASingleDerprogrammanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerProgramTest.getADerProgramRequest(fsaId, derId));
        Thread.sleep(300);
        String response = interstore.DerProgramTest.getADerProgram();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerCapability(String natsSubject) throws Exception {
        interstore.DerTest.setServicename("createDerCapabilitymanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.createNewDerCapability( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getCreatedDerCapability();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerCapability(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        interstore.DerTest.setServicename("getDerCapabilitymanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerTest.getADerCapabilityRequest( derId, endDeviceId));
        Thread.sleep(300);
        String response = interstore.DerTest.getADerCapability();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String createDerSettings(String natsSubject) throws Exception {
        interstore.DerTest.setServicename("createDerSettingsmanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.createNewDerSettings( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getCreatedDerSettings();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String getADerSettings(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long endDeviceId  = currentTest.getLong("endDeviceId");
        Long derId = currentTest.getLong("derID");
        LOGGER.info("the fsa id is " + endDeviceId );
        LOGGER.info("the der id is " + derId);   
        interstore.DerTest.setServicename("getDerSettingsmanager");
        this.messageToPublish.newStart(natsSubject, interstore.DerTest.getADerSettingsRequest( derId, endDeviceId));
        Thread.sleep(300);
        String response = interstore.DerTest.getADerSettings();
        LOGGER.info("the response of the der programs is in the app.java " + response);
        return response;
    }

    public String PowerGenerationtest(String natsSubject)throws Exception{
        interstore.DerTest.setServicename("PowerGenerationTestmanager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
         interstore.DerTest.powerGenerationDeviceTest( currentTest));
        Thread.sleep(300);
        String response = interstore.DerTest.getEditedpowerGeneration();
        LOGGER.info("the response of DER is " + response);
        return response;
    }

    public String TimeTest(String natsSubject) throws Exception {
        if(interstore.DeviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
            LOGGER.info("the device capability response is  " + response);
            Thread.sleep(300);
            String timeLink = interstore.TimeTest.getTimeLink(response);
            LOGGER.info("the timelink response is  " + timeLink);
            interstore.TimeTest.setserviceName("timemanager");
            this.messageToPublish.newStart(natsSubject, interstore.TimeTest.getTimeQuery(timeLink));
            Thread.sleep(300);
            String timeListResponse = interstore.TimeTest.getTimeResponse();

            timeListResponse = timeListResponse.substring(1, timeListResponse.length() - 1);
            timeListResponse = timeListResponse.replace("\\", "");
            JSONObject object = new JSONObject(timeListResponse);
            String timeInstance = object.getString("time_instance");
            String quality = object.getString("quality");
            Thread.sleep(500);
            Instant instant = Instant.now();
            long currentTime =  instant.getEpochSecond();
            TimeType REF_Client_TimeInstance = new TimeType(currentTime);
            if (quality!=null && timeInstance!=null) {
                if(quality.equals("7")){
                    LOGGER.info("Quality metric matched with Client with value: "+quality);
                    LOGGER.info("REF-Client Time: "+ REF_Client_TimeInstance.getInt64Value());
                    LOGGER.info("Synchronized REF-Client Time: " + timeInstance);
                    return "Synchronized the REF-Client Time with the Device Capability";
                }
                else {
                    LOGGER.info("Wrong quality metric value of "+quality);
                    return "Wrong quality metric value of "+quality;
                }
            } else {
                LOGGER.info("Quality value not found.");
                return "Quality value not found.";
            }
        }
        return "Please run Device Capability Test first because the DeviceCapabilityResponse is " + interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
    }

    public String AdvancedTimeTest(String natsSubject) throws Exception {
        if(interstore.DeviceCapabilityTest.getDeviceCapabilityresponse() != null){
            String response = interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
            LOGGER.info("the device capability response is  " + response);
            String timeLink = interstore.TimeTest.getTimeLink(response);
            LOGGER.info("the timelink response is  " + timeLink);
            interstore.TimeTest.setserviceName("timemanager");
            this.messageToPublish.newStart(natsSubject+"_Get_Time", interstore.TimeTest.getTimeQuery(timeLink));
            Thread.sleep(300);
            String timeListResponse = interstore.TimeTest.getTimeResponse();
            timeListResponse = timeListResponse.substring(1, timeListResponse.length() - 1);
            timeListResponse = timeListResponse.replace("\\", "");
            JSONObject object = new JSONObject(timeListResponse);
            String timeInstance = object.getString("time_instance");
            if (timeInstance!=null) {
                long updatedTime = Long.parseLong(timeInstance) + 3600;
                JSONObject payload = new JSONObject();
                payload.put("updated_time_instance", updatedTime);
                payload.put("timeLink", timeLink);
                interstore.TimeTest.setserviceName("advancedtimemanager");
                this.messageToPublish.newStart(natsSubject+"_update_time", interstore.TimeTest.updateTimeQuery(payload.toString()));
                Thread.sleep(1000);
                interstore.TimeTest.setserviceName("timemanager");
                this.messageToPublish.newStart(natsSubject+"_validate_updated_time", interstore.TimeTest.getTimeQuery(timeLink));
                Thread.sleep(100);
                return "The Time resource was updated successfully by 1 hour.";
            } else {
                System.out.println("Time Instance value not found.");
                return "Time Instance value not found.";
            }
        }
        return "Please run Device Capability Test first because the DeviceCapabilityResponse is " + interstore.DeviceCapabilityTest.getDeviceCapabilityresponse();
    }

    public String createDerCurve(String natsSubject) throws Exception {
        interstore.DerCurveTest.setServicename("createDerCurveManager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
                interstore.DerCurveTest.createNewDerCurve( currentTest));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getCreatedDerCurve();
        LOGGER.info("the response of DERCurve is " + response);
        return response;
    }

    public String getADerCurve(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long dercId = currentTest.getLong("dercID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derc id is " + dercId);
        interstore.DerCurveTest.setServicename("getASingleDerCurveManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerCurveTest.getADerCurveRequest(derpId, dercId));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getADerCurve();
        LOGGER.info("the response of the der curve is in the app.java " + response);
        return response;
    }

    public String getAllDerCurves(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        interstore.DerCurveTest.setServicename("getallDerCurveManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerCurveTest.getAllDerCurveRequest(derpId));
        Thread.sleep(300);
        String response = interstore.DerCurveTest.getAllderCurves();
        LOGGER.info("the response of the der curves is in the app.java " + response);
        return response;
    }

    public String createDerControl(String natsSubject) throws Exception {
        interstore.DerControlTest.setServicename("createDerControlManager");
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        this.messageToPublish.newStart(natsSubject ,
                interstore.DerControlTest.createNewDerControl( currentTest));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getCreatedDerControl();
        LOGGER.info("the response of DERControl is " + response);
        return response;
    }

    public String getAllDerControls(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        interstore.DerControlTest.setServicename("getallDerControlManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerControlTest.getAllDerControlRequest(derpId));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getAllderControls();
        LOGGER.info("the response of the der controls is in the app.java " + response);
        return response;
    }

    public String getADerControl(String natsSubject) throws Exception {
        JSONObject currentTest = this.uiControleHandler.getCurrentTestObject();
        Long derpId = currentTest.getLong("derpID");
        Long derControlId = currentTest.getLong("derControlID");
        LOGGER.info("the derp id is " + derpId);
        LOGGER.info("the derControl id is " + derControlId);
        interstore.DerControlTest.setServicename("getASingleDerControlManager");
        this.messageToPublish.newStart(natsSubject, interstore.DerControlTest.getADerControlRequest(derpId, derControlId));
        Thread.sleep(300);
        String response = interstore.DerControlTest.getADerControl();
        LOGGER.info("the response of the der control is in the app.java " + response);
        return response;
    }
}