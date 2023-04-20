package main.kitchen;

import main.ConsoleHelper;
import main.Tablet;

import java.io.IOException;
import java.util.List;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        initDishes();
        //ConsoleHelper.writeMessage(toString());
    }

    public Tablet getTablet() {
        return tablet;
    }

    public int getTotalCookingTime() {
        int result = 0;
        for (Dish dish : dishes) {
            result += dish.getDuration();
        }
        return result;
    }

    public boolean isEmpty() {
        return dishes.isEmpty();
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    @Override
    public String toString() {
        if (dishes.isEmpty())
            return "";
        return "Your order: " + dishes
                + " of " + tablet.toString()
                + ", cooking time " + getTotalCookingTime() + "min";
    }

    protected void initDishes() throws IOException {
        this.dishes = ConsoleHelper.getAllDishesForOrder();
    }
}
