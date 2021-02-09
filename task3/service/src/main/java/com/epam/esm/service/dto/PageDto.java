package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Objects;

@JsonRootName("page")
public class PageDto implements Serializable {
    private static final long serialVersionUID = -3319772390593018460L;

    private int size;
    private int page;

    @JsonIgnore
    private int offset;
    @JsonIgnore
    private int limit;

    public PageDto() {
    }

    public PageDto(int size, int page) {
        this.size = size;
        this.page = page;
        limit = size;
        offset = (page - 1) * limit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto pageDto = (PageDto) o;
        return size == pageDto.size &&
                page == pageDto.page;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, page);
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "size=" + size +
                ", page=" + page +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
