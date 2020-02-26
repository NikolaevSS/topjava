package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 2;
    public static final Meal MEAL = new Meal(
            MEAL_ID,
            LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак",
            500
    );
    public static final int ALL_USER_MEALS_COUNT = 7;
    public static final LocalDate FILTER_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDateTime FILTER_DATE_TIME_START = LocalDateTime.of(
            2020, Month.JANUARY, 30, 0, 0, 0
    );
    public static final LocalDateTime FILTER_DATE_TIME_END = LocalDateTime.of(
            2020, Month.JANUARY, 31, 0, 0, 0
    );
    public static final int USER_MEALS_COUNT_BY_DATE = 3;

    public static Meal getNew() {
        return new Meal(LocalDateTime.now(), "Meal", 1234);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(4321);
        return updated;
    }
}
