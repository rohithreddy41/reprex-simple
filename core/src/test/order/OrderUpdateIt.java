package order;

import beans.OrderManager;
import org.junit.Test;

public class OrderUpdateIt extends AbstractIntegrationFixture{

    final public static String APPNAME = "reprex-1.0-SNAPSHOT";
    final public static String MODULENAME = "core";
    final public static String BEAN_PREFIX = "ejb:" + APPNAME + "/" + MODULENAME;
    final public static String ORDER_MANANGER_BEAN_NAME = BEAN_PREFIX + "/OrderManagerBean!" + OrderManager.class.getName();


    @Test
    public void addMessages(){
        OrderManager orderManager = getOrderManager();
        orderManager.updateOrder(1l);
        System.out.println(orderManager.getMessageReceivedCount());
    }

  private OrderManager getOrderManager(){
      return (OrderManager)getBean(ORDER_MANANGER_BEAN_NAME);
  }
}
