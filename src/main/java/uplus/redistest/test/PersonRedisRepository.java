package uplus.redistest.test;

import org.springframework.data.repository.CrudRepository;
import uplus.redistest.test.Person;

public interface PersonRedisRepository extends CrudRepository<Person, String> {
}
