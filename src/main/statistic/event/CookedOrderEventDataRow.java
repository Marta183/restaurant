package main.statistic.event;

import main.kitchen.Dish;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CookedOrderEventDataRow implements EventDataRow {
    private String tabletName;
    private String cookName;
    private int cookingTimeSeconds;
    private List<Dish> cookingDishes;
    private Date currentDate;

    public CookedOrderEventDataRow(String tabletName, String cookName, int cookingTimeSeconds, List<Dish> cookingDishes) {
        this.tabletName = tabletName;
        this.cookName = cookName;
        this.cookingTimeSeconds = cookingTimeSeconds;
        this.cookingDishes = cookingDishes;
        this.currentDate = new Date();
    }

    public String getCookName() {
        return cookName;
    }

    @Override
    public EventType getType() {
        return EventType.COOKED_ORDER;
    }

    @Override
    public Date getDate() {
        return currentDate;
    }

    public String getStartOfDay() {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(currentDate);
    }

    @Override
    public int getTime() {
        return cookingTimeSeconds;
    }
}
