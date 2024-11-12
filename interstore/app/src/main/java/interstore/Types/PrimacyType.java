package interstore.Types;


/* The Primacy values are 0:In home energy management system 
 * 1: Contracted premises servcie provider 
 * 2: Non-contractual servcie provider 
 * 3-64 : Reserved
 * 65-191 : User Defined 
 * 192-255 : Reserved
 * Lower Number indicate Higher Priortiy 
 */
public class PrimacyType extends UInt8 {
    private short PrimacyTypeValue;
    public PrimacyType(short value) {
        super(value);
        if (value >= 3 && value <= 64) {
            throw new IllegalArgumentException("Values 3 to 64 are reserved");
        }
        if (value >= 192 && value <= 255) {
            throw new IllegalArgumentException("Values 192 to 255 are reserved");
        }
        this.PrimacyTypeValue = value;
    }

    public String getString(){
        return String.valueOf(this.PrimacyTypeValue);
    }
}
