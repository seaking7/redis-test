package uplus.redistest.repos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uplus.redistest.entities.ClickTracker;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdClickTrackerRepositoryTest {
    @Autowired
    private AdClickTrackerRepository clickRepo;
    private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
    private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";

    @AfterEach
    public void tearDown() {
        clickRepo.delete(CLICK_ID);
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

    @DisplayName("Repository로 save실행 후 find로 찾기동작")
    @Test
    public void whenEntitySavedByRepoThenItIsSavedIntoSystem() throws Exception{
        ClickTracker clickTrackerEntity = createClickrTrackerEntity();
        clickRepo.save(clickTrackerEntity);
        assertThat(clickRepo.findByClickId(CLICK_ID)).isEqualTo(clickTrackerEntity);
    }

    @Test
    public void whenRepoQueriedBySavedClickedClickIdThenItReturnsCorrespondingEntity() throws Exception{
        ClickTracker clickTrackerEntity = createClickrTrackerEntity();
        clickRepo.save(clickTrackerEntity);
        assertThat(clickRepo.findByClickId(CLICK_ID)).isEqualTo(clickTrackerEntity);
    }

    @Test
    public void whenRepoQueriedByUnSavedClickedClickIdThenItReturnsNull() throws Exception{
        ClickTracker clickTrackerEntity = createClickrTrackerEntity();
        clickRepo.save(clickTrackerEntity);
        assertThat(clickRepo.findByClickId(CLICK_ID+"unsaved")).isNull();
    }
}