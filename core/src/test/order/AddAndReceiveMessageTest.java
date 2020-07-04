package order;

import beans.MessageManager;
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
        messageManager.addMessage("test message");

        messageManager.receive();

    }

  private MessageManager getOrderManager(){
      return (MessageManager)getBean(ORDER_MANANGER_BEAN_NAME);
  }
}
