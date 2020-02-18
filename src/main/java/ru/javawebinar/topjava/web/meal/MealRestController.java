package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public Meal save(Meal meal) {
        return meal.isNew() ? mealService.create(meal) : mealService.update(meal);
    }

    public void delete(int id) {
        mealService.delete(id, authUserId());
    }

    public Meal get(int id) {
        return mealService.get(id, authUserId());
    }

    public List<Meal> getAll() {
        return mealService.getAll(authUserId());
    }

    public List<MealTo> getAllTos() {
        return MealsUtil.getTos(getAll(), authUserCaloriesPerDay());
    }

    public List<Meal> getFiltered(LocalDate dateStart, LocalDate dateEnd) {
        return mealService.getAll(authUserId(), dateStart, dateEnd);
    }

    public List<MealTo> getFilteredTos(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) {
        return MealsUtil.getFilteredTos(
                getFiltered(dateStart, dateEnd),
                authUserCaloriesPerDay(),
                Optional.ofNullable(timeStart).orElse(LocalTime.MIN),
                Optional.ofNullable(timeEnd).orElse(LocalTime.MAX)
        );
    }
}