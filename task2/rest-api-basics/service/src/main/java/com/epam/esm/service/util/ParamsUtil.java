package com.epam.esm.service.util;

import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public final class ParamsUtil {
    public static final String TAG_PARAM = "tag";
    public static final String PART_PARAM = "part";
    public static final String SORT_PARAM = "sort";

    private static final String SORT_ITEM_PARAM_DELIMITER = ",";

    public static Sort buildSortByParams(String[] sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sort : sorts) {
            String[] sortItem = sort.strip().split(SORT_ITEM_PARAM_DELIMITER);
            if (sortItem[1].strip().equalsIgnoreCase(Sort.Direction.ASC.name())) {
                orders.add(Sort.Order.asc(sortItem[0]));
            } else {
                orders.add(Sort.Order.desc(sortItem[0]));
            }
        }
        return Sort.by(orders);
    }

    public static Criteria buildCriteria(Map<String, String[]> params) {
        Criteria criteria = new Criteria();
        Map<CriteriaSearch, String> criteriaParams = new HashMap<>();
        if (params.get(TAG_PARAM) != null) {
            criteriaParams.put(CriteriaSearch.TAG, params.get(TAG_PARAM)[0]);
        }
        if (params.get(PART_PARAM) != null) {
            criteriaParams.put(CriteriaSearch.PART, params.get(PART_PARAM)[0]);
        }
        if (params.get(SORT_PARAM) != null) {
            Sort sort = buildSortByParams(params.get(SORT_PARAM));
            criteria.setSort(sort);
        }
        criteria.setParams(criteriaParams);
        return criteria;
    }

    public static Criteria buildCriteria(CriteriaSearch criteriaSearch, String param) {
        Criteria criteria = new Criteria();
        Map<CriteriaSearch, String> criteriaParams = new HashMap<>();
        criteriaParams.put(criteriaSearch, param);
        criteria.setParams(criteriaParams);
        return criteria;
    }
}
