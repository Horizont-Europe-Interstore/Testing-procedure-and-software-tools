package interstore.Identity;

import java.util.Optional;
import java.util.logging.Logger;

public class Resource {
    private String href;
    private static String fixedHref = "http://localhost/";
    private static final Logger LOGGER = Logger.getLogger(Link.class.getName());
    public Resource(String href) {
        this.href = href;
    }
    public Resource() {

    }

    public String getHref() {
        return href;
    }
   
    public void setHref(Optional<String> endpoint) {
        if (endpoint.isPresent()) {
            this.href = String.format("%s%s", fixedHref, endpoint.get());
        } else {
            this.href = null;
        }
    }
    
 

    
}

/*
 * 
 *     
    public void setHref(String href) {
        this.href = href;
    }

 * 
 * 
 */