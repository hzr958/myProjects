package com.smate.center.open.utils.xml;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * XML工具类方法.
 * 
 * @author pwl
 */
public class XmlUtils {

  /**
   * 换行符.
   */
  public static final String BR = "<br />";

  /**
   * 影响因子格式.
   */
  private static NumberFormat IMPACT_FACTOR_FORMATTER = new DecimalFormat("0.0000");

  /**
   * 替换Map.
   */
  public static final Map<String, String> replaceWith = new HashMap<String, String>();

  static {
    replaceWith.put("(&amp;)", "&");
    replaceWith.put("(&quot;)", "\"");
    replaceWith.put("(&apos;)", "'");
    replaceWith.put("(&gt;)", ">");
    replaceWith.put("(&lt;)", "<");
    replaceWith.put("(&nbsp;)+", " ");
  }

  /**
   * @param language 语言
   * @param ctext 中文文本
   * @param etext 英文文本
   * @return String
   */
  public static String getLanguageSpecificText(String language, String ctext, String etext) {
    if (ctext == null || "null".equalsIgnoreCase(ctext)) {
      ctext = "";
    }
    if (etext == null || "null".equalsIgnoreCase(etext)) {
      etext = "";
    }

    String lan = language;

    if (lan.equalsIgnoreCase("zh_CN") || lan.equalsIgnoreCase("zh")) {
      if (!"".equals(ctext)) {
        return ctext;
      } else {
        return etext;
      }
    } else {
      if (!"".equals(etext)) {
        return etext;
      } else {
        return ctext;
      }
    }
  }

  /**
   * @param text 文本
   * @return boolean
   */
  public static boolean isEmpty(String text) {
    if (text == null || "null".equalsIgnoreCase(text)) {
      return true;
    }
    text = text.trim();
    return org.apache.commons.lang.StringUtils.isBlank(text);
  }

  /**
   * @param val 字符串对象
   * @return String
   */
  public static String filterNull(Object val) {
    if (val == null) {
      return "";
    }

    return org.apache.commons.lang.StringUtils.trimToEmpty(val.toString());
  }

  /**
   * 是否含有中文字符.
   * 
   * @param str 字符串
   * @return boolean
   */
  public static boolean isSCString(String str) {
    if (isEmpty(str)) {
      return false;
    }
    return str.getBytes().length != str.length();
  }

