package interstore.Identity;
import interstore.Types.UInt32; 
public class ResourceList {
    private UInt32  all;
    private UInt32  results;

    public ResourceList(UInt32  all, UInt32  results) {
        this.all = all;
        this.results = results;
    }
    public UInt32  getAll() {
        return this.all;
    }
    public UInt32  getResults() {
        return this.results;
    }
    public void setAll(UInt32  all) {
        this.all = all;
    }
    public void setResults(UInt32  results) {
        this.results = results;
    }

}
