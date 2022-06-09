package uplus.redistest.repos;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uplus.redistest.entities.DeliveryTracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdDeliveryTrackerRepositoryTest {
    @Autowired
    private AdDeliveryTrackerRepository deliveryRepo;
    private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";

    @AfterEach
    public void tearDown() {
        deliveryRepo.delete(DELIVERY_ID);
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

    @Test
    public void whenEntitySavedByRepoThenItIsSavedIntoSystem() throws Exception{
        DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
        deliveryRepo.save(deliveryrackerEntity);
        assertThat(deliveryRepo.findByDeliveryId(DELIVERY_ID)).isEqualTo(deliveryrackerEntity);
    }

    @Test
    public void whenRepoQueriedBySavedClickedClickIdThenItReturnsCorrespondingEntity() throws Exception{
        DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
        deliveryRepo.save(deliveryrackerEntity);
        assertThat(deliveryRepo.findByDeliveryId(DELIVERY_ID)).isEqualTo(deliveryrackerEntity);
    }

    @Test
    public void whenRepoQueriedByUnSavedClickedClickIdThenItReturnsNull() throws Exception{
        DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
        deliveryRepo.save(deliveryrackerEntity);
        assertThat(deliveryRepo.findByDeliveryId(DELIVERY_ID+"unsaved")).isNull();
    }
}