package uplus.redistest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uplus.redistest.domain.AvailablePoint;
import uplus.redistest.domain.AvailablePointRedisRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.SplittableRandom;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiController {

    private final AvailablePointRedisRepository availablePointRedisRepository;
    private final RedisTemplate redisTemplate;

    @GetMapping("/keys")
    public String keys(){
        Set<byte[]> keys = redisTemplate.keys("*");

        return keys.toString();
    }


    @GetMapping("/save1")
    public String save(){
        String randomId = createId();
        LocalDateTime now = LocalDateTime.now();

        AvailablePoint availablePoint = AvailablePoint.builder()
                .id(randomId)
                .point(1L)
                .refreshTime(now)
                .build();

        log.info(">>>>>>> [save] availablePoint={}", availablePoint);

        availablePointRedisRepository.save(availablePoint);

        return "save";
    }


    @GetMapping("/get2")
    public long get () {
        String id = createId();
        return availablePointRedisRepository.findById(id)
                .map(AvailablePoint::getPoint)
                .orElse(0L);
    }

    @GetMapping("/get3")
    public long get2 () {
        String id = createId();
        return availablePointRedisRepository.findById(id)
                .map(AvailablePoint::getPoint)
                .orElse(0L);
    }

    private String createId() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(1, 1_000_000_000));
    }
}
