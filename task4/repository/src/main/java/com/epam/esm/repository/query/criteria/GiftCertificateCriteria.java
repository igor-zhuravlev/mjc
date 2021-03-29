package com.epam.esm.repository.query.criteria;

import com.epam.esm.domain.Sort;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GiftCertificateCriteria {
    private String name;
    private String description;
    private String[] tagNames;
    private Sort sort;
}
