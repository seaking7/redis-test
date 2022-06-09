/**
 * @author gramcha
 * 07-Jul-2018 4:58:07 PM
 * 
 */
package uplus.redistest.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uplus.redistest.domain.entities.ClickTracker;
import uplus.redistest.domain.entities.DeliveryTracker;
import uplus.redistest.repos.AdClickTrackerRepository;
import uplus.redistest.repos.AdDeliveryTrackerRepository;

@Service
public class RedisCacheService implements CacheService {
	@Autowired
	AdDeliveryTrackerRepository deliveryTrackerRepo;

	@Autowired
	AdClickTrackerRepository clickTrackerRepo;
	@Override
	public void addDeliveryTrackerIntoCache(DeliveryTracker deliveryTracker) {
		deliveryTrackerRepo.save(deliveryTracker);
	}

	@Override
	public DeliveryTracker queryDeliveryTrackerFromCache(String deliveryId) {
		return deliveryTrackerRepo.findByDeliveryId(deliveryId);
	}

	@Override
	public void addClickTrackerIntoCache(ClickTracker clickTracker) {
		clickTrackerRepo.save(clickTracker);
	}

	@Override
	public ClickTracker queryClickTrackerFromCache(String clickId) {
		return clickTrackerRepo.findByClickId(clickId);
	}

}
