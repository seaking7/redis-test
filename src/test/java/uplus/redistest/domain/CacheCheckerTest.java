package uplus.redistest.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class CacheCheckerTest {
    String userId = "999901";

    @Autowired
    private FrequencyAdsRedisRepository frequencyAdsRedisRepository;

    @BeforeEach
    public  void initUser(){
        frequencyAdsRedisRepository.deleteById(userId);
        CacheChecker cacheChecker = new CacheChecker(userId, frequencyAdsRedisRepository);

        cacheChecker.reflectWatchedResult(Arrays.asList("991102", "991103"));
        cacheChecker.reflectWatchedResult(Arrays.asList("991102", "991103"));
        cacheChecker.reflectWatchedResult(Arrays.asList("991103"));
        cacheChecker.reflectWatchedResult(Arrays.asList("991103"));
    }

    @Test
    @DisplayName("3개의 ADS 중 Frequency 이슈없는 2개의 Ads 만 잘 반환하는지")
    public void test_checkAndMakeFinalList(){
        List<String> canViewList = Arrays.asList("991101", "991102", "991103");
        CacheChecker cacheChecker = new CacheChecker(userId, frequencyAdsRedisRepository);

        List<String> listFinalList = cacheChecker.checkAndMakeFinalList(canViewList);
        Assertions.assertThat(listFinalList).contains("991101", "991102");
        Assertions.assertThat(listFinalList).doesNotContain("991103");
    }

    @Test
    @DisplayName("최종 시청한 Ads 정보를 캐쉬에 잘 저장하는지 확인")
    public void test_reflectFinalResult(){
        //given
        List<String> canViewList = Arrays.asList("991101", "991102", "991103");
        CacheChecker cacheChecker = new CacheChecker(userId, frequencyAdsRedisRepository);

        //when
        cacheChecker.reflectWatchedResult(canViewList);
        List<AdsViewStatus> resultList = cacheChecker.searchAdsViewStatus(userId);

        //then
        Assertions.assertThat(resultList).size().isEqualTo(3);
        String firstAds = resultList.stream()
                .sorted(Comparator.comparing(AdsViewStatus::getAdsNumber))
                .findFirst()
                        .get().getAdsNumber();
        Assertions.assertThat(firstAds).isEqualTo("991101");
    }

}