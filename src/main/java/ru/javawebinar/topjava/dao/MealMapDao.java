package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealMapDao implements Dao<Meal> {
    private static final ConcurrentHashMap<Long, Meal> map = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong();

    public MealMapDao() {
        MealsUtil.getMocks().forEach(this::save);
    }

    @Override
    public Meal save(Meal entity) {
        if (entity.isNew()) {
            entity.setId(sequence.incrementAndGet());
        }
        return map.put(entity.getId(), entity);
    }

    @Override
    public Meal get(Long id) {
        return map.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void remove(Long id) {
        map.remove(id);
    }
}
