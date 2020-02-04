package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;

import static java.util.stream.Collectors.*;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenInclusive;

public class UserMealsUtil {
    private static final Comparator<UserMeal> USER_MEAL_COMPARATOR = Comparator.comparing(UserMeal::getDateTime);

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
        System.out.println("filteredByStream2() result: " + filteredByStream2(meals, startTime, endTime, caloriesLimitPerDay));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesLimitPerDay) {
        CaloriesPerDay caloriesPerDay = new CaloriesPerDay();
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        meals.sort(USER_MEAL_COMPARATOR);
        meals.forEach(meal -> {
            caloriesPerDay.addCalories(meal.getLocalDate(), meal.getCalories());
            if (isBetweenInclusive(meal.getLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                caloriesPerDay.get(meal.getLocalDate()),
                                caloriesLimitPerDay
                        )
                );
            }
        });
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesLimitPerDay) {
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
                        caloriesLimitPerDay
                ))
                .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesLimitPerDay) {
        CaloriesPerDay caloriesPerDay = new CaloriesPerDay();
        return meals.stream()
                .sorted(USER_MEAL_COMPARATOR)
                .peek(meal -> caloriesPerDay.addCalories(meal.getLocalDate(), meal.getCalories()))
                .filter(meal -> isBetweenInclusive(meal.getLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesPerDay.get(meal.getLocalDate()),
                        caloriesLimitPerDay
                ))
                .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByStream2(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesLimitPerDay) {
        CaloriesPerDay caloriesPerDay = new CaloriesPerDay();
        return meals.stream()
                .sorted(USER_MEAL_COMPARATOR)
                .collect(Collector.of(
                        ArrayList<UserMealWithExcess>::new,
                        (list, meal) -> {
                            caloriesPerDay.addCalories(meal.getLocalDate(), meal.getCalories());
                            if (isBetweenInclusive(meal.getLocalTime(), startTime, endTime)) {
                                list.add(new UserMealWithExcess(
                                        meal.getDateTime(),
                                        meal.getDescription(),
                                        meal.getCalories(),
                                        caloriesPerDay.get(meal.getLocalDate()),
                                        caloriesLimitPerDay
                                ));
                            }
                        },
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        },
                        (list) -> list
                ));
    }
}

class CaloriesPerDay {
    private LocalDate date;
    private AtomicInteger calories;

    public void addCalories(LocalDate date, int calories) {
        if (!Objects.equals(this.date, date)) {
            this.date = date;
            this.calories = new AtomicInteger();
        }
        this.calories.addAndGet(calories);
    }

    public AtomicInteger get(LocalDate date) {
        if (this.date.equals(date)) {
            return calories;
        } else {
            return new AtomicInteger();
        }
    }
}