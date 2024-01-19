package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator("registered", "roles", "meals");
    public static final MatcherFactory.Matcher<User> USER_MATCHER_WITH_MEALS = MatcherFactory.usingIgnoringFieldsComparator("registered", "roles", "meals.user", "meals.userId");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int USER_WITH_MEALS_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");
    public static final User userWithEmptyMeals = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest", DEFAULT_CALORIES_PER_DAY, true, new Date(), Collections.emptyList(), Collections.emptyList());
    public static final User userWithMeals = new User(USER_ID, "User", "user@yandex.ru", "password", DEFAULT_CALORIES_PER_DAY, true, new Date(), List.of(Role.USER), MealTestData.meals);


    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER), null);
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }
}
