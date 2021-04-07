package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonRootName(value = "error")
public class ErrorDto implements Serializable {
    private static final long serialVersionUID = -7453008975136453861L;

    @NonNull
    private String message;
    @NonNull
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> constraints;
}
