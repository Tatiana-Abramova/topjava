package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.util.*;

public class UserMealPerDay {

    private final LocalDate day;

    private final Set<UserMeal> meals = new TreeSet<>();

    private int caloriesPerDay = 0;

    public UserMealPerDay(LocalDate day) {
        this.day = day;
    }

    public Set<UserMeal> getMeals() {
        return meals;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void addMeal(UserMeal meal) {
        meals.add(meal);
        caloriesPerDay += meal.getCalories();
    }
}
