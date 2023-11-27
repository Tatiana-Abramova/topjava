package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealStorage {

    Collection<Meal> getAll();

    Meal save(Meal meal);

    void delete(int id);

    Meal get(int id);
}
