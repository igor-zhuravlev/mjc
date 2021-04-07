package com.epam.esm.service.dto;

import com.epam.esm.service.validation.annotation.NotBlankIfPresent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonRootName(value = "gift_certificate")
public class GiftCertificateUpdateDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = 1144649892569462913L;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String TIME_ZONE = "UTC";

    @NotBlankIfPresent
    private String name;
    @NotBlankIfPresent
    private String description;
    private BigDecimal price;
    private Integer duration;
    @Null
    @JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
    private Instant createDate;
    @Null
    @JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
    private Instant lastUpdateDate;

    private Set<TagDto> tags;
}
