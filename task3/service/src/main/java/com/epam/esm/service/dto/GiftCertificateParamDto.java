package com.epam.esm.service.dto;

import com.epam.esm.service.validation.annotation.NotBlankIfPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateParamDto implements Serializable {
    private static final long serialVersionUID = -1565771738449160282L;

    @NotBlankIfPresent
    private String name;
    @NotBlankIfPresent
    private String description;
    private List<@NotBlankIfPresent String> tags;
    private List<@NotBlankIfPresent String> sort;
}
