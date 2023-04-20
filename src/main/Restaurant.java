package main;

import main.kitchen.Cook;
import main.kitchen.Order;
import main.kitchen.Waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {

    private static final int ORDER_CREATING_INTERVAL = 100;
    private static final List<Tablet> tablets = new ArrayList<>();
    private static final LinkedBlockingQueue<Order> ORDER_QUEUE = new LinkedBlockingQueue<>(200);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i+1);
            tablet.setQueue(ORDER_QUEUE);
            tablets.add(tablet);
        }

        Waiter waiter1 = new Waiter();

        Cook cook1 = new Cook("Italian cook");
        cook1.setQueue(ORDER_QUEUE);
        cook1.addObserver(waiter1);

        Cook cook2 = new Cook("Spanish cook");
        cook2.setQueue(ORDER_QUEUE);
        cook2.addObserver(waiter1);

        Thread cook11 = new Thread(cook1);
        cook11.start();
        Thread cook22 = new Thread(cook2);
        cook22.start();
        Thread thread = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        thread.start();

        try {
            Thread.sleep(1000);
            thread.interrupt();
            thread.join();
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }

        DirectorTablet directorTablet = new DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
    }

}
