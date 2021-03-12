package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonRootName(value = "gift_certificate")
public class GiftCertificateDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = 1144649892569462913L;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String TIME_ZONE = "UTC";

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull @Positive
    private BigDecimal price;
    @NotNull @Positive
    private Integer duration;
    @Null
    @JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
    private Instant createDate;
    @Null
    @JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
    private Instant lastUpdateDate;

    private Set<TagDto> tags;
}
