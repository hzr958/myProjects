package com.smate.core.base.utils.string;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 数字工具类.
 * 
 * @author liqinghua
 * 
 */
public class IrisNumberUtils {

  /**
   * 传入字符串，创建Integer类型数据，如果为空或非数字类型返回null.
   * <p/>
   * 这个方法不能实现"02"转2,如果要实现该情况可以使用numberUtils.toInt()
   * 
   * @param str
   * @return
   */
  public static Integer createInteger(String str) {

    if (StringUtils.isBlank(str) || !str.matches("^-?([1-9]+[0-9]*)*0?$")) {
      return null;
    }
    try {
      return Integer.valueOf(str);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 传入字符串，创建int类型数据，如果为空或非数字类型返回null.
   * <p/>
   * 这个方法不能实现"02"转2,如果要实现该情况可以使用numberUtils.toInt()
   * 
   * @param str
   * @return
   */
  public static Integer toInteger(String str) {
    if (StringUtils.isBlank(str) || !str.trim().matches("^-?([0-9]+[0-9]*)*0?$")) {
      return null;
    }
    try {
      str = str.trim();
      int intValue = NumberUtils.toInt(str);
      if (intValue == 0) {
        return null;
      }
      return intValue;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 传入字符串，创建int类型数据，如果为空或非数字类型返回null.
   * <p/>
   * 这个方法不能实现"02"转2,如果要实现该情况可以使用numberUtils.toInt()
   * 
   * defaultValue 必须为null或数据字符
   * 
   * @param str
   * @return
   */
  public static Integer toInteger(String str, Object defaultValue) {

    if (StringUtils.isBlank(str) || !str.trim().matches("^-?([0-9]+[0-9]*)*0?$")) {
      return null;
    }
    try {
      str = str.trim();
      int intValue = NumberUtils.toInt(str);
      if (intValue == 0) {
        return (Integer) defaultValue;
      }
      return intValue;
    } catch (Exception e) {
      return (Integer) defaultValue;
    }
  }

  /**
   * 字符转Double，如果为Null或“”则返回null否则转换为DOUBLE型.
   * 
   * @param str
   * @return
   */
  public static Double createDouble(String str) {

    if (str == null || StringUtils.isBlank(str)) {
      return null;
    }
    if (!str.matches("^-?\\d+(\\.\\d+)?$")) {
      return null;
    }
    return NumberUtils.toDouble(str);
  }

  /**
   * 字符转Float，如果为Null或“”则返回null否则转换为Float型.
   * 
   * @param str
   * @return
   */
  public static Float createFloat(String str) {

    if (str == null || StringUtils.isBlank(str)) {
      return null;
    }
    if (!str.matches("^-?\\d+(\\.\\d+)?$")) {
      return null;
    }
    return NumberUtils.toFloat(str);
  }

  /**
   * 字符转BigDecimal，如果为Null或“”则返回null否则转换为BigDecimal型.
   * 
   * @param str
   * @return
   */
  public static BigDecimal createBigDecimal(String str) {

    if (str == null || StringUtils.isBlank(str)) {
      return null;
    }
    if (!str.matches("^-?\\d+(\\.\\d+)?$")) {
      return null;
    }
    return NumberUtils.createBigDecimal(str);
  }

  /**
   * 传入字符串，创建Long类型数据，如果为空或非数字类型返回null.
   * <p/>
   * 这个方法不能实现"02"转2,如果要实现该情况可以使用numberUtils.toLong()
   * 
   * @param str
   * @return
   */
  public static Long createLong(String str) {

    if (StringUtils.isBlank(str) || !str.matches("^(-?[1-9]+[0-9]*)*0?$")) {
      return null;
    }
    return Long.valueOf(str);
  }

  /**
   * 月日转整型.
   * <p/>
   * 如果arg不是数字或为0则返回null
   * 
   * <pre>
   *   monthDayToInteger(null) = null
   *   monthDayToInteger("")   = null
   *   monthDayToInteger("01")=1
   *   monthDayToInteger("1")  = 1
   * </pre>
   * 
   * @param arg
   * @return
   */
  public static Integer monthDayToInteger(String arg) {
    return NumberUtils.toInt(arg) <= 0 ? null : NumberUtils.toInt(arg);
  }

}
