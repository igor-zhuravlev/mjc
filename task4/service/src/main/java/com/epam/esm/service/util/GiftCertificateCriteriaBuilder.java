package com.epam.esm.service.util;

import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.service.dto.GiftCertificateParamDto;

import java.util.List;

public final class GiftCertificateCriteriaBuilder {

    public static GiftCertificateCriteria build(GiftCertificateParamDto giftCertificateParam) {
        GiftCertificateCriteria criteria = new GiftCertificateCriteria();
        String name = giftCertificateParam.getName();
        if (name != null) {
            criteria.setName(name.strip());
        }
        String description = giftCertificateParam.getDescription();
        if (description != null) {
            criteria.setDescription(description.strip());
        }
        List<String> tags = giftCertificateParam.getTags();
        if (tags != null) {
            String[] formattedTags = tags.stream()
                    .map(String::strip)
                    .toArray(String[]::new);
            criteria.setTagNames(formattedTags);
        }
        return criteria;
    }
}
