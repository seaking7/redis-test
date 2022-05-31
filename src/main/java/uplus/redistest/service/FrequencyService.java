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
        log.info("canViewList : {}", canViewList);

        Optional<FrequencyAds> cacheDataById = frequencyAdsRedisRepository.findById(id);
        List<AdsViewStatus> adsViewStatusList = new ArrayList<>();
        if(cacheDataById.isPresent()) {
            adsViewStatusList = cacheDataById.get().adsViewStatusList;
//            adsViewStatusList.stream().sorted(Comparator.comparing(AdsViewStatus::getAdsNumber))
//                    .forEach(System.out::println);
        }

        List<String> listFinalList = new ArrayList<>();
        for(String checkAdsNo : canViewList){
            boolean isFound = false;
            for(int i = 0; i < adsViewStatusList.size(); i++){
                AdsViewStatus adsViewStatus = adsViewStatusList.get(i);
                if(adsViewStatus.adsNumber.equals(checkAdsNo) && adsViewStatus.viewCount >= 3){
                    isFound = true;
                    log.info("can't ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
                    break;
                }
            }
            if(!isFound){
                listFinalList.add(checkAdsNo);
            }
        }
        log.info("FoundList : {}", listFinalList);
        updateAdsView(listFinalList);

    }

    public void updateAdsView(List<String> watchedList){

        String viewingUserId = createId();
        List<AdsViewStatus> beforeAdsViewList = searchAdsViewStatus(viewingUserId);

        List<AdsViewStatus> afterAdsViewList = new ArrayList<>();
        afterAdsViewList = beforeAdsViewList;
        for( String watchedAdsNo : watchedList){
            boolean isFound = false;

            for(int i = 0; i < beforeAdsViewList.size(); i++){
                AdsViewStatus adsViewStatus = beforeAdsViewList.get(i);
                if(adsViewStatus.adsNumber.equals(watchedAdsNo)){
                    adsViewStatus.viewCount++;
                    afterAdsViewList.set(i, adsViewStatus);
                    log.info("existed ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
                    isFound = true;
                    break;
                }
            }

            if(!isFound){
                afterAdsViewList.add(makeNewAdsStatus(watchedAdsNo));
            }
        }

        afterAdsViewList.stream().sorted(Comparator.comparing(AdsViewStatus::getAdsNumber))
                .forEach(System.out::println);


        FrequencyAds frequencyAds = FrequencyAds.builder()
                .userId(viewingUserId)
                .adsViewStatusList(afterAdsViewList)
                .build();

        log.info(">>>>>>> [save] FrequencyAds={}", frequencyAds);

        frequencyAdsRedisRepository.save(frequencyAds);
    }

    private List<AdsViewStatus> searchAdsViewStatus(String id) {
        Optional<FrequencyAds> cacheDataById = frequencyAdsRedisRepository.findById(id);
        List<AdsViewStatus> beforeAdsViewList = new ArrayList<>();
        if(cacheDataById.isPresent()){
            beforeAdsViewList = cacheDataById.get().getAdsViewStatusList();
        }
        return beforeAdsViewList;
    }

    private AdsViewStatus makeNewAdsStatus(String watchedAdsNo) {
        AdsViewStatus adsViewStatus = new AdsViewStatus();
        adsViewStatus.adsNumber = watchedAdsNo;
        adsViewStatus.adsFrequencyTime = LocalDateTime.now().plusDays(7);
        adsViewStatus.viewCount = 1;
        log.info("new ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
        return adsViewStatus;
    }

    private List<String> supplyListOfCanView(){
        List<String> result = new ArrayList<String>();
        for(int i=0; i < 10; i++){
            result.add(createAdsNumber());
        }
        return result;
    }

    private List<String> supplyListOfWatched(){
        List<String> result = new ArrayList<String>();
        for(int i=0; i < 5; i++){
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
