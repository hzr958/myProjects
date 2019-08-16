package com.smate.web.v8pub.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.date.DateStringSplitFormateUtil;

public class DataFormatUtils {


  /**
   * 将传入的日期格式，组装成以-拼接
   * 
   * @param date
   * @return
   */
  public static String parseDate(String date, String pattern) {
    String year = "", month = "", day = "";
    Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(date);
    if (publishDateMap.get("year") != null) {
      year = publishDateMap.get("year");
    }
    if (publishDateMap.get("month") != null) {
      month = publishDateMap.get("month");
    }
    if (publishDateMap.get("day") != null) {
      day = publishDateMap.get("day");
    }
    if (StringUtils.isBlank(month)) {
      return year;
    }
    if (StringUtils.isBlank(day)) {
      return year + pattern + month;
    }
    return year + pattern + month + pattern + day;
  }

}
