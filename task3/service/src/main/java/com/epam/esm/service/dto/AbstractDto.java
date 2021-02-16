package com.epam.esm.service.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractDto extends RepresentationModel<AbstractDto> implements Serializable {
    private static final long serialVersionUID = -3116869888525443852L;

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto that = (AbstractDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
