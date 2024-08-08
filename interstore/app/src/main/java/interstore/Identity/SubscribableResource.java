package interstore.Identity;

import interstore.Types.SubscribableType;

public class SubscribableResource extends Resource{
    private SubscribableType subscribable;

     public SubscribableResource(String href, SubscribableType subscribable) {
        super(href);
        this.subscribable = subscribable;
    }

    
    public SubscribableResource() {

    }

    public SubscribableType isSubscribable() {
        return subscribable;
    }

    public void setSubscribable(SubscribableType subscribable) {
        this.subscribable = subscribable;
    }
}
