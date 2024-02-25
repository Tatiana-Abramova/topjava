package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.validation.constraints.*;
import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class MealTo extends BaseTo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @PastOrPresent
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 3, max = 100)
    private String description;

    @NotNull
    @Positive
    private Integer calories;

    private boolean excess;

    public MealTo() {

    }

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
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

    /*
     * Не поняла вот это как применить в задании для уже готового объекта:
     * "Напомню, что параметры методов контроллера (в том числе собранные в объекты через Binding) парсятся конверторами спринга (@DateTimeFormat)"
     * Сделала преобразование через сеттер.
     * */
    public void setDateTime(String dateTime) {
        this.dateTime = DateTimeUtil.parseLocalDateTime(dateTime);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return calories == mealTo.calories &&
                excess == mealTo.excess &&
                Objects.equals(id, mealTo.id) &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
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
}
