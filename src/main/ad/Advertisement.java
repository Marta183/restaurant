package main.ad;

public class Advertisement implements Comparable {
    private Object content;
    private String name;
    private long initialAmount;
    private long amountPerOneDisplaying;
    private long amountPerSecond;
    private int hits;
    private int duration;

    public Advertisement(Object content, String name, long initialAmount, int hits, int duration) {
        this.content = content;
        this.name = name;
        this.initialAmount = initialAmount;
        this.hits = hits;
        this.duration = duration;
        if (hits != 0) {
            this.amountPerOneDisplaying = initialAmount / hits;
            this.amountPerSecond = 1000 * this.amountPerOneDisplaying / duration * 1000;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getAmountPerOneDisplaying() {
        return amountPerOneDisplaying;
    }

    public void setAmountPerOneDisplaying(long amountPerOneDisplaying) {
        this.amountPerOneDisplaying = amountPerOneDisplaying;
    }

    public long getAmountPerSecond() {
        return amountPerSecond;
    }

    public int getHits() {
        return hits;
    }

    public void revalidate() throws UnsupportedOperationException {
        if (hits == 0)
            throw new UnsupportedOperationException();
        hits--;
    }

    public boolean isActive() {
        return hits > 0;
    }

    @Override
    public int compareTo(Object o) {
        Advertisement ad = (Advertisement) o;

        int result = Long.compare(ad.amountPerOneDisplaying, this.amountPerOneDisplaying); //max
        if (result != 0) return result;

        result = Long.compare(this.amountPerSecond, ad.amountPerSecond); // min
        return result;
    }
}
