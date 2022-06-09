
package uplus.redistest.repos;

import uplus.redistest.entities.DeliveryTracker;

public interface AdDeliveryTrackerRepository {
	void save(DeliveryTracker deliveryTracker);
	DeliveryTracker findByDeliveryId(String deliveryId);
	void delete(String deliveryId);
}
