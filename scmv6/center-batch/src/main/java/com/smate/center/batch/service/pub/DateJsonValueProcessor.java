package com.smate.center.batch.service.pub;

import net.sf.json.JsonConfig;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {

  public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
  private DateFormat dateFormat;

  /**
   * 构造方法.
   * 
   * @param datePattern 日期格式
   */
  public DateJsonValueProcessor(String datePattern) {
    try {
      dateFormat = new SimpleDateFormat(datePattern);
    } catch (Exception ex) {
      dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
    }
  }

  public Object processArrayValue(Object value, JsonConfig jsonConfig) {
    return process(value);
  }

  public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
    return process(value);
  }

  private Object process(Object value) {
    if (value != null) {
      return dateFormat.format((Date) value);
    }
    return null;
  }


}
