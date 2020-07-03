package tracking.entityListeners;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User: bven
 * Date: 4/26/16.
 */
public class UpdateEvent extends LifecycleEvent implements Serializable {
    private Set<ChangedProperty> changeList = new HashSet<>();

    public UpdateEvent() {
        super();
    }

    public UpdateEvent(Object id, String entityType) {
        super(id, entityType);
    }

    public Set<ChangedProperty> getChangeList() {
        return changeList;
    }

    public void setChangeList(Set<ChangedProperty> changeList) {
        this.changeList = changeList;
    }
}
