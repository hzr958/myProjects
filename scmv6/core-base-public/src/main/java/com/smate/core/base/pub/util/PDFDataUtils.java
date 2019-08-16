package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

public class PDFDataUtils {

  /**
   * 
   * @param pdfData
   * @return
   */
  public static String formatPDFData(String data) {
    if (StringUtils.isBlank(data)) {
      return "";
    }
    // data = full2Half(data);
    data = data.replace(" ", "");
    data = data.replace("ＤＯＩ：", "doi:");
    data = data.replace("０", "0");
    data = data.replace("１", "1");
    data = data.replace("２", "2");
    data = data.replace("３", "3");
    data = data.replace("４", "4");
    data = data.replace("５", "5");
    data = data.replace("６", "6");
    data = data.replace("７", "7");
    data = data.replace("８", "8");
    data = data.replace("９", "9");
    data = data.replace("／", "/");
    data = data.replace("（", "(");
    data = data.replace("）", ")");
    data = data.replace("－", "-");
    data = data.replace("．", ".");
    data = data.replace("ｉ", "i");
    data = data.replace("ｎ", "n");
    data = data.replace("ｓ", "s");
    data = data.replace("ｊ", "j");
    data = data.replace("∶", ":");
    return data;
  }

  /**
   * 全角转半角
   * 
   * @param string
   * @return
   */
  public static String full2Half(String string) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }
    char[] charArray = string.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      if (charArray[i] == 12288) {
        charArray[i] = ' ';
      } else if (charArray[i] >= ' ' && charArray[i] <= 65374) {
        charArray[i] = (char) (charArray[i] - 65248);
      }
    }
    return new String(charArray);
  }

  /**
   * 半角转全角
   * 
   * @param value
   * @return
   */
  public static String half2Full(String value) {
    if (StringUtils.isEmpty(value)) {
      return "";
    }
    char[] cha = value.toCharArray();

    /**
     * full blank space is 12288, half blank space is 32 others :full is 65281-65374,and half is 33-126.
     */
    for (int i = 0; i < cha.length; i++) {
      if (cha[i] == 32) {
        cha[i] = (char) 12288;
      } else if (cha[i] < 127) {
        cha[i] = (char) (cha[i] + 65248);
      }
    }
    return new String(cha);
  }

  /**
   * 匹配内容中的DOI数据
   * 
   * @param content
   * @return
   */
  public static String matchDOI(String content) {
    content = content.replace(" ", "");
    String DOI = "";
    if (StringUtils.isBlank(content)) {
      return "";
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("doi:[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("doi[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("http://dx\\.doi\\.org/[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("10(\\.){1}[^\\s]+(/){1}[^\\s]+", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        if (StringUtils.isBlank(DOI)) {
          DOI = m.group();
        }
      }
    }
    // 去除10.之前的字符
    if (StringUtils.isNotBlank(DOI)) {
      DOI = StringUtils.substring(DOI, DOI.indexOf("10."), DOI.length());
      DOI = DOI.replaceAll("[\\u4e00-\\u9fa5]", "");// 去除中文
    }
    return DOI;
  }

  /**
   * 判断是否存在doi.
   * 
   * @param content
   * @return
   */
  public static boolean hasDOI(String content) {
    content = content.replace(" ", "");
    boolean result = false;
    if (StringUtils.isBlank(content)) {
      return result;
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("doi:", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("doi:[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("doi[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("http://dx\\.doi\\.org/[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("10(\\.){1}[^\\s]+(/){1}[^\\s]+", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        return true;
      }
    }
    return result;
  }

  /**
   * 判断是否存在邮箱.
   * 
   * @param content
   * @return
   */
  public static boolean hasEmail(String content) {
    boolean result = false;
    if (StringUtils.isBlank(content)) {
      return result;
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        return true;
      }
    }
    return result;
  }

  /**
   * 判断是否存在关键词.
   * 
   * @param content
   * @return
   */
  public static boolean hasKeywords(String content) {
    boolean result = false;
    if (StringUtils.isBlank(content)) {
      return result;
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("keywords", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("关键字", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        return true;
      }
    }
    return result;
  }

  /**
   * 判断是否存在摘要.
   * 
   * @param content
   * @return
   */
  public static boolean hasAbstract(String content) {
    boolean result = false;
    if (StringUtils.isBlank(content)) {
      return result;
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("abstract", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("摘要", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        return true;
      }
    }
    return result;
  }

  /**
   * 判断是否存在作者.
   * 
   * @param content
   * @return
   */
  public static boolean hasAuthor(String content) {
    boolean result = false;
    if (StringUtils.isBlank(content)) {
      return result;
    }
    List<Pattern> pList = new ArrayList<>();
    pList.add(Pattern.compile("doi:[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("doi[\\s]+[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("http://dx\\.doi\\.org/[^\\s]+", Pattern.CASE_INSENSITIVE));
    pList.add(Pattern.compile("10(\\.){1}[^\\s]+(/){1}[^\\s]+", Pattern.CASE_INSENSITIVE));
    for (Pattern p : pList) {
      Matcher m = p.matcher(content);
      if (m.find()) {
        return true;
      }
    }
    return result;
  }


  public static void main(String[] args) {
    String doi = "ddd";
    System.out.println(full2Half(doi));
  }

}
