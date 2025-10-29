package interstore.Identity;
import java.nio.charset.StandardCharsets;
public class Identification {
   
    private String href;
    private long all;
    private byte mRID;
    private int version;
    private boolean subscribable;
    private String results; 
    private String replyTo;
    private byte responseRequired;
   // write a constructor and getter and setter for this class 
   
   public Identification(String href, long all, byte mRID, int version, boolean subscribable, String results, String replyTo, byte responseRequired){
       this.href = href;
       this.all = all;
       this.mRID = mRID;
       this.version = version;
       this.subscribable = subscribable;
       this.results = results;
       this.replyTo = replyTo;
       this.responseRequired = responseRequired;
   }

   public String getHref(){ 
       return this.href;
   }
   public long getAll(){    
       return this.all;
   }
   public byte getMRID(){   
       return this.mRID;
   }
   public int getVersion(){ 
       return this.version;
   }
   public boolean getSubscribable(){    
       return this.subscribable;
   }
   public String getResults(){
       return this.results;
   }
   public String getReplyTo(){
       return this.replyTo;
   }
   public byte getResponseRequired(){
       return this.responseRequired;
   }

 //now setters
   public void setHref(String href){
       this.href = href;
   }
   public void setAll(long all){
    this.all = all;
   }
   public void setMRID(byte mRID){
       this.mRID = mRID;
   }
   public void setVersion(int version){
       this.version = version;
   }
   public void setSubscribable(boolean subscribable){
       this.subscribable = subscribable;
   }
   public void setResults(String results){
       this.results = results;
   }
   public void setReplyTo(String replyTo){
       this.replyTo = replyTo;
   }
   public void setResponseRequired(byte responseRequired){
       this.responseRequired = responseRequired;
   }

  



}


