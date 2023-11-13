package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealPerDay;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo2.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, UserMealPerDay> dailyMeals = new TreeMap<>(Collections.reverseOrder());
        for (UserMeal meal : meals) {
            LocalDate day = meal.getDateTime().toLocalDate();
            dailyMeals.putIfAbsent(day, new UserMealPerDay(day));
            dailyMeals.get(day).addMeal(meal);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (Map.Entry<LocalDate, UserMealPerDay> entry : dailyMeals.entrySet()) {
            UserMealPerDay mealPerDay = entry.getValue();
            for (UserMeal meal : mealPerDay.getMeals()) {
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    boolean exceed = mealPerDay.getCaloriesPerDay() > caloriesPerDay;
                    UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal, exceed);
                    result.add(mealWithExcess);
                }
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        meals.sort(Comparator.comparing(UserMeal::getDateTime).reversed());
        Map<LocalDate, Integer> exceeded = meals.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                m -> m.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories)),
                        m -> {
                            m.values().removeIf(c -> c > caloriesPerDay);
                            return m;
                        }
                ));

        return meals.stream()
                .map((m) -> new UserMealWithExcess(m, exceeded.containsKey(m.getDateTime().toLocalDate())))
                .filter((m) -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}
