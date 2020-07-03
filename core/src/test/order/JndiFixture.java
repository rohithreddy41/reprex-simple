package order;



import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

/**
 * User: bven
 * Date: 4/28/16.
 */
public class JndiFixture implements Closeable {

   // static final Logger logger = LoggerFactory.getLogger(JndiFixture.class);

    private final String hostName;
    private final String httpPort;
    private final String managementPort;
    private final String userName;
    private final String password;

    public JndiFixture() {

        // The 'remote.*' is there for backwards compatibility
        hostName = System.getProperty(TestConstants.JBOSS_HOST, System.getProperty(
                "remote.connection.default.host",
                "127.0.0.1"));
        httpPort = System.getProperty(TestConstants.JBOSS_PORT_HTTP, System.getProperty("remote.connection.default.port", "8080"));
        userName = System.getProperty(TestConstants.JBOSS_USER, System.getProperty(
                "remote.connection.default.username",
                "nsfsapp"));
        password = System.getProperty(TestConstants.JBOSS_PASSWORD, System.getProperty(
                "remote.connection.default.password",
                "!nsfsapp1"));
        managementPort = System.getProperty(TestConstants.JBOSS_PORT_MANAGEMENT, "9990");
    }

    public <T> T lookup(String name) throws NamingException {
        Context context = getJNDIContext();
        try {
            return (T) context.lookup(name);
        } finally {
            close(context);
        }
    }

    public void close(Context context) {
        try {
            context.close();
        } catch (NamingException e) {
           // logger.error(e.getMessage());
        }
    }

    public Context getJNDIContext()
            throws NamingException {
        return getJNDIContextForUser(userName, password);
    }

    public Context getJNDIContextForUser(String user, String password)
            throws NamingException {

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", hostName, Integer.parseInt(httpPort)));
        props.put(Context.SECURITY_PRINCIPAL, user);
        props.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialContext(props);
    }


    @Override
    public void close() throws IOException {
//        if (EJBClientContext.getCurrent() != null) {
//            EJBClientContext.getCurrent().getConfiguredConnections().get(0).;
//        }
    }

    public String getHostName() {
        return hostName;
    }

    public String getHttpPort() {
        return httpPort;
    }

    public String getManagementPort() {
        return managementPort;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
