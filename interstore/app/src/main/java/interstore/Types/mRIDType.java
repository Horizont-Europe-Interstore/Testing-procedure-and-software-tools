package interstore.Types;
import java.math.BigInteger;
import jakarta.persistence.Embeddable;
@Embeddable
public class mRIDType extends HexBinary128 {
    private BigInteger mRIDvalue;

    public mRIDType(String hexValue) {
        super(hexValue);
        this.mRIDvalue = new BigInteger(getMRID(), 16); 
    }

    public void setMRID(String hexValue) {
        super.setHexValue128Value(hexValue);
        this.mRIDvalue = new BigInteger(getMRID(), 16); 
    }

    public String getMRID() {
        return super.getHexValue128Value();
    }


    public BigInteger getMRIDValue() {
        return mRIDvalue;
    }

    @Override
    public String toString() {
        return getMRID();
    }
}


