package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonRootName("page")
public class PageDto implements Serializable {
    private static final long serialVersionUID = -3319772390593018460L;

    private int size;
    private int page;

    @JsonIgnore
    private int offset;
    @JsonIgnore
    private int limit;

    public PageDto(int size, int page) {
        this.size = size;
        this.page = page;
        limit = size;
        offset = (page - 1) * limit;
    }
}
