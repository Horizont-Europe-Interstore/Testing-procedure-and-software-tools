package interstore.DER;

import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class DERManager {

    private DERImpl derImpl;
    private static final Logger LOGGER = Logger.getLogger(DERManager.class.getName());

    public DERManager(DERImpl derImpl) {
        this.derImpl = derImpl;
    }

}
