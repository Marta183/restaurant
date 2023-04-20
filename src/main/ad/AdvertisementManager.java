package main.ad;

import main.statistic.StatisticManager;
import main.statistic.event.VideoSelectedEventDataRow;

import java.util.*;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;
    private PlayList optimalPlayList;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        if (storage.list().isEmpty())
            throw new NoVideoAvailableException();

//        Map<PlayList, Long> playListsWithProfit = pickUpSuitablePlayLists();
//        optimalPlayList = Collections.max(playListsWithProfit.entrySet(), Map.Entry.comparingByValue()).getKey();
//        displayAdvertisements();

        this.totalTimeSecondsLeft = Integer.MAX_VALUE;
        obtainOptimalVideoSet(new ArrayList<Advertisement>(), timeSeconds, 0L);

        StatisticManager.getInstance().register(
                new VideoSelectedEventDataRow(optimalVideoSet, maxAmount, timeSeconds - totalTimeSecondsLeft)
        );

        displayAdvertisement();
    }

    //recursion
    private long maxAmount;
    private List<Advertisement> optimalVideoSet;
    private int totalTimeSecondsLeft;

    private void obtainOptimalVideoSet(List<Advertisement> totalList, int currentTimeSecondsLeft, long currentAmount) {
        if (currentTimeSecondsLeft < 0) {
            return;
        } else if (currentAmount > maxAmount
                || currentAmount == maxAmount && (totalTimeSecondsLeft > currentTimeSecondsLeft
                    || totalTimeSecondsLeft == currentTimeSecondsLeft && totalList.size() < optimalVideoSet.size())) {
            this.totalTimeSecondsLeft = currentTimeSecondsLeft;
            this.optimalVideoSet = totalList;
            this.maxAmount = currentAmount;
            if (currentTimeSecondsLeft == 0) {
                return;
            }
        }

        ArrayList<Advertisement> tmp = getActualAdvertisements();
        tmp.removeAll(totalList);
        for (Advertisement ad : tmp) {
            if (!ad.isActive()) continue;
            ArrayList<Advertisement> currentList = new ArrayList<>(totalList);
            currentList.add(ad);
            obtainOptimalVideoSet(currentList, currentTimeSecondsLeft - ad.getDuration(), currentAmount + ad.getAmountPerOneDisplaying());
        }
    }

    private ArrayList<Advertisement> getActualAdvertisements() {
        ArrayList<Advertisement> advertisements = new ArrayList<>();
        for (Advertisement ad : storage.list()) {
            if (ad.isActive()) {
                advertisements.add(ad);
            }
        }
        Collections.sort(advertisements);
        return advertisements;
    }

    private void displayAdvertisement() {
        //TODO displaying
        if (optimalVideoSet == null || optimalVideoSet.isEmpty()) {
            throw new NoVideoAvailableException();
        }

        optimalVideoSet.sort((o1, o2) -> {
            long l = o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying();
            return (int) (l != 0 ? l : o2.getDuration() - o1.getDuration());
        });

        for (Advertisement ad : optimalVideoSet) {
            displayInPlayer(ad);
            ad.revalidate();
        }
    }

    private void displayInPlayer(Advertisement ad) {
        //TODO get Player instance and display content
        System.out.println(ad.getName() + " is displaying... "
                + ad.getAmountPerOneDisplaying() + ", "
                + (1000 * ad.getAmountPerOneDisplaying() / ad.getDuration()));
    }



    ///////////////////////////////////////////////////////
    private Map<PlayList, Long> pickUpSuitablePlayLists() {
        Map<PlayList, Long> playListsMap = new TreeMap<>();

        ArrayList<Advertisement> actualAdvertisements = getActualAdvertisements();

        for (Advertisement currVideo: actualAdvertisements) {
            PlayList videoList = new PlayList(timeSeconds);
            if (videoList.canAddVideo(currVideo))
                videoList.addVideo(currVideo);
            else continue;

            for (Advertisement videoToCheck : actualAdvertisements) {
                if (videoList.canAddVideo(videoToCheck))
                    videoList.addVideo(videoToCheck);
            }
            playListsMap.put(videoList, videoList.totalAmount);
        }

        return playListsMap;
    }

    private void displayAdvertisements() {
        if (optimalPlayList == null || optimalPlayList.videos.isEmpty())
            throw new NoVideoAvailableException();

        Collections.sort(optimalPlayList.videos);

        for (Advertisement video : optimalPlayList.videos) {
            displayInPlayer(video);
            video.revalidate();
        }
    }


    private static class PlayList implements Comparable {
        private final List<Advertisement> videos = new ArrayList<>();
        private final int limitDuration;
        private int totalDuration;
        private long totalAmount;

        public PlayList(int limitDuration) {
            this.limitDuration = limitDuration;
        }

        private void addVideo(Advertisement video) {
            videos.add(video);
            totalAmount += video.getAmountPerOneDisplaying();
            totalDuration += video.getDuration();
        }

        public boolean canAddVideo(Advertisement video) {
            if (videos.contains(video))
                return false;
            return totalDuration + video.getDuration() <= limitDuration;
        }

        @Override
        public int compareTo(Object o) {
            PlayList playList = (PlayList) o;

            int result = Long.compare(playList.totalAmount, this.totalAmount); //max
            if (result != 0) return result;

            result = Integer.compare(playList.totalDuration, this.totalDuration); // max
            if (result != 0) return result;

            result = Integer.compare(this.videos.size(), playList.videos.size()); // min
            return result;
        }
    }

}
