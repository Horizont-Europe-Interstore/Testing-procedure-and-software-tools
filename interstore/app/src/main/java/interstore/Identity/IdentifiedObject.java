package interstore.Identity;

// the version here comes to play , this vesion
// can be a type . now treating it like a string
public class IdentifiedObject {
    private byte mRID;
    private String version;
    private String description;

   public IdentifiedObject(byte mRID, String version, String description){
       this.mRID = mRID;
       this.version = version;
       this.description = description;
   }

    public IdentifiedObject(){

    }

   public byte getMRID(){
       return mRID;
   }
   public String getVersion(){
       return version;
   }
   public String getDescription(){
     return description;
   }
  // setter for the above 
    public void setMRID(byte mRID){
        this.mRID = mRID;
    }
    public void setVersion(String version){
        this.version = version;
    }
    public void setDescription(String description){
        this.description = description;
    }



    
}
