package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@JsonRootName(value = "error")
public class ErrorDto implements Serializable {
    private static final long serialVersionUID = -7453008975136453861L;

    private String message;
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> constraints;

    public ErrorDto(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getConstraints() {
        return constraints;
    }

    public void setConstraints(Map<String, String> constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(message, errorDto.message) &&
                Objects.equals(code, errorDto.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, code);
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", constraints=" + constraints +
                '}';
    }
}
