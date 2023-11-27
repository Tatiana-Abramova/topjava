package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealMemoryStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final int CALORIES_PER_DAY = 2000;

    private final MealStorage storage = new MealMemoryStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        addTestMeal();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET: {}?{}", request.getRequestURL(), request.getQueryString());
        String action = request.getParameter("action");

        if (action == null) {
            showMeal(request, response);
            return;
        }

        Integer id = getId(request);
        Meal meal;
        switch (action) {
            case "delete":
                log.debug("Delete meal: id = {}", id);
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                if (id == null) {
                    meal = new Meal();
                } else {
                    log.debug("Get meal: id = {}", id);
                    meal = storage.get(id);
                }
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(("/editMeal.jsp")).forward(request, response);
                break;
            default:
                showMeal(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("POST: {}?{}", request.getRequestURL(), request.getQueryString());
        request.setCharacterEncoding("UTF-8");
        Integer id = getId(request);
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime"), DateTimeUtil.FORMATTER),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (id != null) {
            meal.setId(id);
        }
        log.debug("Save meal: {}", meal);
        storage.save(meal);
        response.sendRedirect("meals");
    }

    private Integer getId(HttpServletRequest request) {
        String id = request.getParameter("id");
        return (id == null || id.isEmpty()) ? null : Integer.parseInt(id);
    }

    private void showMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Get meal list");
        request.setAttribute("meals", MealsUtil.getMealToList(storage.getAll(), CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void addTestMeal() {
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
}
