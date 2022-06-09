/**
 * @author gramcha
 * 07-Jul-2018 4:51:45 PM
 * 
 */
package uplus.redistest.service;

import uplus.redistest.entities.ClickTracker;
import uplus.redistest.entities.DeliveryTracker;

public interface CacheService {
	void addDeliveryTrackerIntoCache(DeliveryTracker deliveryTracker);

	DeliveryTracker queryDeliveryTrackerFromCache(String deliveryId);

	void addClickTrackerIntoCache(ClickTracker clickTracker);

	ClickTracker queryClickTrackerFromCache(String clickId);

}
