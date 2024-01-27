package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping(params = "!action")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping(params = "action=delete") // не поняла как здесь можно обойтись без параметра action
    public String delete(@RequestParam Integer id) {
        super.delete(id);
        return "redirect:meals";
    }

    @GetMapping(params = {"action=create"})
    public String getCreateForm(Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("new meal form {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(params = {"id="})
    public String create(
            @RequestParam String dateTime,
            @RequestParam String description,
            @RequestParam Integer calories) throws UnsupportedEncodingException {
        super.create(new Meal(LocalDateTime.parse(dateTime), description, calories));
        return "redirect:meals";
    }

    @GetMapping(params = {"action=update"})
    public String getUpdateForm(@RequestParam Integer id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = super.get(id);
        log.info("fill meal form {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String update(
            @RequestParam Integer id,
            @RequestParam String dateTime,
            @RequestParam String description, // кодировка
            @RequestParam Integer calories) throws UnsupportedEncodingException {
        super.update(new Meal(id, LocalDateTime.parse(dateTime), description, calories), id);
        return "redirect:meals";
    }

    @GetMapping(params = {"action=filter"})
    public String getBetween(@Nullable @RequestParam String startDate, @Nullable @RequestParam String startTime,
                             @Nullable @RequestParam String endDate, @Nullable @RequestParam String endTime,
                             Model model) {
        model.addAttribute(
                "meals",
                super.getBetween(parseLocalDate(startDate), parseLocalTime(startTime), parseLocalDate(endDate), parseLocalTime(endTime)));
        return "meals";
    }
}
