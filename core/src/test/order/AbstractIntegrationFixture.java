package order;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

import javax.naming.NamingException;
import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractIntegrationFixture {

    private static JndiFixture jndi;
    @Rule
    public TestName testName = new TestName();

    @AfterClass
    public static void cleanUpAbstractIntegrationFixture() {
        try {
            close(jndi);
        } catch (Exception e) {
        }
    }

    @BeforeClass
    public static void initAbstractIntegrationFixture() {
        jndi = new JndiFixture();

    }


    protected static void close(Closeable fixture) {
        if (fixture != null) {
            try {
                fixture.close();
            } catch (IOException e) {
            }
        }
    }

    public static <T> T getBean(String beanName) {
        try {
            return jndi.lookup(beanName);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }


}
