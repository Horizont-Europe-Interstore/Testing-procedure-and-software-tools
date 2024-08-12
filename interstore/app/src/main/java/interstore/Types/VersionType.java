package interstore.Types;
import jakarta.persistence.Embeddable;
@Embeddable
public class VersionType extends UInt16{
    public VersionType(){
        super(0);
    }

    public VersionType(int versionTypeValue) {
        super(versionTypeValue);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
