/**
 * @author gramcha
 * 07-Jul-2018 5:00:49 PM
 * 
 */
package uplus.redistest.service;

import uplus.redistest.domain.models.AdClickTrackerRequest;
import uplus.redistest.domain.models.AdDeliveryTrackerRequest;
import uplus.redistest.domain.models.AdInstallTrackerRequest;

public interface TrackerIngestService {
	boolean ingestDeliveryTracker(AdDeliveryTrackerRequest deliveryPayload);
	boolean ingestClickTracker(AdClickTrackerRequest clickPayload);
	boolean ingestInstallTracker(AdInstallTrackerRequest installPayload);
}
