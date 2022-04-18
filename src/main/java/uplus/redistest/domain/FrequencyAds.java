package uplus.redistest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@RedisHash("adFrequency")
public class FrequencyAds  implements Serializable {
    @Id
    private String id;

    private String adsNumber;
    private LocalDateTime adsViewTime;

    @Builder
    public FrequencyAds(String id, String adsNumber, LocalDateTime adsViewTime){
        this.id = id;
        this.adsNumber = adsNumber;
        this.adsViewTime = adsViewTime;

    }


}
