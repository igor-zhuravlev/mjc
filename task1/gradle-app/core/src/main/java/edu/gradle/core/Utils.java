package edu.gradle.core;

import edu.gradle.lib.StringUtils;

public class Utils {

    public static boolean isAllPositiveNumbers(String ... str) {
        StringUtils stringUtils = new StringUtils();
        for (String string : str) {
            if (!stringUtils.isPositiveNumber(string)) {
                return false;
            }
        }
        return true;
    }
}
