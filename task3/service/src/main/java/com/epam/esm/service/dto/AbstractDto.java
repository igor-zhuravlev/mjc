package com.epam.esm.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class AbstractDto extends RepresentationModel<AbstractDto> implements Serializable {
    private static final long serialVersionUID = -3116869888525443852L;

    protected Long id;
}
