package com.smate.center.batch.util.pub;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.dom4j.Element;

import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;

/**
 * Xml净化工具类型.
 * 
 * @author yamingd
 */
public class XmlFragmentCleanerHelper {

  /**
   * @param year 年
   * @param month 月
   * @param day 日
   * @param pattern 格式
   * @return String
   * @throws Exception Exception
   */
  public static String formatDate(int year, int month, int day, String pattern) throws Exception {
    try {

      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month - 1);
      cal.set(Calendar.DATE, day);
      SimpleDateFormat f = new SimpleDateFormat(pattern);
      String res = f.format(cal.getTime());

      return res;

    } catch (Exception e) {

      throw e;

    }
  }

  /**
   * 把日期分隔成年、月、日.
   * 
   * @param doc XmlDocument
   * @param xpath xml元素路径
   * @param attrName 属性名
   * @param pattern 格式
   * @return String[]
   */
  public static String[] splitDateYearMonth(PubXmlDocument doc, String xpath, String attrName, String pattern) {
    String date = doc.getXmlNodeAttribute(xpath, attrName);
    if ("".equals(date)) {
      return new String[] {"", "", ""};
    }

    return getDateYearMonth(pattern, date);
  }

  /**
   * 把日期分隔成年、月、日.
   * 
   * @param attrName 属性名
   * @param pattern 格式
   * @return String[]
   */
  public static String[] splitDateYearMonth(Element ele, String attrName, String pattern) {
    String date = ele.attributeValue(attrName);
    if (date == null || "".equals(date.trim())) {
      return new String[] {"", "", ""};
    }
    return getDateYearMonth(pattern, date);
  }

  /**
   * @param pattern 格式
   * @param date 日期字符串
   * @return String[]
   */
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
   * @param doc XmlDocument
   * @param xpathPrefix 日期元素路径前缀
   * @param pattern 格式
   * @throws InvalidXpathException InvalidXpathException
   */
  public static void setDateValueFromYMD(PubXmlDocument doc, String xpathPrefix, String pattern)
      throws InvalidXpathException {
    // /data/publication/@publish_year
    String year = doc.getXmlNodeAttribute(xpathPrefix + "year");
    String month = doc.getXmlNodeAttribute(xpathPrefix + "month");
    String day = doc.getXmlNodeAttribute(xpathPrefix + "day");

    year = XmlUtil.changeSBCChar(year);
    month = XmlUtil.changeSBCChar(month);
    day = XmlUtil.changeSBCChar(day);

    String prefix = xpathPrefix.substring(xpathPrefix.lastIndexOf("/") + 1);
    xpathPrefix = xpathPrefix.substring(0, xpathPrefix.lastIndexOf("/"));

    doc.setXmlNodeAttribute(xpathPrefix, prefix + "year", year);
    doc.setXmlNodeAttribute(xpathPrefix, prefix + "month", month);
    doc.setXmlNodeAttribute(xpathPrefix, prefix + "day", day);

    if ("".equals(year)) {
      return;
    }

    if ("".equals(month)) {
      doc.setXmlNodeAttribute(xpathPrefix, prefix + "date", year);
      return;
    }
    if ("".equals(day)) {
      if (PubXmlConstants.CHS_DATE_PATTERN.equalsIgnoreCase(pattern)) {
        doc.setXmlNodeAttribute(xpathPrefix, prefix + "date", year + "/" + month);
      } else {
        doc.setXmlNodeAttribute(xpathPrefix, prefix + "date", month + "/" + year);
      }
      return;
    }
    if (PubXmlConstants.CHS_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      doc.setXmlNodeAttribute(xpathPrefix, prefix + "date", String.format("%s/%s/%s", year, month, day));
    } else {
      doc.setXmlNodeAttribute(xpathPrefix, prefix + "date", String.format("%s/%s/%s", day, month, year));
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    String[] ds = getDateYearMonth("yyyy-MM-dd", "2008-7-");
    System.out.println(ds[0] + "?" + ds[1] + "?" + ds[2]);

    System.out.println(XmlUtil.changeSBCChar("２００８"));

    try {

      System.out.println(formatDate(2008, 12, 15, "yyyy/M/d"));

      System.out.println(formatDate(2008, 12, 1, "yyyy/M"));

      System.out.println(formatDate(2008, 1, 1, "yyyy"));

      System.out.println(formatDate(1900, 1, 0, "yyyy"));

      System.out.println(formatDate(0, 1, 0, "yyyy"));

    } catch (Exception e) {

      e.printStackTrace();

    }
  }

}
