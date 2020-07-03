package tracking.mdb;

import constants.NotificationConstants;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = NotificationConstants.LIFECYCLE_TOPIC_NAME),
        /*@ActivationConfigProperty(propertyName = "messageSelector", propertyValue =
                "(" +
                        NotificationConstants.LIFECYCLE_ENTITY_TYPE_PROPERTY + "='domain.Order')"),*/
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "AgencyOrderLifeCycleMDB"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "AgentOrderStateChange"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "10")
})
public class AgencyOrderLifeCycleMDB implements MessageListener {

    @Inject
    private MDBMessageTracker mdbMessageTracker;

    @Override
    public void onMessage(Message message)  {
        mdbMessageTracker.increseMessageReceivedCount();
    }
}





