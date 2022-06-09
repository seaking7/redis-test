package uplus.redistest.repos;


import uplus.redistest.domain.entities.ClickTracker;

public interface AdClickTrackerRepository {
	void save(ClickTracker clickTracker);
	ClickTracker findByClickId(String clickId);
	void delete(String clickId);
}
