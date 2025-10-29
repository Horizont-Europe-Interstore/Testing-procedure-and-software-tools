
package interstore.Identity;
import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "subscribable_resource")
public class SubscribableResourceEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscribable")
    private Short subscribable;

   
    public SubscribableResourceEntity() {
    }

  
    public Short getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(Short subscribable) {
        this.subscribable = subscribable;
    }
}




















/*
 * package interstore.Identity;

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

 * 
 * 
  */
