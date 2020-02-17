package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.util.List;

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

    public List<Meal> getFiltered(LocalDate dateStart, LocalDate dateEnd) {
        return mealService.getAll(authUserId(), dateStart, dateEnd);
    }
}