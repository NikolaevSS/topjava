package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final AtomicInteger caloriesPerDay;
    private final int caloriesLimitPerDay;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories,
                              AtomicInteger caloriesPerDay, int caloriesLimitPerDay) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.caloriesPerDay = caloriesPerDay;
        this.caloriesLimitPerDay = caloriesLimitPerDay;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories,
                              Integer caloriesPerDay, int caloriesLimitPerDay) {
        this(dateTime, description, calories, new AtomicInteger(caloriesPerDay), caloriesLimitPerDay);
    }

    public boolean isExcess() {
        return nonNull(caloriesPerDay) && caloriesPerDay.intValue() > caloriesLimitPerDay;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + isExcess() +
                '}';
    }
}
