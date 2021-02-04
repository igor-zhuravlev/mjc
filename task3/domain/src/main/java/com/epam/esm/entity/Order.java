package com.epam.esm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "order", schema = "rest")
public class Order extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -519531229783607916L;

    @Column(name = "create_date", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant createDate;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_gift_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private Set<GiftCertificate> giftCertificates;

    public Order() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(createDate, order.createDate) &&
                Objects.equals(amount, order.amount) &&
                Objects.equals(user, order.user) &&
                Objects.equals(giftCertificates, order.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createDate, amount, user, giftCertificates);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                "createDate=" + createDate +
                ", amount=" + amount +
                ", user=" + user +
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
