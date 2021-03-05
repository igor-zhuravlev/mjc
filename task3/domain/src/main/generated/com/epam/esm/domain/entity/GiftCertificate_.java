package com.epam.esm.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GiftCertificate.class)
public abstract class GiftCertificate_ extends com.epam.esm.domain.entity.AbstractEntity_ {

	public static volatile SingularAttribute<GiftCertificate, Integer> duration;
	public static volatile SingularAttribute<GiftCertificate, BigDecimal> price;
	public static volatile SingularAttribute<GiftCertificate, Instant> lastUpdateDate;
	public static volatile SingularAttribute<GiftCertificate, String> name;
	public static volatile SingularAttribute<GiftCertificate, String> description;
	public static volatile SingularAttribute<GiftCertificate, Instant> createDate;
	public static volatile SetAttribute<GiftCertificate, Tag> tags;

	public static final String DURATION = "duration";
	public static final String PRICE = "price";
	public static final String LAST_UPDATE_DATE = "lastUpdateDate";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CREATE_DATE = "createDate";
	public static final String TAGS = "tags";

}

