package com.smate.core.base.utils.common;

import com.smate.core.base.utils.data.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 货币格式化.
 * 
 * @author yamingd
 */
public class MoneyFormatterUtils {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MoneyFormatterUtils.class);

  /**
   * 格式.
   */
  private static NumberFormat formatter = new DecimalFormat("###,###.00");

  /**
   * 格式.
   */
  private static NumberFormat formatterValue = new DecimalFormat("###.##");

  /**
   * 格式.
   */
  private static NumberFormat formatterNum = new DecimalFormat("###,###");


  /**
   * 保留2位小数.
   */
  private static int scale = 2;

  /**
   * @param value 输入
   * @return String
   */
  public static String format(Object value) {
    return format(value, "");
  }

  /**
   * @param value 输入
   * @return String
   */
  public static String formatNum(Object value) {
    return formatNum(value, "");
  }

  /**
   * @param value 输入
   * @return String补0的小数
   */
  public static String formatValue(Object value) {
    return formatValue(value, "");
  }

  /**
   * @param value 输入
   * @param nullValue 当输入为空时的返回值
   * @return String
   */
  public static String format(Object value, String nullValue) {

    if (value == null) {
      return nullValue;
    }

    try {
      String str = String.valueOf(value).trim();
      if (XmlUtil.isEmpty(str)) {
        return nullValue;
      }
      str = str.replace(",", "");
      BigDecimal bigDecimal = new BigDecimal(str);

      if (bigDecimal.floatValue() == 0.0000) {
        return "0";
      }
      BigDecimal w = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
      str = formatter.format(w);

      // 根据会计学的要求，补0
      int indexOf = str.indexOf(".");
      if (indexOf > -1) {
        String endStr = str.substring(indexOf + 1);
        if (endStr.length() == 1) {
          str = str + "0";
        }
      }

      return str;

    } catch (RuntimeException e) {
      LOGGER.error("格式化Money错误.", e);
      return nullValue;
    }
  }

  /**
   * @param value 输入
   * @param nullValue 当输入为空时的返回值
   * @return String
   */
  public static String formatNum(Object value, String nullValue) {

    if (value == null) {
      return nullValue;
    }

    try {
      String str = String.valueOf(value).trim();
      if (XmlUtil.isEmpty(str)) {
        return nullValue;
      }
      str = str.replace(",", "");
      BigDecimal bigDecimal = new BigDecimal(str);

      if (bigDecimal.floatValue() == 0.0000) {
        return "0";
      }
      BigDecimal w = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
      str = formatterNum.format(w);

      // 根据会计学的要求，补0
      int indexOf = str.indexOf(".");
      if (indexOf > -1) {
        String endStr = str.substring(indexOf + 1);
        if (endStr.length() == 1) {
          str = str + "0";
        }
      }

      return str;

    } catch (RuntimeException e) {
      LOGGER.error("格式化Money错误.", e);
      return nullValue;
    }
  }

  /**
   * @param value 输入
   * @param nullValue 当输入为空时的返回值
   * @return String
   */
  public static String formatValue(Object value, String nullValue) {

    if (value == null) {
      return nullValue;
    }

    try {
      String str = String.valueOf(value).trim();
      if (XmlUtil.isEmpty(str)) {
        return nullValue;
      }

      BigDecimal bigDecimal = new BigDecimal(str);
      if (bigDecimal.floatValue() == 0.0000) {
        return "0.00";
      }

      BigDecimal w = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
      str = formatterValue.format(w);
      // 根据会计学的要求，补0
      int indexOf = str.indexOf(".");
      if (indexOf > -1) {
        String endStr = str.substring(indexOf + 1);
        if (endStr.length() == 1) {
          str = str + "0";
        }
      } else {
        str = str + ".00";
      }
      return str;

    } catch (RuntimeException e) {
      LOGGER.error("格式化Money错误.", e);
      return nullValue;
    }
  }

  public static String formatterMoney(Long money){
    if(money == null) return "";
    return formatterNum.format(money);
  }

  public static void main(String[] args) {

    System.out.println(formatterMoney(0L));
  }
}
