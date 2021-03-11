package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonRootName(value = "order_gift_certificate")
public class OrderGiftCertificateDto implements Serializable {
    private static final long serialVersionUID = 4268216176920063442L;

    @NotNull
    private GiftCertificateDto giftCertificate;
    @Positive
    private Integer quantity;
}
