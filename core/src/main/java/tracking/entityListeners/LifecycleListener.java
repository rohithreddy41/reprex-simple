package tracking.entityListeners;

import tracking.annotations.TrackedProperties;
import tracking.annotations.TrackedProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.NotificationConstants;


import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public abstract class LifecycleListener {

    private static final ObjectMapper mapper = new ObjectMapper();




    @Resource(mappedName = "java:/" + NotificationConstants.CONNECTION_FACTORY_NAME)
    private TopicConnectionFactory topicConnectionFactory;


//    @Inject
//    @LifecycleTopic
//    private TopicPublisher publisher;
//
//    @Inject
//    @LifecycleTopic
//    private TopicSession session; org.jboss.spec.javax.jms

    protected void sendEvent(LifecycleEvent event, NotificationConstants.LifecycleAction action) {
        sendEvent(event, action, 0);
    }

    protected void sendEvent(LifecycleEvent event, NotificationConstants.LifecycleAction action, long deliveryDelay) {
        if (topicConnectionFactory == null) {
            // running in an SE environment.
            return;
        }

        try (TopicConnection connection = topicConnectionFactory.createTopicConnection();
             TopicSession session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
             TopicPublisher publisher = session.createPublisher(session.createTopic("lifecycle"))) {
            publisher.setDeliveryDelay(deliveryDelay);

            String messageBody = mapper.writeValueAsString(event);
            //logger.info("Sending event {} for action type {}", messageBody, action);

            TextMessage tm = session.createTextMessage(messageBody);
            tm.setStringProperty(NotificationConstants.LIFECYCLE_ENTITY_TYPE_PROPERTY, event.getType());
            tm.setStringProperty(NotificationConstants.LIFECYCLE_ACTION_PROPERTY, action.name());
            publisher.send(tm);
        } catch (JsonProcessingException e) {
           // logger.warn("Unable to parse event to json when trying to send JMS message", e);
        } catch (JMSException e) {
            //logger.warn("Unable to send JMS message", e);
        } catch (Exception e) {
            //logger.warn("Probably running in an SE environment during testing", e);
        }

    }

  protected   Set<ChangedProperty> buildChangeList(Class<?> c, Object entity) {
        HashSet<ChangedProperty> changeList = new HashSet<>();

        TrackedProperties properties = c.getAnnotation(TrackedProperties.class);
        if (properties == null || properties.properties().length == 0) {
            return changeList;
        }
        for (TrackedProperty property : properties.properties()) {
            try {

                Field oldPropertyName = c.getDeclaredField(property.oldPropertyName());
                Field propertyName = c.getDeclaredField(property.propertyName());
                oldPropertyName.setAccessible(true);
                propertyName.setAccessible(true);
                Object oldValue = oldPropertyName.get(entity);
                Object newValue = propertyName.get(entity);
                if (!Objects.equals(oldValue, newValue)) {
                    changeList.add(new ChangedProperty(property.propertyName(), oldValue, newValue));
                }

            } catch (Exception e) {
                //logger.warn("Unable to retrieve the changed properties {}", e.getMessage());
            }
        }
        return changeList;
    }

    // The annotation could be defined on a super class, if so then return that so we have access to the properties.
    // If no annotation is found, return the original class.
    protected Class<?> findTrackedPropertyAnnotationClass(Class<?> type) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            if (c.getAnnotation(TrackedProperties.class) != null) {
                return c;
            }
        }
        return type;
    }
}
