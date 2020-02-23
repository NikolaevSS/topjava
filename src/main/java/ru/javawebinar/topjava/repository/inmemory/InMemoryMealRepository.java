package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclusive;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private Map<Integer, Map<Integer, Meal>> userMealsMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = getMeals(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return nonNull(
                getMeals(userId).remove(id)
        );
    }

    @Override
    public Meal get(int id, int userId) {
        return getMeals(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getMeals(userId).values().stream()
                .sorted(MEAL_COMPARATOR)
                .collect(toList());
    }

    @Override
    public List<Meal> getAllBetweenDates(int userId, LocalDate dateStart, LocalDate dateEnd) {
        return getMeals(userId).values().stream()
                .filter(isBetweenDates(dateStart, dateEnd))
                .sorted(MEAL_COMPARATOR)
                .collect(toList());
    }

    private Map<Integer, Meal> getMeals(int userId) {
        return userMealsMap.computeIfAbsent(userId, ConcurrentHashMap::new);
    }

    private Predicate<Meal> isBetweenDates(LocalDate dateStart, LocalDate dateEnd) {
        return meal -> isBetweenInclusive(meal.getDate(), dateStart, dateEnd);
    }
}

