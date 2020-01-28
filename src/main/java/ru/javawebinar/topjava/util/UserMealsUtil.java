package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
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

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("filteredByStreams() result: " + filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println("filteredByStream() result: " + filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        // Counting calories per date
        Map<LocalDate, Integer> mapCaloriesPerDate = new HashMap<>();
        meals.forEach(meal -> {
            LocalDate currentDate = meal.getDateTime().toLocalDate();
            mapCaloriesPerDate.merge(currentDate, meal.getCalories(), (sum, calories) -> sum += calories);
        });
        // Filter the data and fill in the UserMealWithExcess list
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        meals.forEach(meal -> {
            if (isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                mapCaloriesPerDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay
                        )
                );
            }
        });
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCaloriesPerDate = meals.stream().collect(
                groupingBy(meal -> meal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories))
        );
        return meals.stream()
                .filter(meal -> isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        mapCaloriesPerDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay
                ))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        UserMealsWhitDailyCalories userMealsDailyCalories = new UserMealsWhitDailyCalories(caloriesPerDay);
        return meals.stream()
                .peek(userMealsDailyCalories::addUserMeal)
                .collect(Collectors.toList())
                .stream()
                .filter(meal -> isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        userMealsDailyCalories.isExceededDailyCalories(meal)
                ))
                .collect(Collectors.toList());
    }
}

class UserMealsWhitDailyCalories {
    private List<UserMeal> userMeals;
    private Map<LocalDate, Integer> mapCaloriesPerDate;
    private int caloriesPerDay;

    public UserMealsWhitDailyCalories(int caloriesPerDay) {
        userMeals = new ArrayList<>();
        mapCaloriesPerDate = new HashMap<>();
        this.caloriesPerDay = caloriesPerDay;
    }

    public void addUserMeal(UserMeal meal) {
        userMeals.add(meal);
        mapCaloriesPerDate.merge(
                meal.getDateTime().toLocalDate(), meal.getCalories(), (sum, calories) -> sum += calories
        );
    }

    public boolean isExceededDailyCalories(UserMeal meal) {
        return mapCaloriesPerDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
    }
}
