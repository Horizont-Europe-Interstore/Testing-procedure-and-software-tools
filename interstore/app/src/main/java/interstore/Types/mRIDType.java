package interstore.Types;
import java.math.BigInteger;
import jakarta.persistence.Embeddable;
@Embeddable
public class mRIDType extends HexBinary128 {
    private BigInteger value;

    public mRIDType(String hexValue) {
        super(hexValue);
        this.value = new BigInteger(getMRID(), 16); 
    }

    public void setMRID(String hexValue) {
        super.setHexValue(hexValue);
        this.value = new BigInteger(getMRID(), 16); 
    }

    public String getMRID() {
        return super.getHexValue();
    }


    public BigInteger getMRIDValue() {
        return value;
    }

    @Override
    public String toString() {
        return getMRID();
    }
}


