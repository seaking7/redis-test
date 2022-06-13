package uplus.redistest.repos;

import org.springframework.data.repository.CrudRepository;
import uplus.redistest.domain.FrequencyAds;

public interface FrequencyAdsRedisRepository extends CrudRepository<FrequencyAds, String> {
}
