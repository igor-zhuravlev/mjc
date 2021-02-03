package com.epam.esm.service.dto;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractDto implements Serializable {
    private static final long serialVersionUID = -3116869888525443852L;

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto that = (AbstractDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AbstractDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
