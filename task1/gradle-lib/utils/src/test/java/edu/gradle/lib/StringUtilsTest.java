package edu.gradle.lib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    private StringUtils stringUtils;

    @BeforeEach
    void setUp() {
        stringUtils = new StringUtils();
    }

    @Test
    void isPositiveNumber() {
        assertTrue(stringUtils.isPositiveNumber("123"));
        assertTrue(stringUtils.isPositiveNumber(" 123"));
    }

    @Test
    void isNegativeNumber() {
        assertFalse(stringUtils.isPositiveNumber("-123"));
    }

    @Test
    void isNotPosNegNumber() {
        assertFalse(stringUtils.isPositiveNumber("0"));
    }

    @Test
    void isNotNumber() {
        assertFalse(stringUtils.isPositiveNumber("asd"));
        assertFalse(stringUtils.isPositiveNumber(""));
        assertFalse(stringUtils.isPositiveNumber(" "));
        assertFalse(stringUtils.isPositiveNumber("null"));

        assertFalse(stringUtils.isPositiveNumber("123.123"));
        assertFalse(stringUtils.isPositiveNumber("-123.123"));
    }
}