package ru.javawebinar.topjava.model;

import static java.util.Objects.isNull;

public abstract class BaseModel {
    protected Long id;

    public BaseModel() {
    }

    public BaseModel(Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return isNull(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
