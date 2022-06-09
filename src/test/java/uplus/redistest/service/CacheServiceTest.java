package uplus.redistest.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uplus.redistest.domain.entities.ClickTracker;
import uplus.redistest.domain.entities.DeliveryTracker;
import uplus.redistest.repos.AdClickTrackerRepository;
import uplus.redistest.repos.AdDeliveryTrackerRepository;
import uplus.redistest.service.redis.CacheService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CacheServiceTest {

    @Autowired
    CacheService dbCache;
    @Autowired
    private AdDeliveryTrackerRepository deliveryRepo;
    @Autowired
    private AdClickTrackerRepository clickRepo;
    private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
    private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";

    @AfterEach
    public void tearDown() {
        deliveryRepo.delete(DELIVERY_ID);
        clickRepo.delete(CLICK_ID);
    }

    private DeliveryTracker createDeliverTrackerEntity() {
        DeliveryTracker deliveryTrackerEntity = new DeliveryTracker();
        deliveryTrackerEntity.setAdvertisementId(4483);
        deliveryTrackerEntity.setDeliveryId(DELIVERY_ID);
        deliveryTrackerEntity.setBrowser("Chrome");
        deliveryTrackerEntity.setOs("iOS");
        deliveryTrackerEntity.setSite("http://super-dooper-news.com");
        deliveryTrackerEntity.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:23").getTime());
        return deliveryTrackerEntity;
    }
    private ClickTracker createClickrTrackerEntity() {
        ClickTracker clickTrackerEntity = new ClickTracker();
        clickTrackerEntity.setAdvertisementId(4483);
        clickTrackerEntity.setDeliveryId(DELIVERY_ID);
        clickTrackerEntity.setBrowser("Chrome");
        clickTrackerEntity.setOs("iOS");
        clickTrackerEntity.setSite("http://super-dooper-news.com");
        clickTrackerEntity.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:34:00").getTime());
        clickTrackerEntity.setClickId(CLICK_ID);
        return clickTrackerEntity;
    }

    @DisplayName("[Service] delivery로 Cache 저장 후 잘 찾아지는지 테스트")
    @Test
    public void whenDeliveryTrackerEntityAddedIntoCacheThenItIsSavedCacheServer() throws Exception {
        DeliveryTracker deliveryTrackerEntity = createDeliverTrackerEntity();
        dbCache.addDeliveryTrackerIntoCache(deliveryTrackerEntity);
        DeliveryTracker queriedEntity = deliveryRepo.findByDeliveryId(DELIVERY_ID);
        assertThat(queriedEntity).isEqualTo(deliveryTrackerEntity);
    }

    @Test
    public void whenCacheQueriedWithValidDeliveryTrackerIdThenCorrespondingDeliverTrackerEntityReturned()
            throws Exception {
        DeliveryTracker deliveryTrackerEntity = createDeliverTrackerEntity();
        dbCache.addDeliveryTrackerIntoCache(deliveryTrackerEntity);
        DeliveryTracker queriedEntity = dbCache.queryDeliveryTrackerFromCache(DELIVERY_ID);
        assertThat(queriedEntity).isEqualTo(deliveryTrackerEntity);
    }

    @Test
    public void whenCacheQueriedWithDeliveryTrackerIdWhichIsNotCachedThenNulleturned() throws Exception {
        DeliveryTracker queriedEntity = dbCache.queryDeliveryTrackerFromCache(DELIVERY_ID);
        assertThat(queriedEntity).isNull();
    }

    @Test
    public void whenClickTrackerEntityAddedIntoCacheThenItIsSavedCacheServer() throws Exception {
        ClickTracker clickTrackerEntity = createClickrTrackerEntity();
        dbCache.addClickTrackerIntoCache(clickTrackerEntity);
        ClickTracker queriedEntity = clickRepo.findByClickId(CLICK_ID);
        assertThat(queriedEntity).isEqualTo(clickTrackerEntity);
    }

    @Test
    public void whenCacheQueriedWithClickTrackerIdWhichIsNotCachedThenNulleturned() throws Exception {
        ClickTracker queriedEntity = dbCache.queryClickTrackerFromCache(CLICK_ID);
        assertThat(queriedEntity).isNull();
    }
}