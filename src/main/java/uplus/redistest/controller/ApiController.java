package uplus.redistest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiController {

    private final RedisTemplate redisTemplate;

    @GetMapping("/keys")
    public String keys(){
        Set<byte[]> keys = redisTemplate.keys("*");
        return "keys";
    }
}
