package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;

public class MealTo {

    private final int id;

    private final LocalDateTime dateTime;
    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(int id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime.format(DateTimeUtil.FORMATTER);
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }
}