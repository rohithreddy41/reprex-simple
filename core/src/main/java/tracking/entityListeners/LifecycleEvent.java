package tracking.entityListeners;

import java.io.Serializable;
import java.util.Objects;

/**
 * User: bven
 * Date: 4/26/16.
 */
public abstract class LifecycleEvent implements Serializable {

    private String id;
    private String type;
    private int retryCount = 0;
    private int retryFragmentIndex = 0; // needed for SO and SOCN
    private int retryFragmentSize = -1; // needed for SO and SOCN

    // Needed for parsing
    public LifecycleEvent() {
    }

    public LifecycleEvent(Object id, String type) {
        setId(id);
        setType(type);
    }

    public String getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = Objects.toString(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryFragmentIndex() {
        return retryFragmentIndex;
    }

    public void setRetryFragmentIndex(int retryFragmentIndex) {
        this.retryFragmentIndex = retryFragmentIndex;
    }

    public int getRetryFragmentSize() {
        return retryFragmentSize;
    }

    public void setRetryFragmentSize(int retryFragmentSize) {
        this.retryFragmentSize = retryFragmentSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LifecycleEvent that = (LifecycleEvent) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
