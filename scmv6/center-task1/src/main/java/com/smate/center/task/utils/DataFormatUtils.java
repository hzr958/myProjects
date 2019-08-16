package com.smate.center.task.utils;

import org.apache.commons.lang3.StringUtils;
import com.smate.core.base.pub.util.PubDetailVoUtil;

public class DataFormatUtils {


  /**
   * 将传入的日期格式，组装成以-拼接
   * 
   * @param date
   * @return
   */
  public static String parseDate(String date, String pattern) {
    String year = PubDetailVoUtil.parseDateForYear(date);
    String month = PubDetailVoUtil.parseDateForMonth(date);
    if (month.startsWith("0")) {
      month = month.substring(1, month.length());
    }
    String day = PubDetailVoUtil.parseDateForDay(date);
    if (day.startsWith("0")) {
      day = day.substring(1, day.length());
    }
    if (StringUtils.isEmpty(month)) {
      return year;
    }
    if (StringUtils.isEmpty(day)) {
      return year + pattern + month;
    }
    return year + pattern + month + pattern + day;
  }

}
