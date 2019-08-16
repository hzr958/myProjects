package com.smate.sie.core.base.utils.date;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

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
   * 比较两个 Date 对象表示的时间值（从历元至现在的毫秒偏移量）。
   * 
   * @param d1
   * @param d2
   * @return 如果 d1 表示的时间等于 d2 表示的时间，则返回 0 值；如果此 d1 的时间在d2表示的时间之前，则返回小于 0 的值；如果 d1 的时间在 d2
   *         表示的时间之后，则返回大于 0 的值。
   * 
   */
  public static int compare(Date d1, Date d2) {
    Assert.notNull(d1);
    Assert.notNull(d2);

    Calendar c1 = new GregorianCalendar();
    Calendar c2 = new GregorianCalendar();
    c1.setTime(d1);
    c2.setTime(d2);

    return c1.compareTo(c2);
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
          return "0" + (i + 1);
        }
        return i + 1 + "";
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

  // 针对某个功能
  public static String format(Date date, String string) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int HH = calendar.get(Calendar.HOUR_OF_DAY);
    int mm = calendar.get(Calendar.MINUTE);
    int ss = calendar.get(Calendar.SECOND);
    StringBuilder sb = new StringBuilder();
    if (HH < 10) {
      sb.append("0").append(HH);
    } else {
      sb.append(HH);
    }

    if (mm < 10) {
      sb.append("0").append(mm);
    } else {
      sb.append(mm);
    }

    if (ss < 10) {
      sb.append("0").append(ss);
    } else {
      sb.append(ss);
    }

    return sb.toString();
  }

  /**
   * 解析时间
   * 
   * @param dateString
   * @return
   */
  public static Date parseStringToDate(String dateString) {

    Date date = null;
    if (StringUtils.isNoneBlank(dateString)) {
      dateString = dateString.replaceAll("/", "-");
      String pattem = "";
      switch (dateString.length()) {
        case 4:
          pattem = "yyyy";
          break;
        case 6:
        case 7:
          pattem = "yyyy-MM";
          break;
        case 8:
        case 9:
        case 10:
          pattem = "yyyy-MM-dd";
          break;
        case 16:
          pattem = "yyyy-MM-dd HH:mm";
          break;
        default:
          pattem = "yyyy-MM-dd HH:mm:ss";
          break;
      }
      DateFormat df = new SimpleDateFormat(pattem);

      try {
        df.setLenient(false);
        date = df.parse(dateString);
        // Calendar calendar = Calendar.getInstance();
        // calendar.setTime(date);
        // if (calendar.get(Calendar.YEAR) > 9999) { // 超过最大年份
        // calendar.set(Calendar.YEAR, 9999);
        // return calendar.getTime();
        // }
      } catch (ParseException e) {

      }
    }
    return date;
  }

  /**
   * 将短时间格式时间转换为字符串 yyyy-MM-dd
   * 
   * @param date
   * @return
   */
  public static String dateToStr(Date date) {
    if (date == null) {
      return "";
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = formatter.format(date);
    return dateString;
  }

  /**
   * 将短时间格式时间转换为字符串 yyyy-MM-ddyyyy-MM-dd HH:mm:ss
   * 
   * @param date
   * @return
   */
  public static String dateToStr2(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
  }

  /**
   * 将字符串转换为短时间格式时间
   * 
   * @param strDate
   * @return
   */
  public static Date strToDate(String strDate) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
      Date date = formatter.parse(strDate);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 切分日期为年月日 - 只能用于中文
   * 
   * @param date
   * @return
   */
  public static String[] splitToYearMothDay(Date date) {
    String strDate = dateToStr(date);
    String[] splitDate = strDate.split("-");
    return splitDate;
  }

  /**
   * 切分日期为年月日 - 只能用于中文
   * 
   * @param date
   * @return
   */
  public static String[] splitToYearMothDayByStr(String date) {
    if (date == null || "".equals(date)) {
      return new String[] {"", "", ""};
    }
    String strDate = dateToStr(parseStringToDate(date));
    String[] splitDate = getDateYearMonth(PubXmlConstants.CHS_DATE_PATTERN, strDate);
    return splitDate;
  }

  /**
   * 切分日期为年月日 - 适用于机构版
   * 
   * @param date
   * @return
   */
  public static String[] splitToYearMothDayByStrUseToSie(String date) {
    if (date == null || "".equals(date)) {
      return new String[] {"", "", ""};
    }
    String[] splitDate = getDateYearMonth(PubXmlConstants.CHS_DATE_PATTERN, date);
    return splitDate;
  }

  public static String[] getDateYearMonth(String pattern, String date) {
    if (date == null || "".equals(date)) {
      return new String[] {"", "", ""};
    }
    date = date.replaceAll("\\s+", "");
    String year = "", month = "", day = "";
    if (PubXmlConstants.CHS_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      String[] temp = date.split("[-/.]");
      year = "0".equals(temp[0]) ? "" : temp[0];
      if (temp.length >= 2) {
        month = "0".equals(temp[1]) ? "" : temp[1];
      }
      if (temp.length >= 3) {
        day = "0".equals(temp[2]) ? "" : temp[2];
      }
    } else if (PubXmlConstants.ENG_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      String[] temp = date.split("[-/.]");
      if (temp.length == 1) {
        year = "0".equals(temp[0]) ? "" : temp[0];
      } else if (temp.length <= 2) {
        year = "0".equals(temp[1]) ? "" : temp[1];
        month = "0".equals(temp[0]) ? "" : temp[0];
      } else if (temp.length >= 3) {
        day = "0".equals(temp[0]) ? "" : temp[0];
        month = "0".equals(temp[1]) ? "" : temp[1];
        year = "0".equals(temp[2]) ? "" : temp[2];
      }
    }
    year = XmlUtil.changeSBCChar(year);
    month = XmlUtil.changeSBCChar(month);
    day = XmlUtil.changeSBCChar(day);

    try {
      int t = Integer.parseInt(year);
      if (t < 1970 || t > 2039) {
        year = "";
        month = "";
        day = "";
      } else {
        try {
          int m = Integer.parseInt(month);
          if (m < 1 || m > 12) {
            month = "";
            day = "";
          } else {
            try {
              int d = Integer.parseInt(day);
              if (d < 1 || d > 31) {
                day = "";
              }
            } catch (Exception e) {
              day = "";
            }
          }
        } catch (Exception e) {
          month = "";
          day = "";
        }
      }
    } catch (Exception e) {
      year = "";
      month = "";
      day = "";
    }
    if (month.startsWith("0")) {
      month = month.substring(1);
    }
    if (day.startsWith("0")) {
      day = day.substring(1);
    }

    return new String[] {year, month, day};
  }

  /**
   * 将传入的日期格式，组装成以-拼接
   * 
   * @param date
   * @return
   */
  public static String parseDate(String year, String month, String day, String pattern) {
    if (StringUtils.isEmpty(month)) {
      return year;
    }
    if (StringUtils.isEmpty(day)) {
      return year + pattern + month;
    }
    return year + pattern + month + pattern + day;
  }

  public static void main(String[] args) {
    // Date date = new Date();
    /*
     * System.out.println("test = " + dateToStr(new Date())); String[] test = splitToYearMothDay(new
     * Date()); for (int i = 0; i < test.length; i++) { System.out.println(test[i]); }
     */
    String strDate = "2019/4/1";
    String strDate2 = "2019/4/1 9:10:12";
    System.out.println("Date1 = " + parseStringToDate(strDate));
    System.out.println("Date2 = " + parseStringToDate(strDate2));
    System.out.println("Date compare result = " + parseStringToDate(strDate).compareTo(parseStringToDate(strDate2)));
  }

  /**
   * 获取前preDays天的Date对象
   * 
   * @param date
   * @param preDays
   * @return
   */
  public static Date preDays(Date date, int preDays) {
    GregorianCalendar c1 = new GregorianCalendar();
    c1.setTime(date);
    GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
    cloneCalendar.add(Calendar.DATE, -preDays);
    return cloneCalendar.getTime();
  }

  /**
   * 获取后nextDays天的Date对象
   * 
   * @param date
   * @param nextDays
   * @return
   */
  public static Date nextDays(Date date, int nextDays) {
    GregorianCalendar c1 = new GregorianCalendar();
    c1.setTime(date);
    GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
    cloneCalendar.add(Calendar.DATE, nextDays);
    return cloneCalendar.getTime();
  }

  public static Date nextMonths(Date date, int nextMonth) {
    GregorianCalendar c1 = new GregorianCalendar();
    c1.setTime(date);
    GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
    cloneCalendar.add(Calendar.MONTH, nextMonth);
    return cloneCalendar.getTime();
  }

  public static Date preMonths(Date date, int preMonth) {
    GregorianCalendar c1 = new GregorianCalendar();
    c1.setTime(date);
    GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
    cloneCalendar.add(Calendar.MONTH, -preMonth);
    return cloneCalendar.getTime();
  }
}
