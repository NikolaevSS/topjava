package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.BaseEntity;

import java.util.List;

public interface Dao<E extends BaseEntity> {
    E save(E entity);

    E get(Long id);

    List<E> getAll();

    void remove(Long id);
}
