package main.ad;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager instance = new StatisticAdvertisementManager();
    private final AdvertisementStorage adStorage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager() {}

    public static StatisticAdvertisementManager getInstance() {
        return instance;
    }

    public List<Advertisement> getAdvertisements(boolean isActive) {
        return adStorage.list().stream()
                .filter(ad -> ad.isActive() == isActive)
                .sorted(((o1, o2) -> o1.getName().toLowerCase(Locale.ENGLISH).compareTo(o2.getName().toLowerCase(Locale.ENGLISH))))
                .collect(Collectors.toList());
    }

}
