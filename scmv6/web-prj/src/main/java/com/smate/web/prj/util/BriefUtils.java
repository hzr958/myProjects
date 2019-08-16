package com.smate.web.prj.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.StringUtils;

/**
 * @author yamingd 成果Brief生成工具类.
 */
public class BriefUtils {

  /**
   * 返回标准格式的年-月-日.
   * 
   * @param year 年
   * @param month 月
   * @param day 日
   * @return String
   */
  public static String getStandardDateString(String year, String month, String day) {
    // award_year||'-'||nvl(award_month,'0')||'-'||nvl(award_day,'0')
    if (StringUtils.isBlank(year)) {
      return "";
    }
    if (StringUtils.isBlank(month)) {
      month = "0";
    }
    if (StringUtils.isBlank(day)) {
      day = "0";
    }
    return String.format("%s-%s-%s", year, month, day);
  }

  /**
   * 返回标准格式的年/月/日.
   * 
   * scm-6684 加入了一个Locale参数，根据locale返回对应格式的日期
   * 
   * @param year 年
   * @param month 月
   * @param day 日
   * @return String
   */
  public static String getDateString(Locale locale, String year, String month, String day) {
    // award_year||'-'||nvl(award_month,'0')||'-'||nvl(award_day,'0')
    if (StringUtils.isBlank(year)) {
      return "";
    }
    if (StringUtils.isBlank(month)) {
      return String.format("%s", year);
    }
    if (locale.getLanguage().equalsIgnoreCase("zh_CN") || locale.getLanguage().equalsIgnoreCase("zh")) {
      if (StringUtils.isBlank(day)) {
        return String.format("%s/%s", year, month);
      }
      return String.format("%s/%s/%s", year, month, day);
    }
    if (StringUtils.isBlank(day)) {
      return String.format("%s/%s", month, year);
    }
    return String.format("%s/%s/%s", day, month, year);
  }

  /**
   * 带单位的金额格式化.
   * 
   * @param money
   * @param unit
   * @return
   */
  public static String moneyWithUnit(String money, String unit) {

    String tmp = XmlUtil.filterNull(money);
    tmp = MoneyFormatterUtils.formatNum(tmp);
    if (StringUtils.isNotBlank(tmp)) {
      tmp = StringUtils.trimToEmpty(unit) + " " + tmp;
    }
    return tmp;
  }

  /**
   * 把Map的key除去路径分隔符.
   * 
   * @param data Map<String, String>
   * @return Map<String, String>
   */
  public static Map<String, String> normalizeData(Map<String, String> data) {
    Map<String, String> results = new HashMap<String, String>();
    Iterator<String> itor = data.keySet().iterator();
    while (itor.hasNext()) {
      String key = itor.next();
      String value = data.get(key);
      String[] temp = key.split("/@");
      key = temp[temp.length - 1];
      results.put(key.toUpperCase(), value);
    }
    return results;
  }
}
