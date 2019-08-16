package com.smate.center.batch.util.pub;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间常用处理.
 * 
 * @author chenxiangrong
 * 
 */
public class DateUtils {

  public static List<String> MONTH_LIST =
      Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

  /**
   * 时间比较.
   * 
   * @param deadLine
   * @return
   */
  public static boolean isOverTime(Date deadLine) {
    try {
      Date date = new Date();
      String dt = new String(new SimpleDateFormat("yyyy-MM-dd").format(date));
      DateFormat f = new java.text.SimpleDateFormat("yy-MM-dd");
      Date nowDate = f.parse(dt);
      return deadLine.before(nowDate);// 截止日期小于当前时间true,否则false;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static long getDateTime(Date date) {
    try {
      String dt = new String(new SimpleDateFormat("yyyy-MM-dd").format(date));
      DateFormat f = new java.text.SimpleDateFormat("yy-MM-dd");
      Date nowDate = f.parse(dt);
      return nowDate.getTime();
    } catch (Exception ex) {
      ex.printStackTrace();
      return 0l;
    }
  }

  /**
   * 一年后的日期.
   * 
   * @param dateLine
   * @return
   */
  public static String afterOneYear() {
    Format formatter = new SimpleDateFormat("yyyy/MM/dd");
    Date todayDate = new Date();
    long afterTime = (todayDate.getTime() / 1000) + 60 * 60 * 24 * 365;
    todayDate.setTime(afterTime * 1000);
    String afterOneYear = formatter.format(todayDate);
    return afterOneYear;
  }

  /**
   * 获取N月的今天。
   * 
   * @return
   */
  public static Date afterOneMonth(int nextMonth) {
    try {
      Date date = new Date();
      int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
      int month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) + nextMonth;
      int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
      if (month == 0) {
        year -= 1;
        month = 12;
      } else if (day > 28) {
        if (month == 2) {
          if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            day = 29;
          } else
            day = 28;
        } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
          day = 30;
        }
      }
      String y = year + "";
      String m = "";
      String d = "";
      if (month < 10)
        m = "0" + month;
      else
        m = month + "";
      if (day < 10)
        d = "0" + day;
      else
        d = day + "";
      return new SimpleDateFormat("yyyy-MM-dd").parse(y + "-" + m + "-" + d);
    } catch (Exception e) {
      return new Date();
    }
  }

  public static void main(String[] args) {
    System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(afterOneMonth(3)));
  }

  /**
   * 将月份数字转为英文
   * 
   * @param releaseVersion
   * @return
   */
  public static String changeNumberToChar(String releaseVersion) {

    if (null == releaseVersion) {
      throw new IllegalArgumentException("ReleaseVersion cannot be null");
    }
    String strNumber = releaseVersion.substring(0, 2);
    String strMonth = null;
    try {
      strMonth = MONTH_LIST.get(Integer.parseInt(strNumber));
    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println("Parse month error........");
    }
    return strMonth + releaseVersion.substring(2);
  }

  /**
   * 将月份英文转换为数字
   * 
   * @param releaseVersion
   * @return
   */
  public static String changeStringToNumber(String releaseVersion) {
    if (null == releaseVersion) {
      throw new IllegalArgumentException("ReleaseVersion cannot be null");
    }
    for (int i = 0; i < MONTH_LIST.size(); i++) {
      if (releaseVersion.toUpperCase().startsWith(MONTH_LIST.get(i))) {
        if (i < 10) {
          return "0" + i;
        }
        return i + "";
      }
    }
    return -1 + "";
  }

  public static int getSixMonth(Long toYear, Long toMonth) throws ParseException {
    if (toYear == null) {
      return 1;
    } else {
      if (toMonth == null) {
        toMonth = 1l;
      }
      Calendar cal = Calendar.getInstance();
      int month = cal.get(Calendar.MONTH) + 1;
      int year = cal.get(Calendar.YEAR);
      if (year == toYear.intValue()) {
        if ((month - toMonth.intValue()) <= 6) {
          return 1;
        } else {
          return 0;
        }
      } else {
        int marginMonth = ((year - 1) * 12 + month) - ((toYear.intValue() - 1) * 12 + toMonth.intValue());
        if (marginMonth <= 6) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }
}
