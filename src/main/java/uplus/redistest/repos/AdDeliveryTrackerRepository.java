
package uplus.redistest.repos;

import uplus.redistest.domain.entities.DeliveryTracker;

public interface AdDeliveryTrackerRepository {
	void save(DeliveryTracker deliveryTracker);
	DeliveryTracker findByDeliveryId(String deliveryId);
	void delete(String deliveryId);
}
