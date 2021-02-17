package com.epam.esm.service.util;

import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GiftCertificateCriteriaBuilder {
    public static final String NAME_PARAM = "name";
    public static final String DESCRIPTION_PARAM = "description";
    public static final String TAGS_PARAM = "tags";
    public static final String SORT_PARAM = "sort";

    private static final String PARAM_DELIMITER = ",";

    public static Sort buildSortByParams(String[] sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sort : sorts) {
            String[] sortItem = sort.strip().split(PARAM_DELIMITER);
            if (sortItem[1].strip().equalsIgnoreCase(Sort.Direction.DESC.name())) {
                orders.add(Sort.Order.desc(sortItem[0]));
            } else {
                orders.add(Sort.Order.asc(sortItem[0]));
            }
        }
        return Sort.by(orders);
    }

    public static GiftCertificateCriteria build(Map<String, String[]> params) {
        GiftCertificateCriteria criteria = new GiftCertificateCriteria();
        if (params.get(NAME_PARAM) != null) {
            criteria.setName(params.get(NAME_PARAM)[0]);
        }
        if (params.get(DESCRIPTION_PARAM) != null) {
            criteria.setName(params.get(DESCRIPTION_PARAM)[0]);
        }
        if (params.get(TAGS_PARAM) != null) {
            String[] tags = params.get(TAGS_PARAM)[0].split(PARAM_DELIMITER);
            criteria.setTagNames(tags);
        }
        if (params.get(SORT_PARAM) != null) {
            Sort sort = buildSortByParams(params.get(SORT_PARAM));
            criteria.setSort(sort);
        }
        return criteria;
    }
}
