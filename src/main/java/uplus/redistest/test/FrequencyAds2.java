package uplus.redistest.test;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
public class FrequencyAds2 implements Serializable {
    @Id
    private String id;
    private String adsNumber;


    public FrequencyAds2(String id, String adsNumber ) {
        this.id = id;
        this.adsNumber = adsNumber;
    }


}
