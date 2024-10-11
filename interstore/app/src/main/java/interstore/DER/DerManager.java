package interstore.DER;

import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class DerManager {

    private DerService derService;
    private static final Logger LOGGER = Logger.getLogger(DerManager.class.getName());

    public DerManager(DerService derService) {
        this.derService = derService;
    }

}
