package interstore.EndDevice;
import interstore.Types.UInt32;
import java.util.ArrayList;
import java.util.List;
public class EndDeviceList {

    public UInt32 pollRate = new UInt32(900);
    private static EndDeviceList instance;
    private static List<EndDeviceDto> endDevices = new ArrayList<>();
   
    public EndDeviceList() {

    }

    // Public method to get the singleton instance
    public static EndDeviceList getInstance() {
        if (instance == null) {
            instance = new EndDeviceList();
            System.out.println("Hello instance");
        }
        return instance;
    }

    // Add an EndDevice to the list
    public void addEndDevice(EndDeviceDto endDevice) {
        endDevices.add(endDevice);
    }

    // Get all EndDevices in the list
    public List<EndDeviceDto> getEndDevices() {
        return new ArrayList<>(endDevices); // Return a copy to prevent external modifications
    }
}
