package interstore.Identity;
import java.util.logging.Logger;
import java.lang.reflect.Method;


public class ListLink {
  
    private static final Logger LOGGER = Logger.getLogger(ListLink.class.getName()); 
    
    private Link link; 
    

    public ListLink() {
       this.link = new Link(); 
    } 
   
    
    public void setListLink(String Listlink)
    {
       this.link.setLink(Listlink);
    }
   
     public String getListLink()

     {
         return this.link.getLink(); 
     }


    public Object getListLinks(Object object) {
              try{
                  Method getLinksMethod = object.getClass().getMethod("getLinks");
                   Object listLink = getLinksMethod.invoke(object);
                   LOGGER.info("ListLink from listLink class: "+listLink);
                   return  listLink;     
         
               } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                    e.printStackTrace();
                }
                    
             return null; 
           }

}


