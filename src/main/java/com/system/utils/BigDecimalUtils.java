/**
 * Project Name:ezplatform
 * File Name:BigDecimalUtils.java
 * Package Name:com.enmore.utils
 * Date:2016年8月24日上午11:29:57
 * Copyright (c) 2016, 上海易贸供应链管理有限公司版权所有.
 */

package com.system.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {


    public static BigDecimal getBigDecimal(String value) {

        return value == null ? BigDecimal.valueOf(0) : new BigDecimal(value);
    }

    public static BigDecimal getBigDecimal(BigDecimal value) {

        return value == null ? BigDecimal.valueOf(0) : value;
    }

    public static BigDecimal add(BigDecimal x, BigDecimal y) {

        return getBigDecimal(x).add(getBigDecimal(y));
    }

    public static BigDecimal multiply(BigDecimal x, BigDecimal y) {

        return getBigDecimal(x).multiply(getBigDecimal(y));
    }

    public static BigDecimal divide(BigDecimal x, BigDecimal y) {

        if (y == null || y.compareTo(BigDecimal.valueOf(0)) == 0) {

            throw new RuntimeException("divided by zero");
        }

        return getBigDecimal(x).divide(getBigDecimal(y));
    }

    public static BigDecimal divide(BigDecimal x, BigDecimal y, int scale, RoundingMode roundingMode) {

        if (y == null || y.compareTo(BigDecimal.valueOf(0)) == 0) {

            throw new RuntimeException("divided by zero");
        }

        return getBigDecimal(x).divide(getBigDecimal(y), scale, roundingMode);
    }

    public static BigDecimal divide(BigDecimal x, BigDecimal y, int scale, int roundingMode) {

        if (y == null || y.compareTo(BigDecimal.valueOf(0)) == 0) {

            throw new RuntimeException("divided by zero");
        }

        return getBigDecimal(x).divide(getBigDecimal(y), scale, roundingMode);
    }

    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {

        return getBigDecimal(x).subtract(getBigDecimal(y));
    }

    public static boolean equals(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return false;
        } else {
            return x.compareTo(y) == 0;
        }
    }

    public static int compareTo(BigDecimal x, BigDecimal y){
        return getBigDecimal(x).compareTo(getBigDecimal(y));
    }


    public static BigDecimal add(BigDecimal...values){
        BigDecimal result = getBigDecimal(values[0]);
        for(int i = 1; i < values.length; i ++){
            BigDecimal value = values[i];
            result = add(result, value);
        }
        return result;
    }

    public static BigDecimal subtract(BigDecimal...values){
        BigDecimal result = getBigDecimal(values[0]);
        for(int i = 1; i < values.length; i ++){
            BigDecimal value = values[i];
            result = subtract(result, value);
        }
        return result;
    }
}
