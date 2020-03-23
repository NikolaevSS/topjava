package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.BaseController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Objects.nonNull;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController extends BaseController {
    private final MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    @GetMapping("/meals")
    public String mealsGet(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        List<Meal> meals;
        if (nonNull(startDate) || nonNull(endDate)) {
            meals = service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());
        } else {
            meals = service.getAll(SecurityUtil.authUserId());
        }
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> mealTos;
        if (nonNull(startTime) || nonNull(endTime)) {
            mealTos = MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        } else {
            mealTos = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        }
        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("/meals/add")
    public String addGet(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "add");
        return "mealForm";
    }

    @PostMapping("/meals/add")
    public String addPost(HttpServletRequest request) {
        final Meal meal = mealOf(request);
        checkNew(meal);
        service.create(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/meals/edit")
    public String editGet(HttpServletRequest request, Model model) {
        final Meal meal = service.get(getRequireParameter(request, "id"), SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        model.addAttribute("action", "edit");
        return "mealForm";
    }

    @PostMapping("/meals/edit")
    public String editPost(HttpServletRequest request) {
        final Meal meal = mealOf(request);
        assureIdConsistent(meal, getRequireParameter(request, "id"));
        service.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/meals/delete")
    public String delete(HttpServletRequest request) {
        service.delete(getRequireParameter(request, "id"), SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    private Meal mealOf(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
    }
}
