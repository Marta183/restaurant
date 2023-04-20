package main.ad;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementStorage {

    private static class InstanceHolder {
        private static final AdvertisementStorage ourInstance = new AdvertisementStorage();
    }

    public static AdvertisementStorage getInstance() {
        return InstanceHolder.ourInstance;
    }
    private final List<Advertisement> videos = new ArrayList<>();

    private AdvertisementStorage() {
        Object someContent = new Object();
        videos.add(new Advertisement(someContent, "1 Video", 100, 1, 20 * 60)) ;//15 min   5 UA
        videos.add(new Advertisement(someContent, "2 Video", 400, 2, 10 * 60));   //10 min  4 UA
        videos.add(new Advertisement(someContent, "3 Video", 500, 3, 5 * 60)); // 3 min   100 UA
        videos.add(new Advertisement(someContent, "4 Video", 100, 4, 10 * 60));   //10 min 10 UA
        videos.add(new Advertisement(someContent, "5 Video", 150, 5, 15 * 60));   //10 min 10 UA
    }

    public List<Advertisement> list() {
        return videos;
    }

    public void add(Advertisement advertisement) {
        videos.add(advertisement);
    }
}
