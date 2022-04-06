package uplus.redistest.redis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uplus.redistest.domain.Person;
import uplus.redistest.domain.PersonRedisRepository;

import java.util.Optional;


@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private PersonRedisRepository repo;

    @Test
    void test() {
        Person person = new Person("Kim", 20);

        // 저장
        repo.save(person);

        // `keyspace:id` 값을 가져옴
        Optional<Person> returnPerson = repo.findById(person.getId());
//        if(returnPerson != null) {
//            Person test1 = returnPerson.get();
//            log.info("get value = {}", test1.getName());
//        }

        // Person Entity 의 @RedisHash 에 정의되어 있는 keyspace (people) 에 속한 키의 갯수를 구함

        repo.count();

        // 삭제
        //repo.delete(person);
    }
}
