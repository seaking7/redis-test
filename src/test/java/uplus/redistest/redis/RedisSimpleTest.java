package uplus.redistest.redis;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class RedisSimpleTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    public void tearDown() {

    }

    @Test
    void testString(){

        //given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "stringKey";

        //when
        valueOperations.set(key, "hello");

        //then
        String value = valueOperations.get(key);
        assertThat(value).isEqualTo("hello");
    }

    @Test
    void testList(){
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        String key = "listKey";
        listOperations.rightPush(key, "H");
        listOperations.rightPush(key, "e");
        listOperations.rightPush(key, "l");
        listOperations.rightPush(key, "l");
        listOperations.rightPush(key, "o");
        listOperations.rightPushAll(key, " ", "t", "e", "s", "t");

        assertThat(listOperations.index(key, 0)).isEqualTo("H");
        assertThat(listOperations.index(key, 1)).isEqualTo("e");
        assertThat(listOperations.size(key)).isEqualTo(10);

        List<String> resultRange = listOperations.range(key, 0, 9);
        System.out.println(Arrays.toString(resultRange.toArray()));

        //listOperations.remove(key, 10,  )
    }


    @Test
    void testSet(){
        //given
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "setKey";

        //when
        setOperations.add(key, "h", "e", "l", "l", "o");

        //then
        Set<String> members = setOperations.members(key);
        Long size = setOperations.size(key);

        assertThat(members).containsOnly("h", "e", "l", "o");
        assertThat(size).isEqualTo(4);
    }

    @Test
    void testSortedSet(){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String key = "sortedSetKey";

        zSetOperations.add(key, "H", 1);
        zSetOperations.add(key, "e", 5);
        zSetOperations.add(key, "L", 10);
        zSetOperations.add(key, "L", 15);
        zSetOperations.add(key, "o", 20);

        assertThat(zSetOperations.range(key, 0, 5)).containsOnly("H", "e", "L", "o");

        assertThat(zSetOperations.size(key)).isEqualTo(4);

        assertThat(zSetOperations.rangeByScore(key, 11, 23)).containsOnly("L", "o");
        assertThat(zSetOperations.rangeByScore(key, 1, 9)).containsOnly("H", "e");
        assertThat(zSetOperations.rangeByScore(key, 2, 9)).containsOnly("e");
    }

    @Test
    void testHash(){
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "hashKey";

        //when
        hashOperations.put(key, "hello", "world");

        //then
        Object value = hashOperations.get(key, "hello");
        assertThat(value).isEqualTo("world");

        Map<Object, Object> entries = hashOperations.entries(key);
        assertThat(entries.keySet()).containsExactly("hello");
        assertThat(entries.values()).containsExactly("world");

        Long size = hashOperations.size(key);
        assertThat(size).isEqualTo(entries.size());
    }


}
