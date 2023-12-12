package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1.forEach(m -> save(m, 1));
        MealsUtil.meals2.forEach(m -> save(m, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> meals = repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            meals.put(meal.getId(), meal);
            log.info("saved new meal with id={}", meal.getId());
            return meal;
        } else {
            Map<Integer, Meal> meals = repository.get(userId);
            return meals == null ? null :
                    meals.replace(meal.getId(), meal) == null ? null : meal;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll for userId={}, startDate={}, endDate={}", userId, startDate, endDate);
        return getFilteredByPredicate(userId, m -> DateTimeUtil.isBetweenDates(m.getDate(), startDate, endDate));
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        return getFilteredByPredicate(userId, m -> true);
    }

    private List<Meal> getFilteredByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? Collections.emptyList() : meals.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

