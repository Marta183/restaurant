package main;

import java.util.List;
import java.util.Random;

public class RandomOrderGeneratorTask implements Runnable {

    private List<Tablet> tablets;
    private int interval;

    public RandomOrderGeneratorTask(List<Tablet> tablets, int interval) {
        this.interval = interval;
        this.tablets = tablets;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //int randomIndex = (int) (Math.random() * tablets.size());
                int randomIndex = new Random().nextInt(tablets.size()-1);
                Tablet randomTablet = tablets.get(randomIndex);
                randomTablet.createTestOrder();
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
        }
    }
}
