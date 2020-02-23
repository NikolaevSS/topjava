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

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public Meal create(Meal meal) {
        return mealService.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        mealService.update(meal, authUserId());
    }

    public void delete(int id) {
        mealService.delete(id, authUserId());
    }

    public Meal get(int id) {
        return mealService.get(id, authUserId());
    }

    public List<MealTo> getAllTos() {
        return MealsUtil.getTos(mealService.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredTos(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) {
        return MealsUtil.getFilteredTos(
                mealService.getAllBetweenDates(authUserId(), dateStart, dateEnd),
                authUserCaloriesPerDay(),
                Optional.ofNullable(timeStart).orElse(LocalTime.MIN),
                Optional.ofNullable(timeEnd).orElse(LocalTime.MAX)
        );
    }
}