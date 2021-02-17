package com.epam.esm.service.util;

import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GiftCertificateCriteriaBuilder {

    private static final String PARAM_DELIMITER = ",";

    public static Sort buildSortByParams(String[] sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sort : sorts) {
            String[] sortItem = sort.strip().split(PARAM_DELIMITER);
            String direction = sortItem[1].strip();
            if (direction.equalsIgnoreCase(Sort.Direction.DESC.name())) {
                orders.add(Sort.Order.desc(sortItem[0]));
            } else if (direction.equalsIgnoreCase(Sort.Direction.ASC.name())) {
                orders.add(Sort.Order.asc(sortItem[0]));
            }
        }
        return Sort.by(orders);
    }

    public static GiftCertificateCriteria build(GiftCertificateParamDto giftCertificateParam) {
        GiftCertificateCriteria criteria = new GiftCertificateCriteria();
        String name = giftCertificateParam.getName();
        if (name != null) {
            criteria.setName(name.strip());
        }
        String description = giftCertificateParam.getDescription();
        if (description != null) {
            criteria.setName(description.strip());
        }
        String[] tags = giftCertificateParam.getTags();
        if (tags != null) {
            String[] formattedTags = Arrays.stream(tags)
                    .map(String::strip)
                    .toArray(String[]::new);
            criteria.setTagNames(formattedTags);
        }
        String[] sort = giftCertificateParam.getSort();
        if (sort != null) {
            Sort formattedSort = buildSortByParams(giftCertificateParam.getSort());
            criteria.setSort(formattedSort);
        }
        return criteria;
    }
}
