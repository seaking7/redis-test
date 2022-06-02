package uplus.redistest.domain;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class CacheCheckerTest {

    @Autowired
    private FrequencyAdsRedisRepository frequencyAdsRedisRepository;

    @BeforeAll
    public static void init(){

    }

    @Test
    public void test_checkAndMakeFinalList(){
        List<String> canViewList = Arrays.asList("991101", "991102", "991103");
        String userId = "999901";
        CacheChecker cacheChecker = new CacheChecker(userId, frequencyAdsRedisRepository);

        //찾아진 광고로 Frequency 체크하여 볼 수 있는 광고리스트 만듬
        List<String> listFinalList = cacheChecker.checkAndMakeFinalList(canViewList);
        Assertions.assertThat(listFinalList).contains("991101", "991102", "991103");
    }

    @Test
    public void test_reflectFinalResult(){
        List<String> canViewList = Arrays.asList("991101", "991102", "991103");
        String userId = "999901";
        CacheChecker cacheChecker = new CacheChecker(userId, frequencyAdsRedisRepository);
        cacheChecker.reflectFinalResult(canViewList);

        List<AdsViewStatus> resultList = cacheChecker.searchAdsViewStatus(userId);
        Assertions.assertThat(resultList).size().isEqualTo(3);
        String firstAds = resultList.stream()
                .sorted(Comparator.comparing(AdsViewStatus::getAdsNumber))
                .findFirst()
                        .get().getAdsNumber();
        Assertions.assertThat(firstAds).isEqualTo("991101");
    }

}