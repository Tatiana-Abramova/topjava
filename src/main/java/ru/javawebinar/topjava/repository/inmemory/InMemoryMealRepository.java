package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private final Map<Integer, Object> locks = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.subList(0, 3).forEach(m -> save(m, 1));
        MealsUtil.meals.subList(3, 7).forEach(m -> save(m, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        synchronized (locks.computeIfAbsent(userId, u -> new Object())) {
            try {
                if (meal.isNew()) {
                    meal.setId(counter.incrementAndGet());
                    repository.put(meal.getId(), meal);
                    log.info("saved new meal with id={}", meal.getId());
                    return meal;
                } else {
                    return get(meal.getId(), userId) == null ? null : repository.compute(meal.getId(), (id, oldMeal) -> meal);
                }
            } finally {
                locks.remove(userId);
            }
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        if (get(id, userId) == null) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public Collection<MealTo> getAll(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll for userId={}", userId);
        return MealsUtil.getFilteredTos(
                repository.values().stream()
                        .filter(m -> m.getUserId() == userId)
                        .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                        .collect(Collectors.toList()),
                caloriesPerDay,
                startDate,
                endDate,
                startTime,
                endTime);
    }
}

