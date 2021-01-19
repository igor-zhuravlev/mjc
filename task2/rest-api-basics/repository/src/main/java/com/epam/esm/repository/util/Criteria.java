package com.epam.esm.repository.util;

import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Objects;

public final class Criteria {
    private Map<String, String> params;
    private Sort sort;

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
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
        Criteria criteria = (Criteria) o;
        return Objects.equals(params, criteria.params) &&
                Objects.equals(sort, criteria.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params, sort);
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "params=" + params +
                ", sort=" + sort +
                '}';
    }
}
