package com.smate.core.base.utils.dynamicds;

import java.util.Date;

/**
 * 机构主页动态工具类
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class InspgDynamicUtil {



  /**
   * 转换时间格式.
   * 
   * @param dynDate
   * @return
   */
  public static String calculateDate(Date dynDate) {
    Date nowDate = new Date();
    Long margin = nowDate.getTime() - dynDate.getTime();
    String dateStr = "";
    if (margin.longValue() <= 0) {
      dateStr = "1s";
    } else if (margin.longValue() < (60 * 1000)) {// 一分钟前
      int sdate = Math.round(margin.floatValue() / 1000);
      dateStr = (sdate == 0 ? 1 : sdate) + "s";
    } else if (margin.longValue() <= (60 * 60 * 1000)) {// 一小时前
      dateStr = Math.round(margin.floatValue() / (60 * 1000)) + "m";
    } else if (margin.longValue() <= (24 * 60 * 60 * 1000)) {// 一天前
      dateStr = Math.round(margin.floatValue() / (60 * 60 * 1000)) + "h";
    } else if (margin.longValue() <= ((long) 30 * 24 * 60 * 60 * 1000)) {// 30天以前
      dateStr = Math.round(margin.floatValue() / (24 * 60 * 60 * 1000)) + "d";
    } else {
      dateStr = "dateTime";
    }
    return dateStr;
  }

  /**
   * 格式化时间(中文单位).
   * 
   * @author lhd
   * @param dynDate
   * @return
   */
  public static String formatDate(Date dynDate) {
    Date nowDate = new Date();
    Long margin = nowDate.getTime() - dynDate.getTime();
    String dateStr = "";
    if (margin.longValue() <= 0) {
      dateStr = "1秒前";
    } else if (margin.longValue() < (60 * 1000)) {// 一分钟前
      int sdate = Math.round(margin.floatValue() / 1000);
      dateStr = (sdate == 0 ? 1 : sdate) + "秒前";
    } else if (margin.longValue() <= (60 * 60 * 1000)) {// 一小时前
      dateStr = Math.round(margin.floatValue() / (60 * 1000)) + "分钟前";
    } else if (margin.longValue() <= (24 * 60 * 60 * 1000)) {// 一天前
      dateStr = Math.round(margin.floatValue() / (60 * 60 * 1000)) + "小时前";
    } else {// 超过一天的都显示多少天前
      dateStr = Math.round(margin.floatValue() / (24 * 60 * 60 * 1000)) + "天前";
    }
    return dateStr;
  }

  /**
   * 格式化时间(英文单位).
   * 
   * @author lhd
   * @param dynDate
   * @return
   */
  public static String formatDateUS(Date dynDate) {
    Date nowDate = new Date();
    Long margin = nowDate.getTime() - dynDate.getTime();
    String dateStr = "";
    if (margin.longValue() <= 0) {
      dateStr = "1s ago";
    } else if (margin.longValue() < (60 * 1000)) {// 一分钟前
      int sdate = Math.round(margin.floatValue() / 1000);
      dateStr = (sdate == 0 ? 1 : sdate) + "s ago";
    } else if (margin.longValue() <= (60 * 60 * 1000)) {// 一小时前
      dateStr = Math.round(margin.floatValue() / (60 * 1000)) + "m ago";
    } else if (margin.longValue() <= (24 * 60 * 60 * 1000)) {// 一天前
      dateStr = Math.round(margin.floatValue() / (60 * 60 * 1000)) + "h ago";
    } else {// 超过一天的都显示多少天前
      dateStr = Math.round(margin.floatValue() / (24 * 60 * 60 * 1000)) + "d ago";
    }
    return dateStr;
  }
}
