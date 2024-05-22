package interstore.Types;
import java.util.logging.Logger; 

public class PinType  extends UInt32{
    private static final Logger LOGGER = Logger.getLogger(PinType .class.getName());
     private long value;
    public PinType(long value) {
        super(value);
       
    }
    
    public long getValue(){
       // LOGGER.info("passing here  in PIN TYpe"); 
       // LOGGER.info("PIN TYpe value is " + super.getValue());
        return  super.getValue();
    }
    public void setValue(long value){
       
        this.value = value;
    }


}

// there is need to impliment the checksum of the pin type leave it for next week 