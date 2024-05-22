package interstore.Identity;

public class SubscribableIdentifiedObject extends IdentifiedObject {
    private SubscribableResource subscribableResource; // Composition

    // Constructor for SubscribableIdentifiedObject
    public SubscribableIdentifiedObject(byte mRID, String version, String description, SubscribableResource subscribableResource) {
        super(mRID, version, description);
        this.subscribableResource = subscribableResource;
    }

    public SubscribableResource getSubscribableResource() {
        return subscribableResource;
    }

    public void setSubscribableResource(SubscribableResource subscribableResource) {
        this.subscribableResource = subscribableResource;
    }
    
}
