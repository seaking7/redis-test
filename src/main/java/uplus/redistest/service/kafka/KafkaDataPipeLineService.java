
package uplus.redistest.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import uplus.redistest.domain.entities.ClickTracker;
import uplus.redistest.domain.entities.DeliveryTracker;
import uplus.redistest.domain.entities.InstallTracker;

//@Service
public class KafkaDataPipeLineService implements DataPipeLineService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final static String DELIVERY_TOPIC = "delivery";
	private final static String CLICK_TOPIC = "click";
	private final static String INSTALL_TOPIC = "install";
	@Autowired
	private KafkaTemplate<String, DeliveryTracker> deliveryProducer;

	@Autowired
	private KafkaTemplate<String, ClickTracker> clickProducer;

	@Autowired
	private KafkaTemplate<String, InstallTracker> installProducer;

	@Override
	public void send(DeliveryTracker tracker) {
		deliveryProducer.send(DELIVERY_TOPIC, tracker.getDeliveryId(), tracker);
	}

	@Override
	public void send(ClickTracker tracker) {
		clickProducer.send(CLICK_TOPIC, tracker.getClickId(), tracker);
	}

	@Override
	public void send(InstallTracker tracker) {
		installProducer.send(INSTALL_TOPIC, tracker.getInstallId(), tracker);
	}

	// Below listeners are just to validate the kafka producer will be removed once
	// store-service(kafka consumer) implemented.
	//TODO: remove listeners from this service
	@KafkaListener(topics = DELIVERY_TOPIC)
	void deliveryConsumer(DeliveryTracker deliveryTrackerEntity, Acknowledgment acknowledgment) {
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived delivery tracker " + deliveryTrackerEntity.getDeliveryId());
		acknowledgment.acknowledge();
	}

	@KafkaListener(topics = CLICK_TOPIC)
	void deliveryConsumer(ClickTracker clickTrackerEntity, Acknowledgment acknowledgment) {
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived click tracker " + clickTrackerEntity.getClickId());
		acknowledgment.acknowledge();
	}

	@KafkaListener(topics = INSTALL_TOPIC)
	void deliveryConsumer(InstallTracker installTrackerEntity, Acknowledgment acknowledgment) {
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived install tracker " + installTrackerEntity.getInstallId());
		acknowledgment.acknowledge();
	}
}
