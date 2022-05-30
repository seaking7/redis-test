package uplus.redistest.test;

import org.springframework.data.repository.CrudRepository;
import uplus.redistest.test.AvailablePoint;

public interface AvailablePointRedisRepository extends CrudRepository<AvailablePoint, String> {
}
