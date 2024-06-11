package interstore.Identity;

public class SubscribableResource extends Resource{
    private boolean subscribable;

     public SubscribableResource(String href, boolean subscribable) {
        super(href);
        this.subscribable = subscribable;
    }
    public SubscribableResource() {

    }

    public boolean isSubscribable() {
        return subscribable;
    }

    public void setSubscribable(boolean subscribable) {
        this.subscribable = subscribable;
    }
}
