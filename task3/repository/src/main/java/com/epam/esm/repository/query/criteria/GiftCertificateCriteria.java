package com.epam.esm.repository.query.criteria;

import com.epam.esm.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

public class GiftCertificateCriteria {
    private String name;
    private String description;
    private String[] tagNames;
    private Sort sort;

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

    public String[] getTagNames() {
        return tagNames;
    }

    public void setTagNames(String[] tagNames) {
        this.tagNames = tagNames;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateCriteria criteria = (GiftCertificateCriteria) o;
        return Objects.equals(name, criteria.name) &&
                Objects.equals(description, criteria.description) &&
                Arrays.equals(tagNames, criteria.tagNames) &&
                Objects.equals(sort, criteria.sort);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, sort);
        result = 31 * result + Arrays.hashCode(tagNames);
        return result;
    }

    @Override
    public String toString() {
        return "GiftCertificateQueryParameter{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tagNames=" + Arrays.toString(tagNames) +
                ", sort=" + sort +
                '}';
    }
}
