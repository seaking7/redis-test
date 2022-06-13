package uplus.redistest.service.redis;

import lombok.extern.slf4j.Slf4j;
import uplus.redistest.domain.AdsViewStatus;
import uplus.redistest.domain.FrequencyAds;
import uplus.redistest.repos.FrequencyAdsRedisRepository;

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

    //찾아진 광고로 Frequency 체크하여 볼 수 있는 광고리스트 만듬
    public List<String> checkAndMakeFinalList(List<String> canViewList){
        List<AdsViewStatus> adsViewStatusList = searchAdsViewStatus(userId);

        List<String> finalList = new ArrayList<>();
        for(String checkAdsNo : canViewList){
            boolean isFound = findExcessAds(adsViewStatusList, checkAdsNo);
            if(!isFound){
                finalList.add(checkAdsNo);
            }
        }
        log.info("Final List : {}", finalList);
        return finalList;
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

    public void reflectWatchedResult(List<String> watchedList){
        List<AdsViewStatus> adsViewStatusList = searchAdsViewStatus(userId);
        modifyViewStatusList(watchedList, adsViewStatusList);

        printStatusList(adsViewStatusList);
        saveAdsFrequency(userId, adsViewStatusList);
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
