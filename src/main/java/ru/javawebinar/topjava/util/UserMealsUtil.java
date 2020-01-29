package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.*;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenInclusive;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        int caloriesLimitPerDay = 2000;
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, startTime, endTime, caloriesLimitPerDay);
        mealsTo.forEach(System.out::println);
        System.out.println("filteredByStreams() result: " + filteredByStreams(meals, startTime, endTime, caloriesLimitPerDay));
        System.out.println("filteredByStream() result: " + filteredByStream(meals, startTime, endTime, caloriesLimitPerDay));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        // Counting calories per date
        Map<LocalDate, Integer> mapCaloriesPerDate = new HashMap<>();
        meals.forEach(meal -> {
            LocalDate currentDate = meal.getLocalDate();
            mapCaloriesPerDate.merge(currentDate, meal.getCalories(), (sum, calories) -> sum += calories);
        });
        // Filter the data and fill in the UserMealWithExcess list
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        meals.forEach(meal -> {
            if (isBetweenInclusive(meal.getLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                mapCaloriesPerDate.getOrDefault(meal.getLocalDate(), 0),
                                caloriesPerDay
                        )
                );
            }
        });
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCaloriesPerDate = meals.stream().collect(
                groupingBy(UserMeal::getLocalDate, summingInt(UserMeal::getCalories))
        );
        return meals.stream()
                .filter(meal -> isBetweenInclusive(meal.getLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        mapCaloriesPerDate.get(meal.getLocalDate()),
                        caloriesPerDay
                ))
                .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesLimitPerDay) {
        Map<LocalDate, AtomicInteger> map = new HashMap<>();
        return meals.stream()
                .peek(meal -> {
                    if (!map.containsKey(meal.getLocalDate())) {
                        map.put(meal.getLocalDate(), new AtomicInteger(0));
                    }
                    map.get(meal.getLocalDate()).addAndGet(meal.getCalories());
                })
                .filter(meal -> isBetweenInclusive(meal.getLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        map.get(meal.getLocalDate()),
                        caloriesLimitPerDay
                ))
                .collect(toList());
    }
}