  /**
   * @param str 数字字符串
   * @return boolean
   */
  public static boolean isNumeric(String str) {
    if (isEmpty(str)) {
      return false;
    }

    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  /**
   * 清除逗号、点号、连续空格,[\\._\\-]直接去掉.
   * 
   * @param name 作者名字
   * @return String
   */
  public static String getCleanAuthorName(String name) {
    if (StringUtils.isBlank(name)) {
      return name;
    }

    name = name.replace(",", " ").replaceAll("\\s*\\.\\s*", ".").replaceAll("\\s*\\-\\s*", "-")
        .replaceAll("[\\._\\-]", " ").replaceAll("\\s+", " ").trim();
    if (name.endsWith(";") || name.endsWith(",")) {
      name = name.substring(0, name.length() - 1);
    }
    return name;
  }

  /**
   * 清除逗号、点号、连续空格,[\\._\\-]替换成空格.
   * 
   * @param name 作者名字
   * @return String
   */
  public static String getCleanAuthorName2(String name) {
    if (StringUtils.isBlank(name)) {
      return name;
    }
    name = name.replace(",", " ").replaceAll("\\s*\\.\\s*", ".").replaceAll("\\s*\\-\\s*", "-")
        .replaceAll("[\\._\\-]", " ").replaceAll("\\s+", " ").trim();
    if (name.endsWith(";") || name.endsWith(",")) {
      name = name.substring(0, name.length() - 1);
    }
    return name;
  }

  /**
   * 去除字符串空格、转成小写字符.
   * 
   * @param str
   * @return
   */
  public static String getTrimBlankLower(String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("\\s+", "").toLowerCase();
  }

  /**
   * 去除字符串多个空格、转成小写字符.
   * 
   * @param str
   * @return
   */
  public static String getTrimDoubleBlankLower(String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("\\s+", " ").toLowerCase();
  }

  /**
   * 格式化.
   * 
   * @param text 文本
   * @return String
   */
  public static String escapeXML(String text) {
    if (isEmpty(text)) {
      return "";
    }

    text = text.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&amp;quot;");// fk

    return text;
  }

  /**
   * @param title 文本
   * @return String
   */
  @SuppressWarnings({"rawtypes"})
  public static String decodeTitle(String title) {
    if (isEmpty(title)) {
      return "";
    }

    Iterator itor = replaceWith.keySet().iterator();
    String result = title;
    while (itor.hasNext()) {
      String reg = String.valueOf(itor.next());
      result = result.replaceAll(reg, replaceWith.get(reg));
    }
    result = result.replaceAll("(\r(\n)?|\n)+", "").trim();
    result = result.replaceAll("\\s+", " ").trim();
    return result;
  }

  /**
   * @param title 文本
   * @return String
   */
  @SuppressWarnings("rawtypes")
  public static String encodeTitle(String title) {
    if (isEmpty(title)) {
      return "";
    }

    Iterator itor = replaceWith.keySet().iterator();
    String result = title;
    while (itor.hasNext()) {
      String reg = String.valueOf(itor.next());
      result = result.replaceAll(replaceWith.get(reg), reg);
    }
    result = result.replaceAll("(\r(\n)?|\n)+", "").trim();
    result = result.replaceAll("\\s+", " ").trim();
    return result;
  }

  /**
   * 移除多余
   * <P>
   * .
   * 
   * @param text 文本
   * @return String
   */
  public static String trimP(String text) {
    if (isEmpty(text)) {
      return "";
    }

    String result = trimStartEndP(text);
    // "<P>" -> "<br>"
    // "<P " -> "<br "
    // "</P>" -> ""

    result = result.replace("<P>", "<br />").replace("<p>", "<br />");
    result = result.replace("<P ", "<br ").replace("<p ", "<br ");
    result = result.replace("</P>", "<br />").replace("</p>", "<br />");
    result = result.trim();

    return result;
  }

  /**
   * 替换WORD中复制的特殊字符，例如"&#1;"等.
   * 
   * @param text
   * @return
   */
  public static String trimWordChar(String text) {

    if (isEmpty(text)) {
      return "";
    }
    Pattern p = Pattern.compile("&#([\\d]{0,10});");
    Matcher matcher = p.matcher(text);
    while (matcher.find()) {
      String g = matcher.group();
      text = text.replace(g, StringEscapeUtils.escapeXml(g));
    }
    return text;
  }

  /**
   * 移除开始、结束
   * <P>
   * .
   * 
   * @param text 文本
   * @return String
   */
  public static String trimStartEndP(String text) {
    if (isEmpty(text)) {
      return "";
    }

    String result = text.trim();
    Pattern p = Pattern.compile("(<P>(&nbsp;)*</P>)", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(result);
    result = m.replaceAll("");

    if (result.startsWith("<P>") || result.startsWith("<p>")) {
      result = result.substring(3);
    }
    if (result.endsWith("</P>") || result.endsWith("</p>")) {
      result = result.substring(0, result.length() - 4);
    }
    result = result.trim();

    return result;
  }

  /**
   * 替换&nbsp;.
   * 
   * @param text 文本
   * @return String
   */
  public static String trimNBSP(String text) {
    if (isEmpty(text)) {
      return "";
    }

    String result = text.trim();
    result = result.replaceAll("(&nbsp;)+", " ").replaceAll("\\s+", " ");
    result = result.trim();

    return result;
  }

  /**
   * 替换不支持的HTML标记.
   * 
   * @param text 文本
   * @return String
   */
  public static String trimThreateningHtml(String text) {
    String result = text;
    if (isEmpty(text)) {
      return "";
    }

    // result = decodeTitle(text);

    Pattern p = Pattern.compile("<script(.*?)>((.|\\n)*?)(<\\/script>)", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(result);
    result = m.replaceAll("");

    p = Pattern.compile("<(i)?frame(.*?)>((.|\\n)*?)(<\\/(i)?frame>)", Pattern.CASE_INSENSITIVE);
    m = p.matcher(result);
    result = m.replaceAll("");

    p = Pattern.compile("<input(.*?)>((.|\\n)*?)(<\\/input>)", Pattern.CASE_INSENSITIVE);
    m = p.matcher(result);
    result = m.replaceAll("");

    p = Pattern.compile("<input(.*?)/?>", Pattern.CASE_INSENSITIVE);
    m = p.matcher(result);
    result = m.replaceAll("");

    p = Pattern.compile("<button(.*?)>((.|\\n)*?)(<\\/button>)", Pattern.CASE_INSENSITIVE);
    m = p.matcher(result);
    result = m.replaceAll("");

    return result;
  }

  /**
   * 替换不支持的HTML标记.
   * 
   * @param text 文本
   * @return String
   */
  public static String trimUnSupportHTML(String text) {
    String result = text;
    if (isEmpty(text)) {
      return "";
    }

    result = result.replaceAll("(&nbsp;)+", " ");

    Pattern p =
        Pattern.compile("<{1}?(?=\\/?(i|sup|sub|em|strike|img|b|strong|span|u)( |>))", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(result);
    result = m.replaceAll("##&##");
    result = result.replaceAll("<[^>]+>", " ").replaceAll("##&##", "<");
    result = org.apache.commons.lang.StringUtils.trimToEmpty(result).trim();
    result = result.replaceAll("(&nbsp;)+", " ");
    result = result.replaceAll("\\s+", " ");

    result = trimThreateningHtml(result);

    return result;
  }

  /**
   * 替换成果编辑中的上标下标等.
   * 
   * @param text 文本
   * @return String
   */
  public static String trimHtmlBoxHTML(String text) {
    String result = text;
    if (isEmpty(text)) {
      return "";
    }

    Pattern p = Pattern.compile("<{1}/?(i|sup|sub|em|b|strong|span|u)>", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(result);
    result = m.replaceAll("");
    return result;
  }

  /**
   * 将字符串名中如"The ","And ","Or ",空格,标点符号全部删除,只留字母及数字,并返回小写字符串.
   * 
   * @param context 文本
   * @return String
   */
  public static String filterForCompare(String context) {
    if (isEmpty(context)) {
      return "";
    }

    context = decodeTitle(context);

    context = context.replaceAll("(&nbsp;)+", " ");
    context = trimAllHtml(context);
    context = context.replaceAll("\n", " ").replaceAll("\r", " ");
    context = context.replaceAll("\\s+", " ").trim();
    context = context.replaceAll("μ", "mu");
    context = context.replaceAll("&mu;", "mu");
    context = context.replaceAll("&amp;", "&");
    context = context.replaceAll("&", "and");
    context = context.replaceAll("%", "percentage");
    context = context.replaceAll("∞", "infinity");
    context = context.replaceAll("°", "degree");
    context = context.replaceAll("√", "root");
    context = context.replaceAll("α", "alpha");
    context = context.replaceAll("β", "beta");

    context = trimSBCChar(context);

    Pattern p = Pattern.compile("[^a-zA-Z0-9\u4e00-\u9fa5]|the | of ", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(context.toLowerCase());

    return m.replaceAll("");
  }

  /**
   * 替换不必要的字符.
   * 
   * @param context 文本
   * @return
   */
  public static String filterForSorting(String context) {
    if (isEmpty(context)) {
      return "";
    }

    context = decodeTitle(context);

    context = context.replaceAll("(&nbsp;)+", " ");
    context = trimAllHtml(context);
    context = context.replaceAll("\n", " ").replaceAll("\r", " ");
    context = context.replaceAll("\\s+", " ");
    context = context.replaceAll("&amp;", "&");

    context = trimSBCChar(context);
    context = context.toLowerCase().trim();

    return context;
  }

  /**
   * 
   * @param text 文本
   * @return String
   */
  public static String trimSBCChar(String text) {
    if (isEmpty(text)) {
      return "";
    }

    Pattern p1 = Pattern.compile("[\uff00-\uffff]", Pattern.CASE_INSENSITIVE);// 这里先将全角符号去除
    Matcher m1 = p1.matcher(text);
    text = m1.replaceAll("").trim();
    return text;
  }

  /**
   * 转半角的函数(DBC case) 任意字符串 半角字符串 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248.
   * 
   * @param text 文本
   * @return String
   */
  public static String changeSBCChar(String text) {
    if (isEmpty(text)) {
      return "";
    }

    char[] c = text.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == 12288) {
        c[i] = (char) 32;
        continue;
      }
      if (c[i] > 65280 && c[i] < 65375) {
        c[i] = (char) (c[i] - 65248);
      }
    }
    return new String(c);
  }

  /**
   * 格式化Impact Factor.
   * 
   * @param factor 数字字符串
   * @return String
   */
  public static String formatImpactFactor(String factor) {
    factor = org.apache.commons.lang.StringUtils.trimToEmpty(factor);
    if (isEmpty(factor)) {
      return "";
    }
    if (!"0".equals(factor)) {
      try {
        BigDecimal bigDecimal = new BigDecimal(factor);
        factor = IMPACT_FACTOR_FORMATTER.format(bigDecimal);
      } catch (RuntimeException e) {
        return "";
      }
    }
    return factor;
  }

  /**
   * 过滤所有HTML标记.
   * 
   * @param text 文本
   * @return String
   */
  public static String trimAllHtml(String text) {
    text = org.apache.commons.lang.StringUtils.trimToEmpty(text);
    text = text.replaceAll("\\<.*?>", "").replaceAll("\\s+", " ");
    return text;
  }

  /**
   * 过滤非数字字符.
   * 
   * @param pubIds 字符串
   * @return String
   * @throws Exception Exception
   */
  public static String filterNaNNumber(String pubIds) throws Exception {
    if (isEmpty(pubIds)) {
      return "";
    }
    pubIds = java.net.URLDecoder.decode(pubIds, "UTF-8");
    String[] temp = pubIds.split(",");
    List<String> res = new ArrayList<String>();

    Pattern pattern = Pattern.compile("[0-9]*");
    for (String id : temp) {
      if (!"".equals(id.trim()) && pattern.matcher(id.trim()).matches()) {
        res.add(id.trim());
      }
    }
    return StringUtils.join(res, ",");
  }

  /**
   * 判断XPath是否元素级别.
   * 
   * @param path xml元素路径
   * @return boolean
   */
  public static boolean isElementPath(String path) {
    if (isEmpty(path)) {
      return false;
    }
    return path.indexOf("/@") < 0;
  }

  /**
   * 判断XPath是否是属性级别.
   * 
   * @param path xml元素路径
   * @return boolean
   */
  public static boolean isAttributePath(String path) {
    if (isEmpty(path)) {
      return false;
    }
    return path.indexOf("/@") > 0;
  }

  /**
   * 拆分xml路径.
   * 
   * @param fullPath (e.g /publication/@en_title will return ["/publication","en_title"]) (e.g
   *        /pub_members/pub_member[1]/@member_psn_name will return
   *        ["/pub_members/pub_member[1]","member_psn_name"]) (e.g /pub_fulltext will return
   *        ["/pub_fulltext",""])
   * @return [path_only,attribute_name]
   */
  public static String[] splitXPath(String fullPath) {
    if (isEmpty(fullPath)) {
      return null;
    }
    String[] tmp = fullPath.split("/@");
    String[] ret = new String[2];
    ret[0] = tmp[0];
    if (tmp.length <= 1) {
      ret[1] = "";
    } else {
      ret[1] = tmp[1];
    }
    return ret;
  }

  /**
   * @param xpath (e.g. /pub_members/pub_member[1] will return 1) (e.g. /pub_fulltext will return
   *        null)
   * @return
   */
  public static Integer getSeqNo(String xpath) {
    if (isEmpty(xpath)) {
      return null;
    }
    int pos = xpath.lastIndexOf("[");
    if (pos < 0) {
      return null;
    }
    int end = xpath.lastIndexOf("]");
    if (end < 0) {
      return null;
    }
    String str = xpath.substring(pos + 1, end);
    return Integer.parseInt(str);
  }

  /**
   * 删除逗号前后的空格，LQH ADD.
   * 
   * @param str
   * @return
   */
  public static String removeCommaBlank(String str) {

    if (str == null) {
      return null;
    }
    return str.replaceAll("\\s*,\\s*", ",");
  }

  /**
   * 删除括号前后的空格，LQH ADD.
   * 
   * @param str
   * @return
   */
  public static String removeBracketBlank(String str) {

    if (str == null) {
      return null;
    }
    // 括号左右的空格全部清除；
    str = str.replaceAll("\\s*\\(\\s*", "(");
    str = str.replaceAll("\\s*\\)\\s*", ")");
    return str;
  }

  public static boolean isChinese(String string) {
    string = StringUtils.trimToEmpty(string);
    char[] ch = string.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
      if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
          || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
          || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
          || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
          || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
          || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
        return true;
      }
    }
    return false;
  }

}
