package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-app-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_1_1, USER_ID);
        assertMatch(meal, userMeal1_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_MEAL_ID, USER_ID));
    }

    @Test
    public void getWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_1_1, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_1_0, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_1_0, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteWrongUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_1_0, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 29), LocalDate.of(2020, Month.JANUARY, 29), USER_ID);
        assertMatch(actual, userMeals1);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(ADMIN_ID);
        assertMatch(actual, adminMeals);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID_1_0, USER_ID), getUpdated());
    }

    @Test
    public void updateNotFound() {
        Meal meal = getUpdated();
        meal.setId(NOT_FOUND_MEAL_ID);
        assertThrows(NotFoundException.class, () -> service.update(meal, USER_ID));
    }

    @Test
    public void updateWrongUser() {
        Meal meal = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(meal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal meal = new Meal(userMeal1_0.getDateTime(), userMeal1_0.getDescription() + "test", userMeal1_0.getCalories() + 10);
        assertThrows(DuplicateKeyException.class, () -> service.create(meal, USER_ID));
    }
}