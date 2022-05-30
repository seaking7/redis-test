package uplus.redistest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@RedisHash("adFrequency")
public class FrequencyAds  implements Serializable {
    @Id
    private String userId;
    public List<AdsViewStatus> adsViewStatusList;


    @Builder
    public FrequencyAds(String userId, List<AdsViewStatus> adsViewStatusList){
        this.userId = userId;
        this.adsViewStatusList = adsViewStatusList;
    }


}
