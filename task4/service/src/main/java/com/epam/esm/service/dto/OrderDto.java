package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonRootName(value = "order")
public class OrderDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = 1141594450857312063L;

    @Null
    private Instant createDate;
    @Null
    private BigDecimal amount;

    @JsonIgnore
    private UserDto user;

    @NotNull
    private Set<@NotNull OrderGiftCertificateDto> orderGiftCertificates;
}
