package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ControllerUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/meals")
public class JspMealController extends BaseMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String get(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping(params = {"startDate", "endDate", "startTime", "endTime"})
    public String filteredGet(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                              @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                              Model model) {
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute("meal", newEntity());
        return "mealForm";
    }

    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        create(entityOf(request));
        return "redirect:/meals";
    }

    @GetMapping("/edit")
    public String editGet(HttpServletRequest request, Model model) {
        model.addAttribute("meal", get(ControllerUtil.getRequireParameter(request, "id")));
        return "mealForm";
    }

    @PostMapping("/edit")
    public String editPost(HttpServletRequest request) {
        update(entityOf(request), ControllerUtil.getRequireParameter(request, "id"));
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        delete(ControllerUtil.getRequireParameter(request, "id"));
        return "redirect:/meals";
    }

    protected Meal newEntity() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    protected Meal entityOf(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
    }
}
