package com.epam.esm.util;

import com.epam.esm.repository.util.Criteria;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, String> newParams = new HashMap<>();
        if (params.get(TAG_PARAM) != null) {
            newParams.put(TAG_PARAM, params.get(TAG_PARAM)[0]);
        }
        if (params.get(PART_PARAM) != null) {
            newParams.put(PART_PARAM, params.get(PART_PARAM)[0]);
        }
        if (params.get(SORT_PARAM) != null) {
            Sort sort = buildSortByParams(params.get(SORT_PARAM));
            criteria.setSort(sort);
        }
        criteria.setParams(newParams);
        return criteria;
    }
}
