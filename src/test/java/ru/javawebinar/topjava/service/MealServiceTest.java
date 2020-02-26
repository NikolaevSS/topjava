package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealServiceTest extends BaseServiceTest {
    @Autowired
    private MealService mealService;

    @Test
    public void testCorrectGet() {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        assertNotNull(meal);
        assertThat(meal).isEqualToComparingFieldByField(MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void testFailureGet() {
        mealService.get(MEAL_ID, -1);
    }

    @Test(expected = NotFoundException.class)
    public void testCorrectDelete() {
        mealService.delete(MEAL_ID, USER_ID);
        mealService.get(MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testFailureDelete() {
        mealService.delete(MEAL_ID, -1);
    }

    @Test
    public void testCorrectGetBetweenHalfOpen() {
        List<Meal> meals = mealService.getBetweenHalfOpen(FILTER_DATE, FILTER_DATE, USER_ID);
        assertThat(meals.size()).isEqualTo(USER_MEALS_COUNT_BY_DATE);
        assertTrue(meals.contains(MEAL));
    }

    @Test
    public void testFailureGetBetweenHalfOpen() {
        List<Meal> meals = mealService.getBetweenHalfOpen(FILTER_DATE, FILTER_DATE, -1);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void testCorrectGetAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertThat(meals.size()).isEqualTo(ALL_USER_MEALS_COUNT);
        assertTrue(meals.contains(MEAL));
    }

    @Test
    public void testFailureGetAll() {
        List<Meal> meals = mealService.getAll(-1);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void testCorrectUpdate() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertThat(updated).isEqualToComparingFieldByField(mealService.get(MEAL_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testFailureUpdate() {
        mealService.update(getUpdated(), -1);
    }

    @Test
    public void testCorrectCreate() {
        Meal newMeal = mealService.create(getNew(), USER_ID);
        assertThat(newMeal).isEqualToComparingFieldByField(mealService.get(newMeal.getId(), USER_ID));
    }
}