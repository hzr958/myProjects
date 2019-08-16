package com.smate.web.prj.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

import com.smate.core.base.utils.date.DateStringSplitFormateUtil;

/**
 * 数据验证工具类
 * 
 * @author YJ
 *
 *         2019年8月14日
 */
public class DataValidUtils {

  /**
   * 校验日期
   * 
   * @param date
   * @return
   */
  public static boolean validDate(String date) {
    if (StringUtils.isBlank(date))
      return false;

    Map<String, String> startMap = DateStringSplitFormateUtil.split(date);
    date = startMap.get("fomate_date");
    String reg = "[1-9]\\d{3}(-|/){1}(0[1-9]|1[0-2]|[1-9])(-|/){1}(0[1-9]|[1-2][0-9]|3[0-1]|[1-9])";
    Pattern p = Pattern.compile(reg);
    if (p.matcher(date).find()) {
      return true;
    }
    return false;
  }

  /**
   * 校验是否是年份
   * 
   * @param year
   * @return
   */
  public static boolean validYear(String year) {
    if (StringUtils.isBlank(year))
      return false;
    // excel传入的是是2017.0
    year = StringUtils.substring(year, 0, year.indexOf("."));
    String reg = "^[1-9]\\d{3}$";
    Pattern p = Pattern.compile(reg);
    if (p.matcher(year).find()) {
      return true;
    }
    return false;
  }

  /**
   * 校验经费金额，只允许整数位最长8位，小数位最长2位（浮点数）
   * 
   * @param amount
   * @return
   */
  public static boolean validAmountF(String amount) {
    if (StringUtils.isBlank(amount))
      return true;
    String reg = "^[0-9]{1,8}.?[0-9]{0,2}$";
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(amount);
    if (m.find()) {
      return true;
    }
    return false;
  }

  /**
   * 校验经费金额，只允许整数位9位（正整数）
   * 
   * @param amount
   * @return
   */
  public static boolean validAmountI(String amount) {
    if (StringUtils.isBlank(amount))
      return true;
    amount = StringUtils.substring(amount, 0, amount.indexOf("."));
    String reg = "^[0-9]{1,9}$";
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(amount);
    if (m.find()) {
      return true;
    }
    return false;
  }

}
