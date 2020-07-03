package tracking.entityListeners;

import java.io.Serializable;
import java.util.Objects;

/**
 * User: bven
 * Date: 4/26/16.
 */
public class ChangedProperty implements Serializable {
    private String propertyName;
    private Object oldValue;
    private Object newValue;

    // Needed for serialization
    public ChangedProperty() {
    }

    public ChangedProperty(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangedProperty that = (ChangedProperty) o;
        return Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName);
    }
}
