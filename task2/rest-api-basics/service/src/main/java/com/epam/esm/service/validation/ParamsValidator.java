package com.epam.esm.service.validation;

import com.epam.esm.service.util.ParamsUtil;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;

public final class ParamsValidator {

    private static final String NAME_PARAM = "name";
    private static final String DATE_PARAM = "date";

    private static final String SORT_PARAMS_DELIMITER = ",";

    public static boolean isValid(Map<String, String[]> params) {
        boolean ret = params.keySet().stream()
                .allMatch(s -> params.get(s).length != 0
                        && params.get(s)[0] != null
                        && !params.get(s)[0].isEmpty()
                        && !params.get(s)[0].isBlank());

        if (params.get(ParamsUtil.SORT_PARAM) != null) {
            return ret && isSortParamsValid(params.get(ParamsUtil.SORT_PARAM));
        }
        return ret;
    }

    public static boolean isSortParamsValid(String[] sortParams) {
        return Arrays.stream(sortParams)
                .allMatch(s -> {
                    String[] p = s.split(SORT_PARAMS_DELIMITER);
                    return (p[0].equalsIgnoreCase(NAME_PARAM)
                            || p[0].equalsIgnoreCase(DATE_PARAM))
                            && (p[1].equalsIgnoreCase(Sort.Direction.ASC.name())
                            || p[1].equalsIgnoreCase(Sort.Direction.DESC.name()));
                });
    }
}
