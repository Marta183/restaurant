package main.statistic;

import main.statistic.event.CookedOrderEventDataRow;
import main.statistic.event.EventDataRow;
import main.statistic.event.EventType;
import main.statistic.event.VideoSelectedEventDataRow;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticManager {

    private static StatisticManager instance = new StatisticManager();
    private StatisticStorage statisticStorage = new StatisticStorage();

    private StatisticManager() {
    }

    public static StatisticManager getInstance() {
        return instance;
    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    public Map<String, Long> calculateAdvertisementProfitByDays() {
        return statisticStorage.getEvents(EventType.SELECTED_VIDEOS)
                .stream()
                .map(obj -> (VideoSelectedEventDataRow) obj)
                .sorted((o1, o2) -> o2.getStartOfDay().compareTo(o1.getStartOfDay()))
                .collect(Collectors.groupingBy(VideoSelectedEventDataRow::getStartOfDay,
                        Collectors.mapping(VideoSelectedEventDataRow::getAmount, Collectors.summingLong(Long::longValue)))
                );
    }

    public Map<String, Map<String, Long>> calculateCookWorkloadByDays() {
        return statisticStorage.getEvents(EventType.COOKED_ORDER)
                .stream()
                .map(obj -> (CookedOrderEventDataRow) obj)
                .sorted((o1, o2) -> o2.getStartOfDay().compareTo(o1.getStartOfDay()))
                .collect(Collectors.groupingBy(CookedOrderEventDataRow::getStartOfDay,
                                Collectors.groupingBy(CookedOrderEventDataRow::getCookName,
                                        Collectors.mapping(CookedOrderEventDataRow::getTime, Collectors.summingLong(Integer::intValue)
                                )
                        )));
    }


    private class StatisticStorage {

        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        private StatisticStorage() {
            Arrays.stream(EventType.values()).forEach(k -> storage.put(k, new ArrayList<>()));
        }

        private void put(EventDataRow data) {
            EventType type = data.getType();
            if (!storage.containsKey(type))
                throw new UnsupportedOperationException();

            storage.get(type).add(data);
        }

        public List<EventDataRow> getEvents(EventType type) {
            if (!storage.containsKey(type))
                throw new UnsupportedOperationException();

            return storage.get(type);
        }
    }
}
