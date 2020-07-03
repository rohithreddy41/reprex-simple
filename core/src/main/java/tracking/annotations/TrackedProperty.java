package tracking.annotations;

/**
 * User: bven
 * Date: 4/22/16.
 */
public @interface TrackedProperty {
    String oldPropertyName();

    String propertyName();
}
