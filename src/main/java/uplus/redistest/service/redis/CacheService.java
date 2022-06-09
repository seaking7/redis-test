/**
 * @author gramcha
 * 07-Jul-2018 4:51:45 PM
 * 
 */
package uplus.redistest.service.redis;

import uplus.redistest.domain.entities.ClickTracker;
import uplus.redistest.domain.entities.DeliveryTracker;

public interface CacheService {
	void addDeliveryTrackerIntoCache(DeliveryTracker deliveryTracker);

	DeliveryTracker queryDeliveryTrackerFromCache(String deliveryId);

	void addClickTrackerIntoCache(ClickTracker clickTracker);

	ClickTracker queryClickTrackerFromCache(String clickId);

}
