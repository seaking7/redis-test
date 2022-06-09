package uplus.redistest.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uplus.redistest.domain.models.AdClickTrackerRequest;
import uplus.redistest.domain.models.AdDeliveryTrackerRequest;
import uplus.redistest.domain.models.AdInstallTrackerRequest;
import uplus.redistest.repos.AdClickTrackerRepository;
import uplus.redistest.repos.AdDeliveryTrackerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TrackerIngestServiceTest {
    private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
    private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";
    @Autowired
    TrackerIngestService ingestService;
    @Autowired
    private AdDeliveryTrackerRepository deliveryRepo;
    @Autowired
    private AdClickTrackerRepository clickRepo;

    @AfterEach
    public void tearDown() {
        deliveryRepo.delete(DELIVERY_ID);
        clickRepo.delete(CLICK_ID);
    }

    private AdDeliveryTrackerRequest createDeliveryTrackerRequest() {
        AdDeliveryTrackerRequest deliveryRequest = new AdDeliveryTrackerRequest();
        deliveryRequest.setAdvertisementId(4483);
        deliveryRequest.setBrowser("Chrome");
        deliveryRequest.setDeliveryId(DELIVERY_ID);
        deliveryRequest.setOs("iOS");
        deliveryRequest.setSite("http://super-dooper-news.com");
        deliveryRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:23").getTime());
        return deliveryRequest;
    }

    private AdClickTrackerRequest createClickTrackerRequest() {
        AdClickTrackerRequest clickTrackerRequest = new AdClickTrackerRequest();
        clickTrackerRequest.setDeliveryId(DELIVERY_ID);
        clickTrackerRequest.setClickId(CLICK_ID);
        clickTrackerRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:34").getTime());
        return clickTrackerRequest;
    }

    private AdInstallTrackerRequest createInstallTrackerRequest() {
        AdInstallTrackerRequest installRequest = new AdInstallTrackerRequest();
        installRequest.setClickId(CLICK_ID);
        installRequest.setInstallId("144cf0db-ba28-4c5f-8c9c-2bf11ee42988");
        installRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:34").getTime());
        return installRequest;
    }

    @DisplayName("[Service] delivery 까지 정상 테스트")
    @Test
    public void whenDeliveryTrackerRequestReceivedThenThatWillBeAccepted() throws Exception {
        AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
        assertThat(ingestService.ingestDeliveryTracker(deliveryRequest)).isEqualTo(true);
    }

    @DisplayName("[Service] delivery, click 까지 정상 테스트")
    @Test
    public void whenClickTrackerRequestReceivedWithValidDeliveryIdThenThatWillBeAccepted() throws Exception {
        AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
        ingestService.ingestDeliveryTracker(deliveryRequest);

        AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
        assertThat(ingestService.ingestClickTracker(clickTrackerRequest)).isEqualTo(true);
    }

    @DisplayName("[Service] delivery 없이 click 수행시 실패하는지 테스트")
    @Test
    public void whenClickTrackerRequestReceivedWithInValidDeliveryIdThenThatWillNotBeAccepted() throws Exception {
        AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
        assertThat(ingestService.ingestClickTracker(clickTrackerRequest)).isEqualTo(false);
    }

    @DisplayName("[Service] delivery, click, install 까지 정상되는지 테스트")
    @Test
    public void whenInstallTrackerRequestReceivedWithValidClickIdThenThatWillBeAccepted() throws Exception {
        AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
        ingestService.ingestDeliveryTracker(deliveryRequest);

        AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
        ingestService.ingestClickTracker(clickTrackerRequest);

        AdInstallTrackerRequest installRequest = createInstallTrackerRequest();
        assertThat(ingestService.ingestInstallTracker(installRequest)).isEqualTo(true);
    }

    @DisplayName("[Service] delivery, click 없이 install 수행시 실패되는지 테스트")
    @Test
    public void whenInstallTrackerRequestReceivedWithInValidClickIdThenThatWillNotBeAccepted() throws Exception {
        AdInstallTrackerRequest installRequest = createInstallTrackerRequest();
        assertThat(ingestService.ingestInstallTracker(installRequest)).isEqualTo(false);
    }
}