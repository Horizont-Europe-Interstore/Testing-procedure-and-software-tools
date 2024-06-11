package interstore.Types;

public class UInt40 {
    private  long value;

    
    public void  setUint40(long value) {
        if (value < 0 || value > 0xFFFFFFFFFFFL) {
            throw new IllegalArgumentException("Value must be between 0 and 1099511627775");
        }
        this.value = value;
    }
   

    public long getUint40() {
        return this.value;
    } 

    @Override
    public String toString() {
        return Long.toString(value);
    }
}



 