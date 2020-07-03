package beans;

import tracking.domain.Order;



public interface OrderManager {
    void updateOrder(Long orderId);

    int getMessageReceivedCount();
}
