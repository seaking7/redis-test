package uplus.redistest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

//    @Value("${spring.redis.password}")
//    private String redisPwd;

//    @Value("${spring.redis.cluster.nodes}")
//    private List<String> clusterNodes;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        log.info("redis Server info {}, {}", redisHost, redisPort);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }


    //Sentinel
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        log.info("redis Server info {}, {}", redisHost, redisPort);
//        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("100.51.6.77", 26379)
//                .sentinel("100.51.5.35", 26379)
//                .sentinel("100.51.0.162", 26379);
//
//        return new LettuceConnectionFactory(redisSentinelConfiguration);
//    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        log.info("redis Cluster Server info {}", clusterNodes);
//
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
//
//        return new LettuceConnectionFactory(redisClusterConfiguration);
//    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));


        //redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(FrequencyAds2.class));


        return redisTemplate;
    }


}
