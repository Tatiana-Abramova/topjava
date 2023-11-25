package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealFrom;
import ru.javawebinar.topjava.model.mapper.MealMapper;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryStorage implements Storage {

    private static final int CALORIES_PER_DAY = 2000;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private final ConcurrentLinkedDeque<Meal> meals = new ConcurrentLinkedDeque<>(Arrays.asList(
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    ));

    @Override
    public Collection<Meal> getAllMeal() {
        return meals;
    }

    @Override
    public int getCaloriesPerDay() {
        return CALORIES_PER_DAY;
    }

    @Override
    public void save(MealFrom meal) {
        meals.add(MealMapper.getMeal(getId(), meal));
    }

    @Override
    public void update(Integer id, MealFrom meal) {
        getOpt(id).ifPresent(m -> {
            m.setDateTime(DateTimeUtil.parse(meal.getDateTime()));
            m.setDescription(meal.getDescription());
            m.setCalories(meal.getCalories());
        });
    }

    @Override
    public void delete(Integer id) {
        meals.removeIf(m -> id.equals(m.getId()));
    }

    @Override
    public Meal get(Integer id) {
        return getOpt(id).orElse(null);
    }

    private Optional<Meal> getOpt(Integer id) {
        return meals.stream()
                .filter(m -> m.getId().equals(id))
                .findAny();
    }

    private int getId() {
        return counter.incrementAndGet();
    }
}
