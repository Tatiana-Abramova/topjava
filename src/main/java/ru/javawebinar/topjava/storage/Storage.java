package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealFrom;

import java.util.Collection;

public interface Storage {

    Collection<Meal> getAllMeal();

    int getCaloriesPerDay();

    void save(MealFrom meal);

    void update(Integer id, MealFrom meal);

    void delete(Integer id);

    Meal get(Integer id);
}
