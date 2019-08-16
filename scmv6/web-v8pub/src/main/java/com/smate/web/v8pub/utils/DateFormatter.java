package com.smate.web.v8pub.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class DateFormatter {

  public static String dateFormater(String date, String toSplitChar) {
    if (StringUtils.isEmpty(date)) {
      return "";
    }
    return StringUtils.trim(date).replaceAll("[-/.]", toSplitChar);
  }

  /**
   * 校验是否日期格式
   * 
   * @param date
   * @return
   */
  public static Boolean isDate(String date) {
    if (date == null) {
      return false;
    }
    return Pattern.compile("^\\d{4}([-/.]\\d{2}){1,2}$").matcher(StringUtils.trim(date)).find();
  }

  public static void main(String[] str) {
    System.out.println(DateFormatter.dateFormater("2015-1", "-"));
  }
}
