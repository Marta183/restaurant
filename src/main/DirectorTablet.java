package main;

import main.ad.StatisticAdvertisementManager;
import main.statistic.StatisticManager;

import java.util.Locale;
import java.util.Map;

public class DirectorTablet {
    public void printAdvertisementProfit() {
        Map<String, Long> profitByDays = StatisticManager.getInstance().calculateAdvertisementProfitByDays();
        for (Map.Entry<String, Long> entry : profitByDays.entrySet()) {
            double amount = 1.0 * entry.getValue() / 100;
            System.out.println(entry.getKey() + " - " + String.format(Locale.ENGLISH, "%.2f", amount));
        }
        Long totalSum = profitByDays.values().stream()
                .mapToLong(longValue -> longValue)
                .sum();
        System.out.println("Total - " + String.format(Locale.ENGLISH, "%.2f", 1.0*totalSum/100));
    }

    public void printCookWorkloading() {
        Map<String, Map<String, Long>> cooksWorkload = StatisticManager.getInstance().calculateCookWorkloadByDays();
        for (Map.Entry<String, Map<String, Long>> entryDate : cooksWorkload.entrySet()) {
            System.out.println(entryDate.getKey());

            for (Map.Entry<String, Long> entryCook : entryDate.getValue().entrySet()) {
                System.out.println(entryCook.getKey() + " - " + ((entryCook.getValue() + 59) / 60) + " min");
            }
            System.out.println();
        }
    }

    public void printActiveVideoSet() {
        StatisticAdvertisementManager manager = StatisticAdvertisementManager.getInstance();
        manager.getAdvertisements(true).forEach(ad -> System.out.println(ad.getName() + " - " + ad.getHits()));
    }

    public void printArchivedVideoSet() {
        StatisticAdvertisementManager manager = StatisticAdvertisementManager.getInstance();
        manager.getAdvertisements(false).forEach(ad -> System.out.println(ad.getName()));
    }
}
