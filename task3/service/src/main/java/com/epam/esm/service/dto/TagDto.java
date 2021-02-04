package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Objects;

@JsonRootName(value = "tag")
public class TagDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    private String name;

    public TagDto() {}

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
        if (!super.equals(o)) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                "name='" + name + '\'' +
                '}';
    }
}
