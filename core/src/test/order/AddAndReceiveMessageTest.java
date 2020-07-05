package order;

import beans.MessageManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.jms.JMSException;

public class AddAndReceiveMessageTest extends AbstractIntegrationFixture{

    final public static String APPNAME = "reprex-1.0-SNAPSHOT";
    final public static String MODULENAME = "core";
    final public static String BEAN_PREFIX = "ejb:" + APPNAME + "/" + MODULENAME;
    final public static String ORDER_MANANGER_BEAN_NAME = BEAN_PREFIX + "/MessageManagerBean!" + MessageManager.class.getName();


    @Test
    public void addMessages() throws JMSException{
        MessageManager messageManager = getOrderManager();
        String message = RandomStringUtils.randomAlphanumeric(8);
        messageManager.addMessage(message);

        messageManager.receive();

    }

  private MessageManager getOrderManager(){
      return (MessageManager)getBean(ORDER_MANANGER_BEAN_NAME);
  }
}
