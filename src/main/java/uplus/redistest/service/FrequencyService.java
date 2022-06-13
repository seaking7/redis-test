package uplus.redistest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uplus.redistest.service.redis.CacheChecker;
import uplus.redistest.repos.FrequencyAdsRedisRepository;

import java.util.*;

@Slf4j
@Service
public class FrequencyService {

    @Autowired
    private FrequencyAdsRedisRepository frequencyAdsRedisRepository;


    public void supportTest(){
        CacheChecker cacheChecker = new CacheChecker(createId(), frequencyAdsRedisRepository);

        //찾아진 광고리스트. 향후 집행에서 전달해야 함
        List<String> canViewList = supplyListOfCanView();
        log.info("canViewList : {}", canViewList);
        //찾아진 광고로 Frequency 체크하여 볼 수 있는 광고리스트 만듬
        List<String> listFinalList = cacheChecker.checkAndMakeFinalList(canViewList);
        //광고 집행이후 시청한 광고리스트 캐쉬에 update
        cacheChecker.reflectWatchedResult(listFinalList);
    }

    private List<String> supplyListOfCanView(){
        List<String> result = new ArrayList<String>();
        for(int i=0; i < 10; i++){
            result.add(createAdsNumber());
        }
        return result;
    }

    private String createAdsNumber() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(111100, 111199));
    }

    private String createId() {
        SplittableRandom random = new SplittableRandom();
        //return String.valueOf(random.nextInt(100000, 199999));
        return "110011";
    }

}
