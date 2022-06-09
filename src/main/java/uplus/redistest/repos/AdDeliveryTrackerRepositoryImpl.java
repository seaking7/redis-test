/**
 * @author gramcha
 * 07-Jul-2018 4:10:55 PM
 * 
 */
package uplus.redistest.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import uplus.redistest.entities.DeliveryTracker;

import javax.annotation.PostConstruct;

@Repository
public class AdDeliveryTrackerRepositoryImpl implements AdDeliveryTrackerRepository {
	private static final String KEY = "Delivery";

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, DeliveryTracker> hashOperations;

	@Autowired
	public AdDeliveryTrackerRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(DeliveryTracker deliveryTracker) {
		hashOperations.put(KEY, deliveryTracker.getDeliveryId(), deliveryTracker);
	}

	@Override
	public DeliveryTracker findByDeliveryId(String deliveryId) {
		return hashOperations.get(KEY, deliveryId);
	}

	@Override
	public void delete(final String deliveryId) {
		hashOperations.delete(KEY, deliveryId);
	}

}
