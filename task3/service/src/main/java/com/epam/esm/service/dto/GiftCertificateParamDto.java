package com.epam.esm.service.dto;

import com.epam.esm.service.exception.validation.annotation.NotBlankIfPresent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class GiftCertificateParamDto implements Serializable {
    private static final long serialVersionUID = -1565771738449160282L;

    @NotBlankIfPresent
    private String name;
    @NotBlankIfPresent
    private String description;
    private String[] tags;
    private String[] sort;

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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateParamDto that = (GiftCertificateParamDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Arrays.equals(tags, that.tags) &&
                Arrays.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description);
        result = 31 * result + Arrays.hashCode(tags);
        result = 31 * result + Arrays.hashCode(sort);
        return result;
    }

    @Override
    public String toString() {
        return "GiftCertificateParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", sort=" + Arrays.toString(sort) +
                '}';
    }
}
