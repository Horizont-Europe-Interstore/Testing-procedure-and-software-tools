package interstore.DERProgram;

import java.util.logging.Logger;

public class DERPListManager {
    DERPListImpl derpListImpl;
    private static final Logger LOGGER = Logger.getLogger(DERPListManager.class.getName());
    public DERPListManager(DERPListImpl derpListImpl) {
        this.derpListImpl = derpListImpl;
    }

}
