package order;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.dmr.ModelNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import static org.junit.Assert.fail;

/**
 * User: bven
 * Date: 5/17/16.
 */
public abstract class AbstractIntegrationFixture {

   // protected static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationFixture.class);
    protected static ModelControllerClient client;
    private static JndiFixture jndi;
    private static JmsFixture jms;
    @Rule
    public TestName testName = new TestName();

    @AfterClass
    public static void cleanUpAbstractIntegrationFixture() {
        try {
            close(jndi);
            close(client);
            jndi = null;
            client = null;
        } catch (Exception e) {
           // logger.trace("Exception cleaning test fixture", e);
        }
    }

    @BeforeClass
    public static void initAbstractIntegrationFixture() {
        jndi = new JndiFixture();
        jms = new JmsFixture(jndi);
        createJbossClient();
        waitTillJbossStarted();
    }

    public static void createJbossClient() {
      //  logger.trace("Creating jboss client <{}>", client);
        if (client == null) {
            try {
                client = ModelControllerClient.Factory.create(jndi.getHostName(), Integer.valueOf(jndi.getManagementPort()), new CallbackHandler() {
                    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                        for (Callback current : callbacks) {
                            if (current instanceof NameCallback) {
                                NameCallback ncb = (NameCallback) current;
                                ncb.setName(jndi.getUserName());
                            } else if (current instanceof PasswordCallback) {
                                PasswordCallback pcb = (PasswordCallback) current;
                                pcb.setPassword(jndi.getPassword().toCharArray());
                            } else if (current instanceof RealmCallback) {
                                RealmCallback rcb = (RealmCallback) current;
                                rcb.setText(rcb.getDefaultText());
                            } else {
                                throw new UnsupportedCallbackException(current);
                            }
                        }
                    }

                });
            } catch (UnknownHostException e) {
                fail("Unable to resolve jboss host " + e.getMessage());
            }
        }
    }

    public static void waitTillJbossStarted() {
        boolean isRunning = false;
        int count = 0;
        while (!isRunning && count++ < 5) {
            try {

                ModelNode operation = Operations.createReadAttributeOperation(new ModelNode().setEmptyList(), "server-state");
                ModelNode result = client.execute(operation);
                if (Operations.isSuccessfulOutcome(result)) {
                   // logger.trace("Success {} ", result);
                    isRunning = result.get("result").asString().equals("running");
                } else {
                   // logger.trace("Failure! {} result {}", Operations.getFailureDescription(result), result);
                    try {
                        Thread.sleep(2000 * (count + 1));
                    } catch (InterruptedException ignore) {
                        // ignore
                    }
                }

            } catch (IOException e) {
                //logger.trace("Can't execute operation request-->{}", e.getMessage(), e);
            }
        }
    }

    protected static void close(Closeable fixture) {
        if (fixture != null) {
            try {
                //logger.trace("Closing fixture {}", fixture);
                fixture.close();
            } catch (IOException e) {
                //logger.warn("Error closing fixture {} ", fixture, e);
            }
        }
    }

    public boolean isSubsystemStarted(String systemName) {
        ModelNode address = new ModelNode();
        address.add("deployment", systemName);
        ModelNode operation = Operations.createReadAttributeOperation(address, "status");
        try {
            ModelNode result = client.execute(operation);
            if (Operations.isSuccessfulOutcome(result)) {
                return result.get("result").asString().equals("OK");
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

    }

    public static <T> T getBean(String beanName) {
        try {
            return jndi.lookup(beanName);
        } catch (NamingException e) {
            //logger.error("Error finding bean {}", beanName);
            throw new RuntimeException(e);
        }
    }

    public JndiFixture getJndi() {
        return jndi;
    }

    public JmsFixture getJms() {
        return jms;
    }
}
