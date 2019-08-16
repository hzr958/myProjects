package com.smate.core.base.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串日期格式化处理. 转成我们的标准格式. 转不了的原样返回.
 * 
 * @author tsz
 *
 */
public class DateStringSplitFormateUtil {

  private final static String SMATE_DATE_PATTERN_1 = "yyyy-MM-dd"; // 科研之友日期格式
  private final static String SMATE_DATE_PATTERN_2 = "yyyy-MM"; // 科研之友日期格式
  private final static String SMATE_DATE_PATTERN_3 = "yyyy";

  private final static List<String> DATA_PATTERN_EN = new ArrayList<String>();
  private final static List<String> DATA_PATTERN_CN = new ArrayList<String>();
  // 英文日期 (月份可全拼) 同格式类型 短的必须放长的后面
  static {
    DATA_PATTERN_EN.add("d MMM, yyyy");// 5 Mar, 2018
    DATA_PATTERN_EN.add("d MMM yyyy");// 5 Mar 2018
    DATA_PATTERN_EN.add("MMM d, yyyy"); // Mar 5, 2018
    DATA_PATTERN_EN.add("MMM d yyyy");// Mar 5 2018
    DATA_PATTERN_EN.add("MMM, yyyy");// Mar, 2018
    DATA_PATTERN_EN.add("MMM yyyy");// Mar 2018
    DATA_PATTERN_EN.add("yyyy, MMM d");// 2018, Mar 5
    DATA_PATTERN_EN.add("yyyy MMM d");// 2018 Mar 5
    DATA_PATTERN_EN.add("yyyy, MMM");// 2018, Mar
    DATA_PATTERN_EN.add("yyyy MMM");// 2018 Mar
    DATA_PATTERN_EN.add("yyyy"); // 必须放最后 不能要。
  }
  // 中文日期格式 同格式类型 短的必须放长的后面
  static {
    DATA_PATTERN_CN.add("yyyy-MM-dd");
    DATA_PATTERN_CN.add("yyyy-MM");
    DATA_PATTERN_CN.add("MM-yyyy");
    DATA_PATTERN_CN.add("dd-MM-yyyy");
    DATA_PATTERN_CN.add("yyyy.MM.dd");
    DATA_PATTERN_CN.add("yyyy.MM");
    DATA_PATTERN_CN.add("MM.yyyy");
    DATA_PATTERN_CN.add("dd.MM.yyyy");
    DATA_PATTERN_CN.add("yyyy/MM/dd");
    DATA_PATTERN_CN.add("yyyy/MM");
    DATA_PATTERN_CN.add("MM/yyyy");
    DATA_PATTERN_CN.add("dd/MM/yyyy");
    DATA_PATTERN_CN.add("dd MM yyyy");
    DATA_PATTERN_CN.add("MM yyyy");
    DATA_PATTERN_CN.add("yyyy MM dd");
    DATA_PATTERN_CN.add("yyyy年MM月dd日");
    DATA_PATTERN_CN.add("yyyy MM");
    DATA_PATTERN_CN.add("yyyy"); // 必须放最后
  }

  /**
   * 拆分.
   * 
   * @param date
   * @return
   */
  public static Map<String, String> split(String dateString) {
    Map<String, String> result = new HashMap<String, String>();
    if (StringUtils.isBlank(dateString)) {
      return result;
    }
    Calendar c = Calendar.getInstance();
    List<String> DATA_PATTERN = new ArrayList<String>();
    boolean isEnglish = isEnglish(dateString);
    if (isEnglish) {
      DATA_PATTERN.addAll(DATA_PATTERN_EN);
    } else {
      DATA_PATTERN.addAll(DATA_PATTERN_CN);
    }
    for (String pattern : DATA_PATTERN) {
      try {
        SimpleDateFormat sdf = null;
        if (isEnglish) {
          sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        } else {
          sdf = new SimpleDateFormat(pattern);
        }
        Date date = sdf.parse(dateString);
        c.setTime(date);
      } catch (ParseException e) {
        // 日期转化失败
        continue;
      }
      int year = c.get(Calendar.YEAR);
      if (year < 1000 || year > 9999) {
        // 年份不满足条件直接下一个 有可能是错误的格式
        continue;
      }
      buildSplitDate(result, c, isEnglish, pattern);
      break;
    }
    // 重新拼接格式日期 yyyy-MM-dd
    reFormateDate(dateString, result);
    return result;
  }

  /**
   * 重新格式化日期. yyyy-MM-dd 日期拆分有问题，按原格式返回。
   * 
   * @param dateString
   * @param result
   */
  private static void reFormateDate(String dateString, Map<String, String> result) {
    StringBuilder smateFormateDate = new StringBuilder();
    if (result.get("year") != null) {
      smateFormateDate.append(result.get("year"));
      if (result.get("month") != null) {
        smateFormateDate.append("-");
        smateFormateDate.append(result.get("month"));
        if (result.get("day") != null) {
          smateFormateDate.append("-");
          smateFormateDate.append(result.get("day"));
        }
        result.put("fomate_date", smateFormateDate.toString());
      } else if (dateString.length() == 4) {
        result.put("fomate_date", smateFormateDate.toString());
      } else {
        // 匹配到年份，拆不出月份 返回原数据
        result.put("fomate_date", dateString);
      }
    } else {
      // 没有匹配到年份， 用正则表达式获取年份，但不重新构造格式化日期 返回原数据
      int year = onluBuildYear(dateString);
      result.put("year", year + "");
      result.put("fomate_date", dateString);
    }

  }

  /**
   * 构造拆分后的数据. year,month,day
   * 
   * @param result
   * @param c
   * @param isEnglish
   * @param pattern
   */
  private static void buildSplitDate(Map<String, String> result, Calendar c, boolean isEnglish, String pattern) {
    result.put("year", c.get(Calendar.YEAR) + "");
    if (pattern.length() > 5) {
      result.put("month", (c.get(Calendar.MONTH) + 1) + "");
    }
    if (isEnglish) {
      if (isEnglish && pattern.length() > 9) {
        result.put("day", c.get(Calendar.DAY_OF_MONTH) + "");
      }
    } else {
      if (pattern.length() > 7) {
        result.put("day", c.get(Calendar.DAY_OF_MONTH) + "");
      }
    }
  }

  /**
   * 正则表达式匹配年份.(只匹配年份)
   * 
   * @param publishDate
   * @return
   */
  private static Integer onluBuildYear(String dateString) {
    // TODO 表达式有问题?
    if (StringUtils.isBlank(dateString) || !dateString.matches("\\s*\\d{4}.*")) {
      return null;
    }
    // TODO 有 null 问题
    return Integer.valueOf(StringUtils.substring(StringUtils.trimToEmpty(dateString), 0, 4));
  }

  /**
   * 判断是否有字母
   * 
   * @param str
   * @return
   */
  public static boolean isEnglish(String str) {
    String regex = ".*[a-zA-Z].*";
    return str.matches(regex);
  }
  // 冬 2018

  public static void main(String[] args) {
    Map<String, String> s = split("2019.03.09");
    s.forEach((k, v) -> {
      System.out.println(k + "=" + v);
    });
  }
}
