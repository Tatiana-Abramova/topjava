package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.mapper.MealMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        List<MealTo> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo2.forEach(System.out::println);

        new ArrayList<>(System.getenv().entrySet())
                .forEach(System.out::println);
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();
        for (Meal meal : meals) {
            dailyCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<MealTo> result = new ArrayList<>();
        for (Meal meal : meals) {
            if (DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean exceed = dailyCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                MealTo MealTo = MealMapper.getMealTo(meal, exceed);
                result.add(MealTo);
            }
        }
        return result;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return filteredByStreams(meals, caloriesPerDay, m -> DateTimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime));
    }

    public static List<MealTo> getMealToList(Collection<Meal> meals, int caloriesPerDay) {
        return filteredByStreams(new ArrayList<>(meals), caloriesPerDay, m -> true);
    }

    private static List<MealTo> filteredByStreams(List<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> dailyCalories = meals.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getDateTime().toLocalDate(),
                        Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
                .filter(filter)
                .map(m -> MealMapper.getMealTo(m, dailyCalories.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
