package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclusive;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {
    public static final String NOT_FOUND_FORMAT = "id=%d, userId=%d";

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    private static Predicate<Meal> isMatchedUserId(Integer userId) {
        return meal -> Objects.equals(meal.getUserId(), userId);
    }

    private static Predicate<Meal> isBetweenDates(LocalDate dateStart, LocalDate dateEnd) {
        return meal -> isBetweenInclusive(meal.getDate(), dateStart, dateEnd);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        checkNotFound(repository.save(meal, userId), String.format(NOT_FOUND_FORMAT, meal.getId(), meal.getUserId()));
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), String.format(NOT_FOUND_FORMAT, id, userId));
    }

    public Meal get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), String.format(NOT_FOUND_FORMAT, id, userId));
    }

    public List<Meal> getAll(int userId) {
        return repository.getFiltered(isMatchedUserId(userId));
    }

    public List<Meal> getAll(int userId, LocalDate dateStart, LocalDate dateEnd) {
        dateStart = Optional.ofNullable(dateStart).orElse(LocalDate.MIN);
        dateEnd = Optional.ofNullable(dateEnd).orElse(LocalDate.MAX);
        return repository.getFiltered(
                isMatchedUserId(userId)
                        .and(isBetweenDates(dateStart, dateEnd))
        );
    }
}