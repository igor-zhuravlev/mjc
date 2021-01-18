package edu.gradle.lib;

public class StringUtils {

    public static final char[] NUMBERS = new char[] {
            '\u0030', '\u0031', '\u0032', '\u0033', '\u0034',
            '\u0035', '\u0036', '\u0037', '\u0038', '\u0039'
    };

    public static final String MINUS = "-";

    public boolean isPositiveNumber(String str) {
        String subStr = org.apache.commons.lang3.StringUtils.substring(str, 1);
        if (org.apache.commons.lang3.StringUtils.isNumeric(subStr)) {
            return !org.apache.commons.lang3.StringUtils.startsWith(str, MINUS);
        }
        return false;
    }
}
