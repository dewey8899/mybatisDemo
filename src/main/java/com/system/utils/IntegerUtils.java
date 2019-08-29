/**
 * Project Name:ezplatform
 * File Name:IntegerUtils.java
 * Package Name:com.enmore.utils
 * Date:2016年10月27日下午6:46:49
 * Copyright (c) 2016, 上海易贸供应链管理有限公司版权所有.
 *
*/

package com.system.utils;

import java.util.HashSet;
import java.util.Set;

public class IntegerUtils {

  public static Integer getInteger(String value) {

    return value == null ? Integer.valueOf(0) : new Integer(value);
  }

  public static Integer getInteger(Integer value) {

    return value == null ? Integer.valueOf(0) : value;
  }

  private static Integer add(Integer x, Integer y) {

    return getInteger(x) + (getInteger(y));
  }

  private static Integer sub(Integer x, Integer y) {

    return getInteger(x) - (getInteger(y));
  }

  private static Integer multiply(Integer x, Integer y) {

    return getInteger(x) * (getInteger(y));
  }

  public static Integer divide(Integer x, Integer y) {

    return getInteger(x) / (getInteger(y));
  }

  public static Integer sub(Integer...values){
    Integer result = getInteger(values[0]);
    for(int i = 1; i < values.length; i ++){
      Integer value = values[i];
      result = sub(result, value);
    }
    return result;
  }

  public static Integer add(Integer...values){
    Integer result = getInteger(values[0]);
    for(int i = 1; i < values.length; i ++){
      Integer value = values[i];
      result = add(result, value);
    }
    return result;
  }

  public static Integer multiply(Integer...values){
    Integer result = getInteger(values[0]);
    for(int i = 1; i < values.length; i ++){
      Integer value = values[i];
      result = multiply(result, value);
    }
    return result;
  }

  public static boolean isEqualToNumber(Integer value1, Integer...values){
    if(value1 == null){
      return false;
    }
    Set<Integer> integerSet = new HashSet<>();
    for(int i = 0; i < values.length; i ++){
      integerSet.add(values[i]);
    }
    int size = integerSet.size();
    if(size == 0 || size > 1){
      return false;
    }
    Integer value2 = integerSet.iterator().next();
    return value1.intValue() == value2.intValue();
  }
}

