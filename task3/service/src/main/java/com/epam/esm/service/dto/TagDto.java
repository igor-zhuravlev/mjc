package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonRootName(value = "tag")
public class TagDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    @NotBlank
    private String name;
}
