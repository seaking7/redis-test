/**
 * @author gramcha
 * 07-Jul-2018 5:00:49 PM
 * 
 */
package uplus.redistest.service;

import uplus.redistest.models.AdClickTrackerRequest;
import uplus.redistest.models.AdDeliveryTrackerRequest;
import uplus.redistest.models.AdInstallTrackerRequest;

public interface TrackerIngestService {
	boolean ingestDeliveryTracker(AdDeliveryTrackerRequest deliveryPayload);
	boolean ingestClickTracker(AdClickTrackerRequest clickPayload);
	boolean ingestInstallTracker(AdInstallTrackerRequest installPayload);
}
