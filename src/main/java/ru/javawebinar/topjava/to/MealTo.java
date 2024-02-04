package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    private boolean excess;

    @JsonCreator
    public MealTo(
            @JsonProperty("id") Integer id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("description") String description,
            @JsonProperty("calories") int calories,
            @JsonProperty("excess") boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
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

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealTo mealTo)) return false;

        if (getCalories() != mealTo.getCalories()) return false;
        if (isExcess() != mealTo.isExcess()) return false;
        if (!getId().equals(mealTo.getId())) return false;
        if (!getDateTime().equals(mealTo.getDateTime())) return false;
        return getDescription().equals(mealTo.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDateTime().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getCalories();
        result = 31 * result + (isExcess() ? 1 : 0);
        return result;
    }
}
