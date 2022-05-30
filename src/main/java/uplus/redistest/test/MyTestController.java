package uplus.redistest.test;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.SplittableRandom;

@Slf4j
@RestController
@AllArgsConstructor
public class MyTestController {

    private final RedisTemplate redisTemplate;



    @GetMapping("test4")
    public String test4() {
        String randomId = createId();
        String randomAdsNumber = createAdsNumber();

        FrequencyAds2 test = new FrequencyAds2(randomId, randomAdsNumber);
        redisTemplate.opsForValue().set("FrequencyAds2", test);

        FrequencyAds2 frequencyAds2 = (FrequencyAds2) redisTemplate.opsForValue().get("FrequencyAds2");
        System.out.println(frequencyAds2);

        return "OK";
    }
//
//
//    @GetMapping("test3")
//    public String test3() {
//
//        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//        String key = "adFrequencyTest";
//
//        String randomId = createId();
//        String randomAdsNumber = createAdsNumber();
//        LocalDateTime now = LocalDateTime.now();
//
//        FrequencyAds frequencyAds = FrequencyAds.builder()
//                .id(randomId)
//                .adsNumber(randomAdsNumber)
//                .adsViewTime(now)
//                .build();
//
//
//        hashOperations.put(key, frequencyAds.getAdsNumber(), frequencyAds);
//        System.out.println("after insert "+ frequencyAds.getAdsNumber());
//        return "OK";
//    }

    private String createId() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(100000, 199999));
    }

    private String createAdsNumber() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(100000, 199999));
    }

}
