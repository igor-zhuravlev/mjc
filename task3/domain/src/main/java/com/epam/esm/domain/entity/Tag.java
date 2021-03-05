package com.epam.esm.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "tags", schema = "gift_certificates_system")
public class Tag extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
