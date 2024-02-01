package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping(path = "/delete")
    public String deleteMeal(@RequestParam int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping(path = "/create")
    public String getCreateForm(Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("new meal form {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(path = "/create")
    public String create(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam LocalDateTime dateTime,
            @RequestParam String description,
            @RequestParam int calories, HttpServletRequest request) throws UnsupportedEncodingException {
        super.create(new Meal(dateTime, description, calories));
        return "redirect:/meals";
    }

    @GetMapping(path = "/update")
    public String getUpdateForm(@RequestParam int id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = super.get(id);
        log.info("fill meal form {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(path = "/update")
    public String update(
            @RequestParam int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam LocalDateTime dateTime,
            @RequestParam String description,
            @RequestParam int calories) throws UnsupportedEncodingException {
        super.update(new Meal(id, dateTime, description, calories), id);
        return "redirect:/meals";
    }

    @GetMapping(path = "filter")
    public String getBetween(@Nullable @RequestParam String startDate, @Nullable @RequestParam String startTime,
                             @Nullable @RequestParam String endDate, @Nullable @RequestParam String endTime,
                             Model model) {
        model.addAttribute(
                "meals",
                super.getBetween(parseLocalDate(startDate), parseLocalTime(startTime), parseLocalDate(endDate), parseLocalTime(endTime)));
        return "/meals";
    }
}
