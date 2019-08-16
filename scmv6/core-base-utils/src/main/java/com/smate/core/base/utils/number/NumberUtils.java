package com.smate.core.base.utils.number;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

/**
 * 扩展commons-lang3 NumberUtils工具类
 *
 * @author houchuanjie
 * @date 2018年3月2日 下午5:22:27
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

  /**
   * <p>
   * 将字符串str转换为Long类型，此方法会对str调用 {@link StringUtils#trimToNull(String)}做处理，空白字符串或空指针将返回null。
   * 如果处理后的字符串不为null，则调用{@link Long#parseLong(String)}处理。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  NumberUtils.parseLong(null)     =   null
   *  NumberUtils.parseLong("   ")    =   null
   *  NumberUtils.parseLong("123")    =   123L
   *  NumberUtils.parseLong(" 123 ")  =   123L
   *  NumberUtils.parseLong("abc")    throws  NumberFormatException
   *  NumberUtils.parseLong("abc123")    throws  NumberFormatException
   * </pre>
   *
   * @param str 要转换的字符串
   * @return 转换后的Long（或者给定的字符串是null或者空白会返回null）
   * @throws NumberFormatException 字符串str不能转换为Long类型，则会抛出此异常
   * @author houchuanjie
   * @date 2018年3月2日 下午5:24:54
   */
  public static Long parseLong(String str) throws NumberFormatException {
    String trimToNullStr = StringUtils.trimToNull(str);
    if (trimToNullStr == null) {
      return null;
    }
    return Long.parseLong(trimToNullStr);
  }

  /**
   * 转化字符串数组为Long数组
   * 
   * @param strArry
   * @return
   * @throws NumberFormatException
   */
  public static Long[] parseLongArry(String[] strArry) throws NumberFormatException {
    Long[] intArray = new Long[strArry.length];
    for (int i = 0; i < strArry.length; i++) {
      intArray[i] = parseLong(strArry[i]);
    }
    return intArray;
  }

  /**
   * <p>
   * 将字符串str转换为Long类型，此方法会对str调用
   * {@link StringUtils#trimToNull(String)}做处理，空白字符串或空指针将返回defaultValue。如果处理后的字符串不为null，
   * 则调用{@link Long#parseLong(String)}处理。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  NumberUtils.parseLong(null, 0L)     =   0L
   *  NumberUtils.parseLong("   ", 0L)    =   0L
   *  NumberUtils.parseLong("123", 0L)    =   123L
   *  NumberUtils.parseLong(" 123 ", 0L)  =   123L
   *  NumberUtils.parseLong("abc", 0L)    =   0L
   *  NumberUtils.parseLong("abc123", 0L) =   0L
   *  NumberUtils.parseLong(null, null)     =   null
   *  NumberUtils.parseLong("   ", null)    =   null
   *  NumberUtils.parseLong("123", null)    =   123L
   *  NumberUtils.parseLong(" 123 ", null)  =   123L
   *  NumberUtils.parseLong("abc", null)    =   null
   *  NumberUtils.parseLong("abc123", null) =   null
   * </pre>
   *
   * @param str 要转换的字符串
   * @return 转换后的Long（或者给定的字符串是null、空白、无法转换则会返回给定的默认值）
   * @author houchuanjie
   * @date 2018年3月2日 下午5:24:54
   */
  public static Long parseLong(String str, Long defaultValue) {
    String trimToNullStr = StringUtils.trimToNull(str);
    if (trimToNullStr == null) {
      return defaultValue;
    }
    try {
      return Long.parseLong(trimToNullStr);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * <p>
   * 将字符串str转换为Integer类型，此方法会对str调用
   * {@link StringUtils#trimToNull(String)}做处理，空白字符串或空指针将返回null。如果处理后的字符串不为null，则调用{@link Integer#parseInt(String)}处理。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  NumberUtils.parseInt(null)     =   null
   *  NumberUtils.parseInt("   ")    =   null
   *  NumberUtils.parseInt("123")    =   123L
   *  NumberUtils.parseInt(" 123 ")  =   123L
   *  NumberUtils.parseInt("abc")    throws  NumberFormatException
   *  NumberUtils.parseInt("abc123")    throws  NumberFormatException
   * </pre>
   *
   * @param str 要转换的字符串
   * @return 转换后的Long（或者给定的字符串是null或者空白会返回null）
   * @throws NumberFormatException 字符串str不能转换为Long类型，则会抛出此异常
   * @author houchuanjie
   * @date 2018年3月2日 下午5:24:54
   */
  public static Integer parseInt(String str) throws NumberFormatException {
    String trimToNullStr = StringUtils.trimToNull(str);
    if (trimToNullStr == null) {
      return null;
    }
    return Integer.parseInt(trimToNullStr);
  }

  /**
   * 转化字符串数组为整型数组
   * 
   * @param strArry
   * @return
   * @throws NumberFormatException
   */
  public static Integer[] parseIntArry(String[] strArry) throws NumberFormatException {
    Integer[] intArray = new Integer[strArry.length];
    for (int i = 0; i < strArry.length; i++) {
      intArray[i] = parseInt(strArry[i]);
    }
    return intArray;
  }

  /**
   * <p>
   * 将字符串str转换为Integer类型，此方法会对str调用
   * {@link StringUtils#trimToNull(String)}做处理，空白字符串或空指针将返回defaultValue。如果处理后的字符串不为null，
   * 则调用{@link Integer#parseInt(String)}处理。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  NumberUtils.parseInt(null, 0L)     =   0
   *  NumberUtils.parseInt("   ", 0L)    =   0
   *  NumberUtils.parseInt("123", 0L)    =   123
   *  NumberUtils.parseInt(" 123 ", 0L)  =   123
   *  NumberUtils.parseInt("abc", 0L)    =   0
   *  NumberUtils.parseInt("abc123", 0L) =   0
   *  NumberUtils.parseInt(null, null)     =   null
   *  NumberUtils.parseInt("   ", null)    =   null
   *  NumberUtils.parseInt("123", null)    =   123
   *  NumberUtils.parseInt(" 123 ", null)  =   123
   *  NumberUtils.parseInt("abc", null)    =   null
   *  NumberUtils.parseInt("abc123", null) =   null
   * </pre>
   *
   * @param str 要转换的字符串
   * @return 转换后的Integer（或者给定的字符串是null、空白、无法转换则会返回给定的默认值）
   * @author houchuanjie
   * @date 2018年3月2日 下午5:24:54
   */
  public static Integer parseInt(String str, Integer defaultValue) {
    String trimToNullStr = StringUtils.trimToNull(str);
    if (trimToNullStr == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(trimToNullStr);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * 判断值是否为0，当且仅当{@code num}不为null且值为0时返回true，否则返回false
   *
   * @param num
   * @return
   * @author houchuanjie
   * @date 2018年3月19日 下午4:15:12
   */
  public static boolean isZero(Long num) {
    return LONG_ZERO.equals(num);
  }

  public static boolean isNotZero(Long num) {
    return !isZero(num);
  }

  public static boolean isNullOrZero(Long num) {
    return Objects.isNull(num) || isZero(num);
  }

  /**
   * 判断值是否为0，当且仅当{@code num}不为null且值为0时返回true，否则返回false
   *
   * @param num
   * @return
   * @author houchuanjie
   * @date 2018年3月19日 下午4:15:12
   */
  public static boolean isZero(Integer num) {
    return INTEGER_ZERO.equals(num);
  }

  public static boolean isNotZero(Integer num) {
    return !isZero(num);
  }

  /**
   * 判断给定数值是否为奇数
   *
   * @param num 数值，不能为{@code null}
   * @return
   */
  public static boolean isOdd(Integer num) {
    assert Objects.nonNull(num) : "给定num不能为null！";
    return num % 2 != 0;
  }

  /**
   * 判断给定数值是否为偶数
   *
   * @param num 数值，不能为{@code null}
   * @return
   */
  public static boolean isEven(Integer num) {
    assert Objects.nonNull(num) : "给定num不能为null！";
    return num % 2 == 0;
  }

  /**
   * Integer对象不为空且值不为0
   * 
   * @param num
   * @return
   */
  public static boolean isNotNullOrZero(Integer num) {
    return !Objects.isNull(num) && (num.intValue() != 0);
  }

  /**
   * Long对象不为空且值不为0
   * 
   * @param num
   * @return
   */
  public static boolean isNotNullOrZero(Long num) {
    return !Objects.isNull(num) && (num.longValue() != 0);
  }

  public static void main(String[] args) {
    Integer isNull = null;
    Integer isZero = 0;
    Integer a = 80000;
    Long nullLong = null;
    Long zeroLong = 0L;
    Long b = 80000L;
    System.out.println("Integer不为空且不为0：" + isNotNullOrZero(isNull));
    System.out.println("Integer不为空且不为0：" + isNotNullOrZero(isZero));
    System.out.println("Integer不为空且不为0：" + isNotNullOrZero(a));
    System.out.println("Long不为空且不为0：" + isNotNullOrZero(nullLong));
    System.out.println("Long不为空且不为0：" + isNotNullOrZero(zeroLong));
    System.out.println("Long不为空且不为0：" + isNotNullOrZero(b));
  }
}
