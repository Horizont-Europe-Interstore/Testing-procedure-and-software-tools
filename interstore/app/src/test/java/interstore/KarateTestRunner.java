package interstore;

import com.intuit.karate.junit5.Karate;

class KarateTestRunner {
    
    @Karate.Test
    Karate testDeviceCapability() {
        return Karate.run("device-capability").relativeTo(getClass());
    }
}