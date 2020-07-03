package order;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * User: bven
 * Date: 5/3/16.
 */
public class JmsFixture {

    public final static String JMS_CONNECTION_FACTORY_JNDI = "jms/RemoteConnectionFactory";
    //static final Logger logger = LoggerFactory.getLogger(JmsFixture.class);
    private final JndiFixture jndi;

    public JmsFixture(JndiFixture jndi) {
        this.jndi = jndi;
    }


    public Future<Message> waitForMessage(String dest, String messageSelector) {
        InitialContext context = null;
        FutureTask<Message> futureTask = null;

        try {
            context = getInitialContext();

            final ConnectionFactory jmsConnectionFactory = (ConnectionFactory) context.lookup(JMS_CONNECTION_FACTORY_JNDI);
            final Connection jmsConnection = jmsConnectionFactory.createConnection(jndi.getUserName(), jndi.getPassword());
            final Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination destination = (Destination) context.lookup(dest);

            final MessageConsumer messageConsumer = jmsSession.createConsumer(destination, messageSelector);
            jmsConnection.start();


            futureTask = new FutureTask<>(() -> {
                try {

                    final Message message = messageConsumer.receive(10000);
                    return message;
                } finally {
                    close(messageConsumer);
                    close(jmsSession);
                    close(jmsConnection);
                }
            });


        } catch (Exception e) {
            //logger.warn("Error setting up JMS environment {}", e);
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
        new Thread(futureTask).start();
        return futureTask;
    }

    private InitialContext getInitialContext() throws NamingException{
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "http-remoting://" + System.getProperty(TestConstants.JMS_HOST, "127.0.0.1") + ":" + jndi.getHttpPort());

        props.put(Context.SECURITY_PRINCIPAL, jndi.getUserName());
        props.put(Context.SECURITY_CREDENTIALS, jndi.getPassword());
        //props.put("jboss.naming.client.ejb.context", true);
        return new InitialContext(props);
    }

    public void postMessageOnQueue(String messageBody, String dest){
        InitialContext context = null;
        try {
            context = getInitialContext();

            final ConnectionFactory jmsConnectionFactory = (ConnectionFactory) context.lookup(JMS_CONNECTION_FACTORY_JNDI);
            final Connection jmsConnection = jmsConnectionFactory.createConnection(jndi.getUserName(), jndi.getPassword());
            final Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination destination = (Destination) context.lookup(dest);

            final MessageProducer messageProducer = jmsSession.createProducer(destination);
            Message message = jmsSession.createTextMessage(messageBody);
            messageProducer.send(message);
            close(messageProducer);
            close(jmsSession);
            close(jmsConnection);
        }catch (Exception e) {
            //logger.warn("Error setting up JMS environment to post message {}", e);
        }finally {
            close(context);
        }
    }

    public void close(MessageConsumer messageConsumer) {
        try {
            messageConsumer.close();
        } catch (Exception e) {
            //logger.warn("Exception closing message consumer", e);
        }
    }

    public void close(MessageProducer messageProducer){
        if(messageProducer != null){
            try{
                messageProducer.close();
            }catch (Exception e) {
               // logger.warn("Exception closing message producer", e);
            }
        }
    }

    public void close(Connection jmsConnection) {
        try {
            jmsConnection.close();
        } catch (Exception e) {
           // logger.warn("Exception closing jms connection", e);
        }
    }

    public void close(Session jmsSession) {
        try {
            jmsSession.close();
        } catch (Exception e) {
           // logger.warn("Exception closing jms session", e);
        }
    }

    public void close(InitialContext context){
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                //logger.warn("Error closing context {}", e);
            }
        }
    }


}
