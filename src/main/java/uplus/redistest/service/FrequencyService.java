package uplus.redistest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uplus.redistest.domain.AdsViewStatus;
import uplus.redistest.domain.FrequencyAds;
import uplus.redistest.domain.FrequencyAdsRedisRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class FrequencyService {

    @Autowired
    private FrequencyAdsRedisRepository frequencyAdsRedisRepository;

    public void checkFrequency(){
        String id = createId();
        List<String> canViewList = supplyListOfCanView();

        Optional<FrequencyAds> byId = frequencyAdsRedisRepository.findById(id);
        if(byId.isPresent()) {
            List<AdsViewStatus> adsViewStatusList = byId.get().adsViewStatusList;
//            adsViewStatusList.stream()
                            
            adsViewStatusList.forEach(System.out::println);
        }



    }

    public void updateAdsView(){

        String randomId = createId();

        String id = createId();
        Optional<FrequencyAds> byId = frequencyAdsRedisRepository.findById(id);

        AdsViewStatus adsViewStatus = new AdsViewStatus();
        adsViewStatus.adsNumber = createAdsNumber();
        adsViewStatus.adsFrequencyTime = LocalDateTime.now().plusDays(7);
        adsViewStatus.viewCount = 1;

        AdsViewStatus adsViewStatus2 = new AdsViewStatus();
        adsViewStatus2.adsNumber = createAdsNumber();
        adsViewStatus2.adsFrequencyTime = LocalDateTime.now().plusDays(30);
        adsViewStatus2.viewCount = 1;


        List<AdsViewStatus> adsViewStatusList = Arrays.asList(adsViewStatus, adsViewStatus2);
        FrequencyAds frequencyAds = FrequencyAds.builder()
                .userId(randomId)
                .adsViewStatusList(adsViewStatusList)
                .build();

        log.info(">>>>>>> [save] FrequencyAds={}", frequencyAds);

        frequencyAdsRedisRepository.save(frequencyAds);
    }

    private List<String> supplyListOfCanView(){
        List<String> result = new ArrayList<String>();
        for(int i=0; i < 10; i++){
            result.add(createAdsNumber());
        }
        return result;
    }

    private String createId() {
        SplittableRandom random = new SplittableRandom();
        //return String.valueOf(random.nextInt(100000, 199999));
        return "110011";

    }

    private String createAdsNumber() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(111100, 111199));
    }
}
