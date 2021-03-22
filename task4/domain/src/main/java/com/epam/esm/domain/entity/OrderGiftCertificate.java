package com.epam.esm.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders_gift_certificates")
public class OrderGiftCertificate implements Serializable {
    private static final long serialVersionUID = 5670960658105200947L;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id", nullable = false)
    private GiftCertificate giftCertificate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
