package beans;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Remote(MessageManager.class)
@ApplicationScoped
public class MessageManagerBean implements MessageManager {


    @Resource(mappedName = "java:/" +CONNECTION_FACTORY_NAME)
    private TopicConnectionFactory topicConnectionFactory;

    @Inject
    private JMSContext jmsContext;

    public static final String CONNECTION_FACTORY_NAME = "ConnectionFactory";


    List<TopicSubscriber> subscribers = new ArrayList<>();

    private TopicSession session;

    private Topic topic;
    private TopicConnection connection;


    @Override
    public void addMessage(String messageBody) {
        if (topicConnectionFactory == null) {
            return;
        }
        try  {
            connection = topicConnectionFactory.createTopicConnection();
            session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic("lifecycle");
            TopicPublisher publisher = session.createPublisher(topic);
            publisher.setDeliveryDelay(0l);
            connection.start();
            addSubscribers();
            TextMessage tm = session.createTextMessage(messageBody);
            publisher.send(tm);

        } catch (JMSException e) {
            System.out.println( e);
        } catch (Exception e) {
            System.out.println( e);
        }

    }

    private  void addSubscribers() throws Exception{
        if(session != null && topic != null) {
            TopicSubscriber subscriber1 = session.createSubscriber(topic);
            subscribers.add(subscriber1);
            TopicSubscriber subscriber2 = session.createSubscriber(topic);
            subscribers.add(subscriber2);
        }
        }

    @Override
    public void receive() throws Exception{
            for(TopicSubscriber subscriber : subscribers){
                Message msg = subscriber.receive(5000);
                if (msg == null) {
                    System.out.println("Timed out waiting for msg");
                } else {
                    System.out.println("Received message=" + msg);
                }
            }
        connection.stop();
        session.close();
        connection.close();
    }
}

//https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=appplatform&downloadType=distributions&version=7.1