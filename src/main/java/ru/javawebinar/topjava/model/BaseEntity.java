package ru.javawebinar.topjava.model;

public abstract class BaseEntity extends BaseModel {
    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        super(id);
    }
}
