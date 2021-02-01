package com.epam.esm.entity;

import java.io.Serializable;

public class Tag extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    public Tag() {}

    public Tag(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
