package beans;

import tracking.domain.Order;
import tracking.mdb.MDBMessageTracker;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
@Remote(OrderManager.class)
@ApplicationScoped
public class OrderManagerBean implements OrderManager {

    @Inject
    private MDBMessageTracker mdbMessageTracker;

    @PersistenceContext(unitName="nsfsApp")
    private EntityManager entityManager;

    @Override
    public void updateOrder(Long orderId) {
        Order order = entityManager.find(Order.class,orderId);
        boolean isReadyForSo = order.isReadyForSO();
        order.setReadyForSO(!isReadyForSo);
        entityManager.merge(order);
        entityManager.flush();
    }

    @Override
    public int getMessageReceivedCount() {
        return mdbMessageTracker.getMessgeReceivedCount();
    }
}
