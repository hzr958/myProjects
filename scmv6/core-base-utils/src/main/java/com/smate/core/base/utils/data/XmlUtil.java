package com.smate.core.base.utils.data;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * @author zjh XML工具类方法
 */
public class XmlUtil {

  /**
   * 换行符.
   */
  public static final String BR = "<br />";

  private static final String[] specialLowerArr = new String[] {"at", "before", "for", "from", "in", "next", "of",
      "over", "since", "to", "under", "with", "inside", "into", "out", "outside", "upon", "within", "without", "above",
      "below", "except", "but", "and", "the", "by", "low"};

  private static final String[] specialUpperArr = new String[] {"dna"};
  private static final String[] frenchAccentLetter =
      new String[] {"à", "â", "ä", "è", "é", "ê", "ë", "î", "ï", "ô", "œ", "ù", "û", "ü", "ÿ", "ç", "À", "Â", "Ä", "È",
          "É", "Ê", "Ë", "Î", "Ï", "Ô", "Œ", "Ù", "Û", "Ü", "Ÿ", "Ç", "a", "a", "a", "e", "e", "e", "e", "i", "i", "o",
          "œ", "u", "u", "u", "y", "c", "A", "A", "A", "E", "E", "E", "E", "I", "I", "O", "Œ", "U", "U", "U", "Y", "C"};
  private static final String frenchAccentLetterStr = "àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ";
  private static Pattern parentheses = Pattern.compile("(?<=\\()[^\\)]+");
  private static Pattern brackets = Pattern.compile("(?<=\\[)[^\\]]+");
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
   * 替换Map.
   */
  public static final Map<String, String> replaceTags = new HashMap<String, String>();
  static {
    replaceTags.put("(&nbsp;)+", " ");
    replaceTags.put("(&mdash;)", "—");
    replaceTags.put("(&copy;)", "©");
    replaceTags.put("(&reg;)", "®");
    replaceTags.put("(&trade;)", "™");
    replaceTags.put("(&pound;)", "£");
    replaceTags.put("(&yen;)", "¥");
    replaceTags.put("(&euro;)", "€");
    /*
     * replaceTags.put("(&quot;)", "\""); replaceTags.put("(&apos;)", "'"); replaceTags.put("(&ldquo;)",
     * "“"); replaceTags.put("(&rdquo;)", "”"); replaceTags.put("(&rsquo;)", "’");
     * replaceTags.put("(&lsquo;)", "‘"); replaceTags.put("(&raquo;)", "»");
     * replaceTags.put("(&laquo;)", "«"); replaceTags.put("(&gt;)", ">"); replaceTags.put("(&lt;)",
     * "<");
     */

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
   * （以最后一个；或者;为分隔符）截取500个字符长度
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年5月31日
   */
  public static String subStr500char(String string) {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    string = string.replace("；", ";");// 统一为英文分隔符
    string = StringUtils.substring(string, 0, 500);// 截取500字符
    if (string.length() == 500) {// 如果是500字符长度 则可能是被截取了，需要找最后的分号再次截取
      int index = string.lastIndexOf(";");
      if (index > 0) {
        string = string.substring(0, index);
      }
    }
    return string;
  }

  /**
   * SCM-17620 作者的分隔未统一按英文分号加英文空格进行分隔，最后一个作者不需要分号；
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年6月30日
   */
  public static String formatPubAuthorKws(String string) {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    String[] split = string.split(";");
    StringBuffer sBuffer = new StringBuffer();
    for (String string2 : split) {
      sBuffer.append(string2.trim() + "; ");
    }
    string = sBuffer.toString();
    string = StringUtils.substring(string, 0, string.length() - 2);// 去除末尾分号空格
    return string;

  }

  /**
   * 清理作者里面可能存在的单位信息， 方法 如果作者里面含有()或者[]包含起来的内容 且包含的内容去除空格后长度大于50个字符则清理掉
   * 
   * @param authors
   * @return
   * @author LIJUN
   * @date 2018年7月4日
   */

  public static String cleanAuthorsAddr(String authors) {
    if (StringUtils.isBlank(authors)) {
      return "";
    }
    if (authors.contains("[") && authors.contains("]")) {
      Matcher m = brackets.matcher(authors);
      while (m.find()) {
        if (m.group() != null && m.group().replaceAll("\\s*", "").length() > 30) {
          authors = authors.replace("[" + m.group() + "]", "");
        }
      }
    }
    if (authors.contains("(") && authors.contains(")")) {
      Matcher m = parentheses.matcher(authors);
      while (m.find()) {
        if (m.group() != null && m.group().replaceAll("\\s*", "").length() > 50) {
          authors = authors.replace("(" + m.group() + ")", "");
        }
      }
    }
    return authors;
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
   * 替换常见的html标签编码
   */
  public static String replaceXmlHtmlTags(String xmlStr) {
    if (isEmpty(xmlStr)) {
      return "";
    }

    Iterator<String> itor = replaceTags.keySet().iterator();
    while (itor.hasNext()) {
      String reg = String.valueOf(itor.next());
      xmlStr = xmlStr.replaceAll(reg, replaceTags.get(reg));
    }
    return xmlStr;

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
   * 公共类请勿修改，2018-11-19--验证成果的 清除逗号、点号,[\*\\._\\-]全部直接去掉. (#) (*)<b></b> 过滤掉 名字转小写
   * 
   * @param name
   * @return
   */
  public static String getCleanAuthorName4(String name) {
    if (StringUtils.isBlank(name)) {
      return "";
    }
    name = name.replaceAll("(<.*?>)|(\\(.?\\))", "").replaceAll("[\\*,\\._\\-]", "").trim();
    name = name.replaceAll("\\u00A0", "");
    return name.toLowerCase();
  }

  /**
   * 公共类请勿修改，2018-11-19--验证成果的 清除逗号、点号,[\*\\._\\-]全部直接去掉. (#) (*)<b></b> 过滤掉 名字转小写 清除 and 单词
   * 
   * @param name
   * @return
   */
  public static String getCleanAuthorName5(String name) {
    if (StringUtils.isBlank(name)) {
      return "";
    }
    name = name.replaceAll("(<.*?>)|(\\(.*?\\))|(（.*）)", " ").replaceAll("[\\*,\\._\\-]", " ");
    name = name.replaceAll("\\u00A0", " ");
    name = name.replaceAll("\\s{2,}", " ");
    name = name.replaceAll("&", " ");
    name = name.replaceAll("[\\d]", "");
    name = name.toLowerCase().trim();
    name = name.replaceAll("(^and\\s+)|(\\s+and$)", " ");
    return name.trim();
  }

  /**
   * 单个名字，去除所有的空格
   * @param name
   * @return
   */
  public static String getCleanAuthorName6(String name) {
    name = getCleanAuthorName5(name);
    name =  name.replaceAll("\\s","");
    return name;
  }
  public static String beforeCompareAurhorName(String name) {
    if (StringUtils.isBlank(name)) {
      return "";
    }
    name = name.replaceAll("（", "(").replaceAll("）", ")");
    name = replaceSpecialStr(name);
    return name.toLowerCase().trim();
  }

  public static String replaceSpecialStr(String name) {
    int firIdx = name.indexOf("(");
    int secIdx = name.indexOf(")");
    if (firIdx > -1 && secIdx > firIdx) {
      name = name.substring(0, firIdx) + name.substring(secIdx + 1, name.length());
      return replaceSpecialStr(name);
    } else {
      return name;
    }
  }

  /**
   * 清除逗号、点号、连续空格,[\\._\\-]全部直接去掉.
   * 
   * @param name
   * @return
   */
  public static String getCleanAuthorName3(String name) {
    if (StringUtils.isBlank(name)) {
      return "";
    }
    name = name.replace(",", "").replaceAll("\\s*\\.\\s*", "").replaceAll("\\s*\\-\\s*", "").replaceAll("[\\._\\-]", "")
        .replaceAll("\\s+", "").trim();
    if (name.endsWith(";") || name.endsWith(",")) {
      name = name.substring(0, name.length() - 1);
    }
    return name;
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
  @SuppressWarnings("unchecked")
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
  @SuppressWarnings("unchecked")
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
   * 将xml作者常见字符替换为空格汉字 如果有连续空格则只保留一个并转小写；;分隔符不能替换
   * 
   * @param string
   * @return
   * @author LIJUN
   * @throws UnsupportedEncodingException
   * @date 2018年3月26日
   */
  public static String replaceXMLAuthorChars(String string) throws UnsupportedEncodingException {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    string = HtmlUtils.htmlUnescape(URLDecoder.decode(string, "utf-8"));
    String regEx = "[`~!@#$%^&*()+=|{}':',\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll(" ").trim().replace(" ", "空格");
    while (string.contains("空格空格")) {
      string = string.replace("空格空格", "空格");
    }
    return string.toLowerCase();

  }

  /**
   * 将xml作者常见字符替换为空格 如果有连续空格则只保留一个并转小写；;分隔符不能替换
   * 
   * @param string
   * @return
   * @author LIJUN
   * @throws UnsupportedEncodingException
   * @date 2018年3月26日
   */
  public static String cleanXMLAuthorChars(String string) throws UnsupportedEncodingException {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    string = HtmlUtils.htmlUnescape(URLDecoder.decode(string, "utf-8"));
    String regEx = "[`~!@#$%^&*()+=|{}':',\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll(" ").trim();
    while (string.contains("  ")) {
      string = string.replace("  ", " ");
    }
    return string.toLowerCase();

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
   * 过滤作者中的字符，用于比较.
   * 
   * @param auName
   * @return
   */
  public static String filterAuForCompare(String auName) {

    if (StringUtils.isBlank(auName)) {
      return null;
    }
    // html解码，删除所有html文本空格，分隔符
    auName = HtmlUtils.htmlUnescape(auName);
    auName = XmlUtil.trimAllHtml(auName);
    auName = XmlUtil.filterForCompare(auName);
    auName = auName.replaceAll("[\\.,，;；'‘\"\\(\\)（）\\[\\]]{1,}", "");
    return auName;
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
    context = context.replaceAll("&mu;", "mu");
    context = context.replaceAll("&amp;", "&");
    context = context.replaceAll("&", "and");

    context = context.replaceAll("%", "percentage");
    context = context.replaceAll("∞", "infinity");
    context = context.replaceAll("°", "degree");
    context = context.replaceAll("√", "root");

    // Α α：阿尔法 Alpha
    context = context.replaceAll("Α", "alpha");
    context = context.replaceAll("α", "alpha");
    // Β β：贝塔 Beta
    context = context.replaceAll("Β", "beta");
    context = context.replaceAll("β", "beta");
    // Γ γ：伽玛 Gamma
    context = context.replaceAll("Γ", "gamma");
    context = context.replaceAll("γ", "gamma");
    // Δ δ：德尔塔 Delte
    context = context.replaceAll("Δ", "delta");
    context = context.replaceAll("δ", "delta");
    // Ε ε：艾普西龙 Epsilon
    context = context.replaceAll("Ε", "epsilon");
    context = context.replaceAll("ε", "epsilon");
    // Ζ ζ ：捷塔 Zeta
    context = context.replaceAll("Ζ", "zeta");
    context = context.replaceAll("ζ", "zeta");
    // Ε η：依塔 Eta
    context = context.replaceAll("Ε", "eta");
    context = context.replaceAll("η", "eta");
    // Θ θ：西塔 Theta
    context = context.replaceAll("Θ", "theta");
    context = context.replaceAll("θ", "theta");
    // Ι ι：艾欧塔 Iota
    context = context.replaceAll("Ι", "iota");
    context = context.replaceAll("ι", "iota");
    // Κ κ：喀帕 Kappa
    context = context.replaceAll("Κ", "kappa");
    context = context.replaceAll("κ", "kappa");
    // ∧ λ：拉姆达 Lambda
    context = context.replaceAll("∧", "lambda");
    context = context.replaceAll("λ", "lambda");
    // Μ μ：缪 Mu
    context = context.replaceAll("Μ", "mu");
    context = context.replaceAll("μ", "mu");
    // Ν ν：拗 Nu
    context = context.replaceAll("Ν", "nu");
    context = context.replaceAll("ν", "nu");
    // Ξ ξ：克西 Xi
    context = context.replaceAll("Ξ", "xi");
    context = context.replaceAll("ξ", "xi");
    // Ο ο：欧麦克轮 Omicron
    context = context.replaceAll("Ο", "omicron");
    context = context.replaceAll("ο", "omicron");
    // ∏ π：派 Pi
    context = context.replaceAll("∏", "pi");
    context = context.replaceAll("π", "pi");
    // Ρ ρ：柔 Rho
    context = context.replaceAll("Ρ", "rho");
    context = context.replaceAll("ρ", "rho");
    // ∑ σ：西格玛 Sigma
    context = context.replaceAll("∑", "sigma");
    context = context.replaceAll("σ", "sigma");
    // Τ τ：套 Tau
    context = context.replaceAll("Τ", "tau");
    context = context.replaceAll("τ", "tau");
    // Υ υ：宇普西龙 Upsilon
    context = context.replaceAll("Υ", "upsilon");
    context = context.replaceAll("υ", "upsilon");
    // Φ φ：fai Phi
    context = context.replaceAll("Φ", "phi");
    context = context.replaceAll("φ", "phi");
    // Χ χ：器 Chi
    context = context.replaceAll("Χ", "chi");
    context = context.replaceAll("χ", "chi");
    // Ψ ψ：普赛 Psi
    context = context.replaceAll("Ψ", "psi");
    context = context.replaceAll("ψ", "psi");
    // Ω ω：欧米伽 Omega
    context = context.replaceAll("Ω", "omega");
    context = context.replaceAll("ω", "omega");
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

  /**
   * <pre>
   *   钟慧敏要求增加：别名处理过程中特殊字符的处理
   * 1、& / - ‘ 全部用空格代替；
   * 如res&dev,则用res dev代替
   * 2、's用空格s代替；
   * 如teacher's,则转为teacher s
   * 3、括号左右的空格全部清除；
   * 4、将所有连续两个空格及以上的空格用一个空格代替；
   * 5、以后抓取的数据提议程序员对数据进行些许处理；
   * </pre>
   * 
   * @param str
   * @return
   */
  public static String clearEnPubAddrMatchSpecChar(String str) {

    if (str == null) {
      return null;
    }
    if (StringUtils.isBlank(str)) {
      return "";
    }
    // 分号全部换成逗号
    str = str.replaceAll("[;]{1,}", ",");
    // 点全部换成逗号
    str = str.replaceAll("[\\.]{1,}", ",");

    // 多个空格换成一个空格
    str = str.replaceAll("[&/\\-']", " ");
    // 清除逗号前后空格
    str = StringUtils.lowerCase(StringUtils.trim(removeCommaBlank(str)));
    // 清除括号前后空格
    str = StringUtils.lowerCase(StringUtils.trim(removeBracketBlank(str)));
    // 多个逗号换成一个逗号
    str = str.replaceAll("[\\,]{1,}", ",");
    // 多个空格换成一个空格
    str = str.replaceAll("\\s+", " ");
    return str.trim();
  }

  /**
   * 解析isi成果机构名称.
   * 
   * @param doc
   * @return @throws
   */
  public static Set<String> parseIsiPubAddrs(String pubaddrStr) {

    if (StringUtils.isNotBlank(pubaddrStr)) {
      // 删除作者括号
      pubaddrStr = pubaddrStr.replaceAll("\\[.*?\\]", " ");
      pubaddrStr = pubaddrStr.replaceAll("\\s+", " ");
      String[] pubAddrs = StringUtils.split(pubaddrStr, ".");
      Set<String> pubAddrSet = new HashSet<String>();
      for (String pubAddr : pubAddrs) {
        if (StringUtils.isNotBlank(pubAddr)) {
          pubAddrSet.add(pubAddr.trim().toLowerCase());
        }
      }
      return pubAddrSet;
    }
    return null;
  }

  /**
   * 解析crossref成果机构名称.
   * 
   * @param doc
   * @return @throws
   */
  public static Set<String> parseCrossrefPubAddrs(String pubaddrStr) {
    if (StringUtils.isNotBlank(pubaddrStr)) {
      // 删除作者括号
      pubaddrStr = pubaddrStr.replaceAll("\\[.*?\\]", " ").replaceAll(",", " ");
      pubaddrStr = pubaddrStr.replaceAll("\\s+", " ");
      String[] pubAddrs = StringUtils.split(pubaddrStr, ".");
      Set<String> pubAddrSet = new HashSet<String>();
      for (String pubAddr : pubAddrs) {
        if (StringUtils.isNotBlank(pubAddr)) {
          pubAddrSet.add(pubAddr.trim().toLowerCase());
        }
      }
      return pubAddrSet;
    }
    return null;
  }

  /**
   * 解析PubMed成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parsePubMedPubAddrs(String pubaddrStr) {

    if (StringUtils.isNotBlank(pubaddrStr)) {
      // 删除作者括号
      pubaddrStr = pubaddrStr.replaceAll("\\[.*?\\]", " ");
      pubaddrStr = pubaddrStr.replaceAll("\\s+", " ");
      String[] pubAddrs = StringUtils.split(pubaddrStr, ".");
      Set<String> pubAddrSet = new HashSet<String>();
      for (String pubAddr : pubAddrs) {
        if (StringUtils.isNotBlank(pubAddr)) {
          pubAddrSet.add(pubAddr.trim().toLowerCase());
        }
      }
      return pubAddrSet;
    }
    return null;
  }

  /**
   * 解析cnki成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parseCnkiPubAddrs(String pubaddrStr) {

    if (StringUtils.isNotBlank(pubaddrStr)) {
      // 删除作者括号
      pubaddrStr = pubaddrStr.replaceAll("\\[.*?\\]", " ");
      pubaddrStr = pubaddrStr.replaceAll("\\s+", " ");
      String[] pubAddrs = StringUtils.split(pubaddrStr, ";");
      Set<String> pubAddrSet = new HashSet<String>();
      for (String pubAddr : pubAddrs) {
        if (StringUtils.isNotBlank(pubAddr)) {
          pubAddrSet.add(pubAddr.trim().toLowerCase());
        }
      }
      return pubAddrSet;
    }
    return null;
  }

  /**
   * 解析Cnipr成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parseCniprPubAddrs(String pubaddrStr) {

    if (StringUtils.isNotBlank(pubaddrStr)) {
      // 删除作者括号
      pubaddrStr = pubaddrStr.replaceAll("\\[.*?\\]", " ");
      pubaddrStr = pubaddrStr.replaceAll("\\s+", " ");
      String[] pubAddrs = StringUtils.split(pubaddrStr, ";");
      Set<String> pubAddrSet = new HashSet<String>();
      for (String pubAddr : pubAddrs) {
        if (StringUtils.isNotBlank(pubAddr)) {
          pubAddrSet.add(pubAddr.trim().toLowerCase());
        }
      }
      return pubAddrSet;
    }
    return null;
  }

  /**
   * 解析scopus成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parseScopusPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 解析ScienceDirect成果机构名称.
   * 
   * @param doc
   * @return
   *
   */
  public static Set<String> parseScienceDirectPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 解析scopus成果机构名称.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  public static Set<String> parseIEEEXpPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 解析Ei成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parseEiPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 解析wanfang成果机构名称.
   * 
   * @param doc
   * @return
   * 
   */
  public static Set<String> parseWfPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 解析百度成果机构名称.
   * 
   * @param doc
   * @return
   *
   */
  public static Set<String> parseBaiduPubAddrs(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {
      orgsStr = removeCommaBlank(orgsStr);
      String[] orgs = StringUtils.split(orgsStr, ";");
      Set<String> orgsSet = new HashSet<String>();
      for (String org : orgs) {
        String orgStr = org;
        if (StringUtils.isNotBlank(org)) {
          orgStr = orgStr.replaceAll("\\s+", " ");
          orgsSet.add(orgStr.trim().toLowerCase());
        }
      }
      return orgsSet;
    }
    return null;
  }

  /**
   * 构造标准的issn.
   * 
   * @param issn
   * @return
   */
  public static String buildStandardIssn(String issn) {

    if (StringUtils.isNotBlank(issn) && issn.length() > 4) {
      String tmpIssn = issn;
      if (StringUtils.indexOf(tmpIssn, "-") > -1 && StringUtils.indexOf(tmpIssn, "-") != 4) {
        tmpIssn = StringUtils.replace(tmpIssn, "-", "");
      }
      if (StringUtils.indexOf(tmpIssn, "-") == -1 && tmpIssn.length() > 4) {
        String tmp1 = StringUtils.substring(tmpIssn, 0, 4);
        String tmp2 = StringUtils.substring(tmpIssn, 4);
        tmpIssn = tmp1 + "-" + tmp2;
      }
      return tmpIssn;
    }
    return issn;
  }

  /**
   * 比较作者名是否一致.
   * 
   * @param auName1
   * @param auName2
   * @return
   */
  public static boolean compareAuthorName(String auName1, String auName2) {
    if (StringUtils.isBlank(auName1) || StringUtils.isBlank(auName2)) {
      return false;
    }

    // 清除特殊字符再比较
    String cleanAuName1 = XmlUtil.getSimpleCleanAuthorName(auName1);
    String cleanAuName2 = XmlUtil.getSimpleCleanAuthorName(auName2);
    return cleanAuName1.equalsIgnoreCase(cleanAuName2);
  }

  /**
   * 比较作者名是否一致.
   * 
   * @param auName1
   * @param auName2
   * @return
   */
  public static boolean compareAuthorNameClear(String auName1, String auName2) {
    if (StringUtils.isBlank(auName1) || StringUtils.isBlank(auName2)) {
      return false;
    }

    // 清除特殊字符再比较
    String cleanAuName1 = XmlUtil.getCleanAuthorName(auName1);
    String cleanAuName2 = XmlUtil.getCleanAuthorName(auName2);
    boolean equal = cleanAuName1.equalsIgnoreCase(cleanAuName2);
    if (!equal) {
      cleanAuName1 = XmlUtil.getCleanAuthorName2(auName1);
      cleanAuName2 = XmlUtil.getCleanAuthorName2(auName2);
      equal = cleanAuName1.equalsIgnoreCase(cleanAuName2);
    }
    return equal;
  }

  /**
   * 简单清除逗号、连续空格，点号、杆号前后空格.
   * 
   * @param name 作者名字
   * @return String
   */
  public static String getSimpleCleanAuthorName(String name) {
    name = name.replace(",", " ").replace(";", " ").replaceAll("\\s*\\.\\s*", ".").replaceAll("\\s*\\-\\s*", "-")
        .replaceAll("\\s+", " ").trim();
    return name;
  }

  /**
   * 拆分关键词.
   * 
   * @param keywords
   * @return
   */
  public static Set<String> splitKeywords(String keywords) {

    if (StringUtils.isBlank(keywords)) {
      return null;
    }
    keywords = keywords.replaceAll("[；。]", ";").replaceAll("\\s+", " ");
    String[] keywordA = StringUtils.split(keywords, ";");
    Set<String> set = new HashSet<String>();
    for (String keyword : keywordA) {
      if (StringUtils.isNotBlank(keyword)) {
        set.add(keyword.trim());
      }
    }
    return set;
  }

  /**
   * 判断字符串是否有中文
   * 
   * @param name
   * @return
   */
  public static boolean containZhChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断字符串是否有英文
   * 
   * @param name
   * @return
   */
  public static boolean containEnChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[A-Za-z]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 格式化各文献库作者名和期刊名大小写.
   * 
   * @param authors
   * @author lichangwen
   * @return
   */
  public static String formatJnlTitle(String title) {

    if (StringUtils.isBlank(title))
      return "";
    // 全部是大写才格式化
    if (title.equals(title.toUpperCase())) {
      String newString = "";
      for (int i = title.length() - 1; i >= 0; i--) {
        if (i > 0) {
          char b = title.charAt(i);
          char a = title.charAt(i - 1);
          if (Character.isLetter(b) && !Character.isLetter(a)) {
            newString += String.valueOf(b).toUpperCase();
          } else {
            newString += String.valueOf(b).toLowerCase();
          }
        } else {
          newString += String.valueOf(title.charAt(i)).toUpperCase();
        }
      }
      String resultStr = "";
      String[] resultArr = StringUtils.reverse(newString).split(" |,|;");
      for (int i = 0; i < resultArr.length; i++) {
        if (StringUtils.isBlank(resultArr[i]))
          continue;
        resultArr[i] = resultArr[i].length() == 2 ? resultArr[i].toUpperCase() : resultArr[i];
        if (ArrayUtils.contains(specialLowerArr, resultArr[i].toLowerCase())) {
          resultStr += " " + resultArr[i].toLowerCase();
        } else if (ArrayUtils.contains(specialUpperArr, resultArr[i].toLowerCase())) {
          resultStr += " " + resultArr[i].toUpperCase();
        } else {
          resultStr += " " + resultArr[i];
        }
      }
      return StringUtils.trimToEmpty(resultStr);
    } else {
      return title;
    }
  }

  public static String formatAuthors(String authors) {

    if (StringUtils.isBlank(authors))
      return "";
    // if (authors.indexOf("(") != -1) {//scm-6293 文件导入，作者名一栏含括号时，后面的信息会丢失
    // authors = authors.substring(0, authors.indexOf("("));
    // }
    // 全部是大写才格式化
    if (authors.equals(authors.toUpperCase())) {
      String newString = "";
      for (int i = authors.length() - 1; i >= 0; i--) {
        if (i > 0) {
          char b = authors.charAt(i);
          char a = authors.charAt(i - 1);
          if (Character.isLetter(b) && !Character.isLetter(a)) {
            newString += String.valueOf(b).toUpperCase();
          } else {
            newString += String.valueOf(b).toLowerCase();
          }
        } else {
          newString += String.valueOf(authors.charAt(i)).toUpperCase();
        }
      }
      String resultStr = "";
      String[] resultArr = StringUtils.reverse(newString).split("；|;| and ");
      for (int i = 0; i < resultArr.length; i++) {
        if (StringUtils.isBlank(resultArr[i]))
          continue;
        resultArr[i] = resultArr[i].length() == 2 ? resultArr[i].toUpperCase() : resultArr[i];
        if (ArrayUtils.contains(specialLowerArr, resultArr[i].toLowerCase())) {
          resultStr += ";" + resultArr[i].toLowerCase();
        } else if (ArrayUtils.contains(specialUpperArr, resultArr[i].toLowerCase())) {
          resultStr += ";" + resultArr[i].toUpperCase();
        } else {
          resultStr += ";" + resultArr[i];
        }
      }
      if (resultStr.length() > 0)
        return resultStr.substring(1);
      else
        return resultStr;
    } else {
      return authors;
    }
  }

  /**
   * 格式化各文献库title名大小写.
   * 
   * @param title
   * @author lichangwen
   * @return
   */
  public static String formatTitle(String title) {
    if (StringUtils.isBlank(title))
      return "";
    if (title.equals(title.toUpperCase())) {
      if (title.length() > 1) {
        title = title.toLowerCase();
        title = String.valueOf(title.charAt(0)).toUpperCase() + title.substring(1);
      }
    }
    return title;
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

  /**
   * 格式成果列表中作者标题行显示字段,如果字段中有非标点符号的中文则将该字段中的所有标点符号修改为中文标点符号，反之亦然.
   * 
   * @param briefDesc
   */
  public static String formateSymbol(String title, String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(title) || StringUtils.isBlank(text))
      return "";
    boolean isChinese = isChinese(filterForCompare(title));
    if (isChinese) {
      text = text.replace(",", "，").replace(";", "；");
      // text = text.replace(".", "。");
    } else {
      // text = text.replace(".", ". ");
      text = text.replace(",", ", ");
      text = text.replace(";", "; ");
      text = text.replace("，", ", ").replace("；", "; ");
      // text = text.replace("。", ". ");
    }
    return text;
  }

  public static String formateSymbolAuthors(String title, String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(title) || StringUtils.isBlank(text))
      return "";
    // boolean isChinese = isChinese(filterForCompare(title));
    text = text.replace("；", ";");
    text = text.replace(" ;", ";");
    text = text.replace("; ", ";");
    text = text.replace(";", "; ");
    /*
     * if (isChinese) { text = text.replace(";", "；").replace(" ", "").replace("&#39；", "&#39; "); //
     * text = text.replace(",", "；").replace("，", "；"); } else { text = text.replace(" ;", ";"); text =
     * text.replace(";", "; "); // text = text.replace("，", "; ").replace("；", "; "); }
     */
    return text;
  }

  public static String formateSymbolForJnlPubs(String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(text))
      return "";
    text = text.replace(".", ". ");
    text = text.replace(",", ", ");
    text = text.replace(";", "; ");
    text = text.replace("，", ", ").replace("；", "; ");
    text = text.replace("。", ". ");
    return text;
  }

  public static String replacePdwhAllPubType(String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(text))
      return "";
    text = text.replace("。奖励", "");
    text = text.replace(".奖励", "");
    text = text.replace(".Award", "");
    text = text.replace("。书/著作", "");
    text = text.replace(".书/著作", "");
    text = text.replace(".Book/Monograph", "");
    text = text.replace("。书籍章节", "");
    text = text.replace(".书籍章节", "");
    text = text.replace(".Book Chapter", "");
    text = text.replace("。会议论文", "");
    text = text.replace(".会议论文", "");
    text = text.replace(".Conference Paper", "");
    text = text.replace("。期刊论文", "");
    text = text.replace(".期刊论文", "");
    text = text.replace(".Journal Article", "");
    text = text.replace(".其它", "");
    text = text.replace("。其它", "");
    text = text.replace(".Others", "");
    text = text.replace("。专利", "");
    text = text.replace(".专利", "");
    text = text.replace(".Patent", "");
    text = text.replace("。学位论文", "");
    text = text.replace(".学位论文", "");
    text = text.replace(".Thesis", "");
    return text;
  }

  public static String formateSymbolAuthorsForJnlPubs(String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(text))
      return "";
    text = text.replace(";", "; ");
    text = text.replace("，", "; ").replace("；", "; ");
    return text;
  }

  /**
   * 格式来源
   * 
   * @param text
   * @return
   */
  public static String formatBriefDesc(String text) {
    text = StringUtils.trimToEmpty(text);
    if (StringUtils.isBlank(text))
      return "";
    // 中文标点变英文
    text = text.replace("；", ";");
    text = text.replace("，", ",");
    text = text.replace("。", ".");
    // 标点前面多空格的去掉空格
    text = text.replace(" .", ".");
    text = text.replace(" ,", ",");
    text = text.replace(" ;", ";");
    // 标点后接空格
    // text = text.replace(".", ". ");
    text = text.replace(",", ", ");
    text = text.replace(";", "; ");
    // 多个空格变一个空格
    text = text.replaceAll("\\s+", " ");
    // 项目来源中的金额用(HKD 172,000.00)
    String regex = "[A-Z]{3}(\\s*)([0-9]*,*,*)(\\s*)([0-9]*)(,?\\s*)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      String a = matcher.group(0);
      String b = a.replace(", ", ",");
      text = text.replace(a, b);
      if (text.contains(". 00")) {
        text = text.replace(". 00", ".00");
      }
    }
    return text;
  }

  /**
   * 中文标点符号转英文字标点符号
   * 
   * @param str 原字符串
   * @return str 新字符串
   */
  public static final String cToe(String str) {
    String[] regs = {"！", "，", "。", "；", "~", "《", "》", "（", "）", "？", "”", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’",
        "!", ",", ".", ";", "`", "<", ">", "(", ")", "?", "'", "{", "}", "\"", ":", "{", "}", "\"", "\'", "\'"};
    for (int i = 0; i < regs.length / 2; i++) {
      str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
    }
    return str;
  }

  /**
   * 将法语声调字符转换为英文处理
   * 
   * @param str 原字符串
   * @return str 新字符串
   */
  public static final String fAccentToE(String str) {
    for (int i = 0; i < frenchAccentLetter.length / 2; i++) {
      str = str.replaceAll(frenchAccentLetter[i], frenchAccentLetter[i + frenchAccentLetter.length / 2]);
    }
    return str;
  }

  public static void main(String[] args) {
    String doi = "asfsdf!@#!@#!@#:|:|:|:|_-==++-+*/1231\"23 ";
    doi = doi.replaceAll("[^a-z0-9]", "");
    System.out.println(doi);
  }

}
