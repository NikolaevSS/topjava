package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {
    public static final String NOT_FOUND_FORMAT = "id=%d, userId=%d";

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public Meal update(Meal meal) {
        return checkNotFound(repository.save(meal), String.format(NOT_FOUND_FORMAT, meal.getId(), meal.getUserId()));
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), String.format(NOT_FOUND_FORMAT, id, userId));
    }

    public Meal get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), String.format(NOT_FOUND_FORMAT, id, userId));
    }

    public List<Meal> getAll(int userId) {
        return getAll(userId, null, null);
    }

    public List<Meal> getAll(int userId, LocalDate dateStart, LocalDate dateEnd) {
        return checkNotFound(
                repository.getFiltered(
                        userId,
                        Optional.ofNullable(dateStart).orElse(LocalDate.MIN),
                        Optional.ofNullable(dateEnd).orElse(LocalDate.MAX)
                ),
                String.format("userId=%d, between dates %s and %s", userId, dateStart, dateEnd)
        );
    }
}