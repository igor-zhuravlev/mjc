package com.epam.esm.service.dto;

import com.epam.esm.service.exception.validation.annotation.NotBlankIfPresent;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class GiftCertificateParamDto implements Serializable {
    private static final long serialVersionUID = -1565771738449160282L;

    @NotBlankIfPresent
    private String name;
    @NotBlankIfPresent
    private String description;
    private List<@NotBlankIfPresent String> tags;
    private List<@NotBlankIfPresent String> sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateParamDto that = (GiftCertificateParamDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, tags, sort);
    }

    @Override
    public String toString() {
        return "GiftCertificateParamDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", sort=" + sort +
                '}';
    }
}
