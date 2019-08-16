package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.data.XmlUtil;

public class AuthorNamesUtils {

  /**
   * 拆分作者名
   * 
   * @param authorNames
   * @return
   */
  public static List<String> parsePubAuthorNames(String authorNames) {
    String[] nameArr = null;
    if (StringUtils.isBlank(authorNames)) {
      return new ArrayList<>();
    }
    authorNames = cleanAuthorNameExceptComma(authorNames);
    nameArr = authorNames.split("[;；]");
    if (!authorNames.contains(";") && !authorNames.contains("；")) {
      nameArr = authorNames.split("[,，]");
    }
    List<String> list = new ArrayList<String>();
    for (String name : nameArr) {
      name = cleanAuthorNameExceptComma(name);
      if (StringUtils.isNotBlank(name)) {
        list.add(name);
      }
    }
    return list;
  }


  /**
   * 清理作者中的垃圾字符数据，包含逗号也一并清理
   * 
   * @param text
   * @return
   */
  public static String cleanAuthorName(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = cleanAllHtml(authorName);
    authorName = cleanNumber(authorName);
    authorName = cleanSpecBlank(authorName);
    authorName = cleanSpecChar(authorName);
    authorName = cleanBracket(authorName);

    // 将多个空格替换成一个空格
    authorName = authorName.replaceAll("\\s+", " ");
    // 去除前后空格
    authorName = authorName.trim();
    // 将 and (前后均有一个空格) 替换成;号
    authorName = authorName.replace(" and ", "; ");
    return authorName;
  }

  /**
   * 清理作者中的垃圾字符数据，但逗号保留，中文逗号会被替换成英文逗号
   * 
   * @param authorName
   * @return
   */
  public static String cleanAuthorNameExceptComma(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = cleanAllHtml(authorName);
    authorName = cleanNumber(authorName);
    authorName = cleanSpecBlank(authorName);
    authorName = cleanSpecCharExceptComma(authorName);
    authorName = cleanBracket(authorName);

    // 将多个空格替换成一个空格
    authorName = authorName.replaceAll("\\s+", " ");
    // 去除前后空格
    authorName = authorName.trim();
    // 将 and (前后均有一个空格) 替换成;号
    authorName = authorName.replace(" and ", "; ");
    return authorName;
  }

  /**
   * 去除所有的html标记
   * 
   * @param authorName
   * @return
   */
  protected static String cleanAllHtml(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = authorName.replace("&amp;", "&");
    authorName = authorName.replace("%26", "&");
    authorName = authorName.replace("&apos;", "'");
    authorName = HtmlUtils.htmlUnescape(authorName);
    authorName = XmlUtil.trimAllHtml(authorName);
    return authorName;
  }

  /**
   * 去除括号（包括括号里面的所有内容）
   * 
   * @param authorName
   * @return
   */
  protected static String cleanBracket(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    // 去除大括号内，以及大括号
    String regex1 = "\\{[^{}]+\\}|\\{\\}";
    authorName = authorName.replaceAll(regex1, "");
    // 去除方括号内，以及方括号
    String regex2 = "\\[[^\\[\\]]+\\]|\\[\\]";
    authorName = authorName.replaceAll(regex2, "");
    // 去除括号内，以及括号
    String regex3 = "\\([^\\(\\)]+\\)|\\(\\)";
    authorName = authorName.replaceAll(regex3, "");
    // 去除中文方括号内，以及中文方括号
    String regex4 = "【[^【】]+】|【】";
    authorName = authorName.replaceAll(regex4, "");
    // 去除中文括号内，以及中文括号
    String regex5 = "（[^（）]+）|（）";
    authorName = authorName.replaceAll(regex5, "");
    return authorName;
  }

  /**
   * 清除特殊字符
   * 
   * @param authorName
   * @return
   */
  protected static String cleanSpecChar(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    // 去除*
    authorName = authorName.replace("*", "");
    // 去除点
    authorName = authorName.replace("。", "").replace(".", " ");
    // 去除逗号
    authorName = authorName.replace("，", "").replace(",", "");
    // 去除-
    authorName = authorName.replace("-", " ");
    // 特殊字符
    authorName = authorName.replace("△", "");
    authorName = authorName.replace("○", "");
    authorName = authorName.replace("１", "");
    authorName = authorName.replace("２", "");
    authorName = authorName.replace("３", "");
    authorName = authorName.replace("、", "");
    authorName = authorName.replace("①", "");
    authorName = authorName.replace("#", "");
    authorName = authorName.replace("？", "").replace("?", "");
    return authorName;
  }

  /**
   * 去除特殊字符，但是除了逗号
   * 
   * @param authorName
   * @return
   */
  protected static String cleanSpecCharExceptComma(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    // 去除*
    authorName = authorName.replace("*", "");
    // 去除点
    authorName = authorName.replace("。", "").replace(".", " ");
    // 去除逗号
    authorName = authorName.replace("，", ",");
    // 去除-
    authorName = authorName.replace("-", " ");
    // 特殊字符
    authorName = authorName.replace("△", "");
    authorName = authorName.replace("○", "");
    authorName = authorName.replace("１", "");
    authorName = authorName.replace("２", "");
    authorName = authorName.replace("３", "");
    authorName = authorName.replace("、", "");
    authorName = authorName.replace("①", "");
    authorName = authorName.replace("#", "");
    authorName = authorName.replace("？", "").replace("?", "");
    return authorName;
  }

  /**
   * 清除特殊空格
   * 
   * @param authorName
   * @return
   */
  protected static String cleanSpecBlank(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = authorName.replace("", "");
    authorName = authorName.replace("", "");
    authorName = authorName.replace("", "");
    authorName = authorName.replace("", "");
    authorName = authorName.replace("", "");
    authorName = authorName.replace("　", "");
    return authorName;
  }

  /**
   * 清除所有的数字
   * 
   * @param authorName
   * @return
   */
  protected static String cleanNumber(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    return authorName.replaceAll("[\\d]", "");
  }

}
