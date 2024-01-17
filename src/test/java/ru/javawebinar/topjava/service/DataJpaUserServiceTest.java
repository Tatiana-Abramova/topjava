package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER_WITH_MEALS;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMealsEmpty() {
        User user = service.getWithMeals(100002);
        USER_MATCHER_WITH_MEALS.assertMatch(user, UserTestData.guest);
    }

    @Test
    public void getWithMeals() {
        User expected = UserTestData.user;
        expected.setMeals(MealTestData.meals);
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER_WITH_MEALS.assertMatch(user, expected);
    }
}
