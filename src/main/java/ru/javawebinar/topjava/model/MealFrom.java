package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;

public class MealFrom {

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    public MealFrom() {

    }

    public MealFrom(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public String getDateTime() {
        return dateTime != null ? dateTime.format(DateTimeUtil.FORMATTER) : null;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "MealFrom{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
