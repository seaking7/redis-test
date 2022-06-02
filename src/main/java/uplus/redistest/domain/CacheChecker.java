package uplus.redistest.domain;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class CacheChecker {

    private String userId;
    private final FrequencyAdsRedisRepository frequencyAdsRedisRepository;

    public CacheChecker(String userId, FrequencyAdsRedisRepository frequencyAdsRedisRepository) {
        this.userId = userId;
        this.frequencyAdsRedisRepository = frequencyAdsRedisRepository;
    }


    public List<String> checkAndMakeFinalList(List<String> canViewList){
        List<AdsViewStatus> adsViewStatusList = searchAdsViewStatus(userId);

        List<String> listFinalList = new ArrayList<>();
        for(String checkAdsNo : canViewList){
            boolean isFound = findExcessAds(adsViewStatusList, checkAdsNo);
            if(!isFound){
                listFinalList.add(checkAdsNo);
            }
        }
        log.info("Final List : {}", listFinalList);
        return listFinalList;
    }

    private boolean findExcessAds(List<AdsViewStatus> adsViewStatusList, String checkAdsNo) {
        for(int i = 0; i < adsViewStatusList.size(); i++){
            AdsViewStatus adsViewStatus = adsViewStatusList.get(i);
            if(adsViewStatus.adsNumber.equals(checkAdsNo) && adsViewStatus.viewCount >= 3){
                log.info("can't ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
                return true;
            }
        }
        return false;
    }

    public void reflectFinalResult(List<String> watchedList){
        String viewingUserId = this.userId;
        List<AdsViewStatus> adsViewStatusList = searchAdsViewStatus(viewingUserId);
        modifyViewStatusList(watchedList, adsViewStatusList);

        printStatusList(adsViewStatusList);
        saveAdsFrequency(viewingUserId, adsViewStatusList);
    }

    public List<AdsViewStatus> searchAdsViewStatus(String id) {
        Optional<FrequencyAds> cacheDataById = frequencyAdsRedisRepository.findById(id);
        List<AdsViewStatus> adsViewStatusList = new ArrayList<>();
        if(cacheDataById.isPresent()){
            adsViewStatusList = cacheDataById.get().getAdsViewStatusList();
        }
        return adsViewStatusList;
    }


    private void modifyViewStatusList(List<String> watchedList, List<AdsViewStatus> adsViewStatusList) {
        for( String watchedAdsNo : watchedList){
            boolean isFound = updateAlreadyViewAds(adsViewStatusList, watchedAdsNo);
            if(!isFound){
                insertNewViewAds(adsViewStatusList, watchedAdsNo);
            }
        }
    }

    private boolean updateAlreadyViewAds(List<AdsViewStatus> adsViewStatusList, String watchedAdsNo) {
        for(int i = 0; i < adsViewStatusList.size(); i++){
            AdsViewStatus adsViewStatus = adsViewStatusList.get(i);
            if(adsViewStatus.adsNumber.equals(watchedAdsNo)){
                adsViewStatus.viewCount++;
                adsViewStatusList.set(i, adsViewStatus);
                log.info("existed ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
                return true;
            }
        }
        return false;
    }

    private void insertNewViewAds(List<AdsViewStatus> adsViewStatusList, String watchedAdsNo) {
        AdsViewStatus adsViewStatus = new AdsViewStatus();
        adsViewStatus.adsNumber = watchedAdsNo;
        adsViewStatus.adsFrequencyTime = LocalDateTime.now().plusDays(7);
        adsViewStatus.viewCount = 1;
        log.info("new ads {} {} {}", adsViewStatus.adsNumber, adsViewStatus.viewCount, adsViewStatus.adsFrequencyTime);
        adsViewStatusList.add(adsViewStatus);
    }

    private void printStatusList(List<AdsViewStatus> adsViewStatusList) {
        adsViewStatusList.stream()
                .sorted(Comparator.comparing(AdsViewStatus::getAdsNumber))
                .forEach(System.out::println);
    }

    private void saveAdsFrequency(String viewingUserId, List<AdsViewStatus> adsViewStatusList) {
        FrequencyAds frequencyAds = FrequencyAds.builder()
                .userId(viewingUserId)
                .adsViewStatusList(adsViewStatusList)
                .build();

        log.info(">>>>>>> [save] FrequencyAds={}", frequencyAds);
        frequencyAdsRedisRepository.save(frequencyAds);
    }

}
