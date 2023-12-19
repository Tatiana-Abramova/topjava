package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_1_0 = START_SEQ + 3;
    public static final int MEAL_ID_1_1 = START_SEQ + 4;
    public static final int NOT_FOUND_MEAL_ID = 1;
    public static final Meal MEAL_1_0 = new Meal(MEAL_ID_1_0, LocalDateTime.of(2020, Month.JANUARY, 29, 0, 0), "Еда", 100);
    public static final Meal MEAL_1_1 = new Meal(MEAL_ID_1_1, LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак", 1000);

    public static final List<Meal> userMeals1 = Arrays.asList(MEAL_1_1, MEAL_1_0);

    public static final List<Meal> userMeals2 = Arrays.asList(
            new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500)
    );

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(START_SEQ + 11, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
            new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100)
    );

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2023, Month.DECEMBER, 30, 10, 0), "Новая еда", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal();
        updated.setId(MEAL_ID_1_0);
        updated.setDateTime(LocalDateTime.of(2023, 12, 22, 11, 11));
        updated.setDescription("updated");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
