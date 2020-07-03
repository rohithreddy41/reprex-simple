package tracking.entityListeners;

import constants.NotificationConstants;
import tracking.domain.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PostUpdate;
import java.util.Set;

/**
 * User: bven
 * Date: 4/25/16.
 */
@ApplicationScoped
public class UpdateEventListener extends LifecycleListener {

   // private static final Logger logger = LoggerFactory.getLogger(UpdateEventListener.class);

    @PostUpdate
    public void updateEvent(Order entity) {
      //  if (entity instanceof PrimaryKey) {
            //Object pk = ((PrimaryKey) entity).getId();
            Class<?> classWithAnnotation = findTrackedPropertyAnnotationClass(entity.getClass());
            UpdateEvent updateEvent = new UpdateEvent(entity.getOrderId(), classWithAnnotation.getCanonicalName());
            Set<ChangedProperty> changedProperties =buildChangeList(classWithAnnotation, entity);
            if(!changedProperties.isEmpty()){
                updateEvent.setChangeList(changedProperties);
                sendEvent(updateEvent, NotificationConstants.LifecycleAction.UPDATE);
            }
        }
    }




