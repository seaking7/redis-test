package uplus.redistest.domain;

import java.time.LocalDateTime;

public class AdsViewStatus {

    public String adsNumber;
    public LocalDateTime adsFrequencyTime;
    public int viewCount;

    @Override
    public String toString() {
        return "AdsViewStatus{" +
                "adsNumber='" + adsNumber + '\'' +
                ", adsFrequencyTime=" + adsFrequencyTime +
                ", viewCount=" + viewCount +
                '}';
    }
}
