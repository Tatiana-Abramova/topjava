package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMemoryStorage implements MealStorage {
    private final AtomicInteger counter = new AtomicInteger(0);

    private final ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public Collection<Meal> getAll() {
        return Collections.unmodifiableCollection(meals.values());
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(getId());
        }
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    private int getId() {
        return counter.incrementAndGet();
    }
}
