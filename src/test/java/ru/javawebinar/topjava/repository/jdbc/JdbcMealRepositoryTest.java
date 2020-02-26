package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class JdbcMealRepositoryTest extends BaseJdbcTest {
    @Autowired
    private JdbcMealRepository jdbcMealRepository;

    @Test
    public void testCorrectGet() {
        Meal meal = jdbcMealRepository.get(MEAL_ID, USER_ID);
        assertNotNull(meal);
        assertThat(meal).isEqualToComparingFieldByField(MEAL);
    }

    @Test
    public void testFailureGet() {
        assertNull(jdbcMealRepository.get(MEAL_ID, -1));
    }

    @Test
    public void testCorrectDelete() {
        assertTrue(jdbcMealRepository.delete(MEAL_ID, USER_ID));
    }

    @Test
    public void testFailureDelete() {
        assertFalse(jdbcMealRepository.delete(MEAL_ID, -1));
    }

    @Test
    public void testCorrectGetBetweenHalfOpen() {
        List<Meal> meals = jdbcMealRepository.getBetweenHalfOpen(FILTER_DATE_TIME_START, FILTER_DATE_TIME_END, USER_ID);
        assertThat(meals.size()).isEqualTo(USER_MEALS_COUNT_BY_DATE);
        assertTrue(meals.contains(MEAL));
    }

    @Test
    public void testFailureGetBetweenHalfOpen() {
        List<Meal> meals = jdbcMealRepository.getBetweenHalfOpen(FILTER_DATE_TIME_START, FILTER_DATE_TIME_END, -1);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void testCorrectGetAll() {
        List<Meal> meals = jdbcMealRepository.getAll(USER_ID);
        assertThat(meals.size()).isEqualTo(ALL_USER_MEALS_COUNT);
        assertTrue(meals.contains(MEAL));
    }

    @Test
    public void testFailureGetAll() {
        List<Meal> meals = jdbcMealRepository.getAll(-1);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void testCorrectUpdate() {
        Meal updated = getUpdated();
        jdbcMealRepository.save(updated, USER_ID);
        assertThat(updated).isEqualToComparingFieldByField(jdbcMealRepository.get(MEAL_ID, USER_ID));
    }

    @Test
    public void testFailureUpdate() {
        assertNull(jdbcMealRepository.save(getUpdated(), -1));
    }

    @Test
    public void testCorrectCreate() {
        Meal newMeal = jdbcMealRepository.save(getNew(), USER_ID);
        assertThat(newMeal).isEqualTo(jdbcMealRepository.get(newMeal.getId(), USER_ID));
    }
}