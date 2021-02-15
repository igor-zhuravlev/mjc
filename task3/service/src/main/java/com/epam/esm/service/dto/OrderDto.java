package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@JsonRootName(value = "order")
public class OrderDto extends AbstractDto implements Serializable {
    private static final long serialVersionUID = 1141594450857312063L;

    private Instant createDate;
    private BigDecimal amount;

    @JsonIgnore
    private UserDto user;

    @JsonIgnore
    private Set<GiftCertificateDto> giftCertificates;

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Set<GiftCertificateDto> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificateDto> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", amount=" + amount +
                ", user=" + user +
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
