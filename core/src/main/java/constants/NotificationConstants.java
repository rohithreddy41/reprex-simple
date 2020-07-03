package constants;

/**
 * User: bven
 * Date: 4/27/16.
 */
public final class NotificationConstants {

    public static final String LIFECYCLE_ENTITY_TYPE_PROPERTY = "type";
    public static final String LIFECYCLE_ACTION_PROPERTY = "action";
    public static final String LIFECYCLE_TOPIC_NAME = "jms/topic/lifecycle";
    public static final String TASK_QUEUE_NAME = "jms/queue/TASKQueue";
    public static final String SLA_QUEUE_NAME = "jms/queue/SLAQueue";
    public static final String TASK_TYPE_PROPERTY = "type";

    public static final String CONNECTION_FACTORY_NAME = "RemoteFuseJMS";


    private NotificationConstants() {
    }

    public enum LifecycleAction {CREATE, UPDATE, DELETE}
}
