package main;

import main.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsoleHelper {
    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return bufferedReader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> selectedDishes = new ArrayList<>();
        writeMessage("Please choose a dish from the list: " + Dish.allDishesToString() + "\n or type 'exit' to complete the order");
        while (true) {
            String dishName = readString().toUpperCase(Locale.ROOT);
            if (dishName.equals("EXIT"))
                break;

            try {
                Dish dish = Dish.valueOf(dishName.toUpperCase(Locale.ROOT));
                selectedDishes.add(dish);
                writeMessage(dishName + " has been successfully added to your order");
            } catch (IllegalArgumentException e) {
                writeMessage(dishName + " hasn't been detected");
            }
        }
        return selectedDishes;
    }

}
