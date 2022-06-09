/**
 * @author gramcha
 * 08-Jul-2018 4:00:08 PM
 * 
 */
package uplus.redistest.service;

import uplus.redistest.entities.ClickTracker;
import uplus.redistest.entities.DeliveryTracker;
import uplus.redistest.entities.InstallTracker;

public interface DataPipeLineService {
	void send(DeliveryTracker tracker);

	void send(ClickTracker tracker);

	void send(InstallTracker tracker);
}
