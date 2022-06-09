package uplus.redistest.repos;


import uplus.redistest.entities.ClickTracker;

public interface AdClickTrackerRepository {
	void save(ClickTracker clickTracker);
	ClickTracker findByClickId(String clickId);
	void delete(String clickId);
}
