package com.smate.center.batch.oldXml.prj;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yamingd 成果Brief生成工具类.
 */
public class PrjBriefUtilis {

  /**
   * 读取字段数据.
   * 
   * @param fields 字段集合
   * @param xmlDoc XmlDocument
   * @return Map<String, String>
   * @throws InvalidXpathException InvalidXpathException
   */
  public static Map<String, String> getFieldsData(List<String> fields, PrjXmlDocument xmlDoc) throws Exception {
    Map<String, String> data = new HashMap<String, String>();
    for (int index = 0; index < fields.size(); index++) {
      String xpath = fields.get(index);
      String value = xmlDoc.getXmlNodeAttribute(xpath);
      data.put(xpath, value);
    }
    return data;
  }

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
    if (org.apache.commons.lang.StringUtils.isBlank(year)) {
      return "";
    }
    if (org.apache.commons.lang.StringUtils.isBlank(month)) {
      month = "0";
    }
    if (org.apache.commons.lang.StringUtils.isBlank(day)) {
      day = "0";
    }
    return String.format("%s-%s-%s", year, month, day);
  }

  /**
   * 返回标准格式的年/月/日.
   * 
   * @param year 年
   * @param month 月
   * @param day 日
   * @return String
   */
  public static String getDateString(String year, String month, String day) {
    // award_year||'-'||nvl(award_month,'0')||'-'||nvl(award_day,'0')
    if (org.apache.commons.lang.StringUtils.isBlank(year)) {
      return "";
    }
    if (org.apache.commons.lang.StringUtils.isBlank(month)) {
      return String.format("%s", year);
    }
    if (org.apache.commons.lang.StringUtils.isBlank(day)) {
      return String.format("%s/%s", year, month);
    }
    return String.format("%s/%s/%s", year, month, day);
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
