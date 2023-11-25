package ru.javawebinar.topjava.model.mapper;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealFrom;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

public class MealMapper {

    public static MealTo getMealTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static Meal getMeal(int id, MealFrom meal) {
        return new Meal(id, DateTimeUtil.parse(meal.getDateTime()), meal.getDescription(), meal.getCalories());
    }
}
