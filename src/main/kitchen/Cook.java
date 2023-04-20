package main.kitchen;

import main.ConsoleHelper;
import main.statistic.StatisticManager;
import main.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private final String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;

    public LinkedBlockingQueue<Order> getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Cook(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void startCookingOrder(Order order){
        busy = true;

        ConsoleHelper.writeMessage(name + " Start cooking - " + order + ", cooking time " + order.getTotalCookingTime() + "min");

        StatisticManager.getInstance()
                .register(new CookedOrderEventDataRow(
                        order.getTablet().toString(),
                        this.name,
                        order.getTotalCookingTime()*60,
                        order.getDishes())
        );

        try {
            Thread.sleep(order.getTotalCookingTime() *10);
        } catch (InterruptedException e) {
        }

        setChanged();
        notifyObservers(order);

        busy = false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(10);
                if (!queue.isEmpty()) {
                    if (!this.isBusy()) {
                        this.startCookingOrder(queue.take());
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }
}
