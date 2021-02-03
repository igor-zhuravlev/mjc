package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonRootName(value = "tag")
public class TagDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + getId() +
                "name='" + getName() + '\'' +
                '}';
    }
}
