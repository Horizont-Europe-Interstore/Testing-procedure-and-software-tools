package interstore.Types;

public class UInt40 {
    private  long UInt40Value;

    
    public void  setUint40(long value) {
        if (value < 0 || value > 0xFFFFFFFFFFFL) {
            throw new IllegalArgumentException("Value must be between 0 and 1099511627775");
        }
        this.UInt40Value = value;
    }
   

    public long getUint40() {
        return this.UInt40Value;
    } 

    @Override
    public String toString() {
        return Long.toString(UInt40Value);
    }
}



 