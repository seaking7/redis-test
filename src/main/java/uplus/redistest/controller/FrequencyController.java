package uplus.redistest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uplus.redistest.service.FrequencyService;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class FrequencyController {

    private final FrequencyService frequencyService;
    private final RedisTemplate redisTemplate;


    @GetMapping("/test1")
    public void test1(){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "stringKey";

        //when
        valueOperations.set(key, "hello");
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        key = "adFreq";

        //when
        hashOperations.put(key, "101010", "20220418");
        hashOperations.put(key, "101011", "20220419");
        hashOperations.put(key, "101012", "20220119");

    }

    @GetMapping("test2")
    public String test2(){

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "adFreq";

        Object value = hashOperations.get(key, "101010");
        System.out.println("value = " + value);


        Map<Object, Object> entries = hashOperations.entries(key);
        for (Object o : entries.keySet()) {
            System.out.println("o = " + o);
            System.out.println("value = " + entries.values());
        }

        return "OK";
    }


    @GetMapping("/save")
    public String save(){
     //   frequencyService.updateAdsView();

        return "save";
    }

    @GetMapping("/get")
    public String get () {
        frequencyService.checkFrequency();
        return "ok";
    }



}
