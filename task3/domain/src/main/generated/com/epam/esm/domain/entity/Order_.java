package com.epam.esm.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ extends com.epam.esm.domain.entity.AbstractEntity_ {

	public static volatile SingularAttribute<Order, BigDecimal> amount;
	public static volatile SetAttribute<Order, GiftCertificate> giftCertificates;
	public static volatile SingularAttribute<Order, User> user;
	public static volatile SingularAttribute<Order, Instant> createDate;

	public static final String AMOUNT = "amount";
	public static final String GIFT_CERTIFICATES = "giftCertificates";
	public static final String USER = "user";
	public static final String CREATE_DATE = "createDate";

}

