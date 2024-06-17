package interstore.Identity;

import interstore.Types.SubscribableType;
import interstore.Types.UInt32;

public class SubscribableList extends SubscribableResource {
    public UInt32 all;
    public UInt32 results;

    public SubscribableList(String href, SubscribableType subscribable) {
        super(href, subscribable);
    }

    public SubscribableList() {

    }
}
