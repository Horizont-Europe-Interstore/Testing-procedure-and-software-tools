package interstore.Identity;
import java.util.logging.Logger;


public class Link {
   
    private String link;
    private static String fixedLink = "http://ieee.org/2030.5/";
    private static final Logger LOGGER = Logger.getLogger(Link.class.getName());

   
    public String getLink() {
        return link;
    }

    public  void setLink(String endPoint) {
        this.link = String.format("%s%s", fixedLink, endPoint);
    }
    
    


}


