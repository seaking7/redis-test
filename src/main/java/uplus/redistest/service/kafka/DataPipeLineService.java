/**
 * @author gramcha
 * 08-Jul-2018 4:00:08 PM
 * 
 */
package uplus.redistest.service.kafka;

import uplus.redistest.domain.entities.ClickTracker;
import uplus.redistest.domain.entities.DeliveryTracker;
import uplus.redistest.domain.entities.InstallTracker;

public interface DataPipeLineService {
	void send(DeliveryTracker tracker);

	void send(ClickTracker tracker);

	void send(InstallTracker tracker);
}
