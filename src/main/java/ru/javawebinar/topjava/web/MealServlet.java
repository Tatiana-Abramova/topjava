package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealFrom;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String STORAGE_TYPE_ENV = "STORAGE_TYPE";

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            storage = (Storage) Class.forName(System.getenv(STORAGE_TYPE_ENV)).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET: " + request.getRequestURL() + "?" + request.getQueryString());
        Integer id = getId(request);
        String action = request.getParameter("action");

        if (action == null) {
            log.debug("Get meal list");
            request.setAttribute("meals", MealsUtil.getMealToList(storage.getAllMeal(), storage.getCaloriesPerDay()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        MealFrom mealFrom;
        switch (action) {
            case "delete":
                log.debug("Delete meal: id = " + id);
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                if (id == null) {
                    mealFrom = new MealFrom();
                } else {
                    log.debug("Get meal: id = " + id);
                    /* Я не уверена, что вообще нужен объект MealFrom
                     * Но с другой стороны, если использовать Meal, будет ли правильно разрешать создавать его без id? */
                    Meal meal = storage.get(id);
                    mealFrom = new MealFrom(meal.getDateTime(), meal.getDescription(), meal.getCalories());
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("id", id);
        request.setAttribute("meal", mealFrom);
        request.getRequestDispatcher(
                ("/edit.jsp")
        ).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("POST: " + request.getRequestURL() + "?" + request.getQueryString());
        request.setCharacterEncoding("UTF-8");
        Integer id = getId(request);
        MealFrom meal = new MealFrom(
                DateTimeUtil.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (id == null) {
            log.debug("Save meal: " + meal);
            storage.save(meal);
        } else {
            log.debug("Update meal: " + meal);
            storage.update(id, meal);
        }
        response.sendRedirect("meals");
    }

    private Integer getId(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id == null || id.trim().length() == 0) {
            return null;
        }
        return Integer.parseInt(id);
    }
}
