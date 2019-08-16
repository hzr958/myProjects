package com.smate.center.task.single.util.pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.common.HashUtils;

/**
 * 关键词字典工具类.
 * 
 * @author lqh
 * 
 */
public final class KeywordsDicUtils {

  /**
   * 把字符串的每个字拆分出来，然后两两组合拼接.
   * 
   * @param str
   * @return
   */
  public static List<String> splitStrJoin2Words(String str) {

    // 词组组合
    List<String> wordsList = new ArrayList<String>();

    // 如果关键词是连续的全英文关键词
    if (str.matches("^[0-9|a-z|\\-]{2,}$")) {
      wordsList.add(str);
      return wordsList;
    }

    // 拆分出来的每个字
    List<String> wordList = new ArrayList<String>();
    char[] chrs = str.toCharArray();
    boolean en = false;
    String enStr = "";
    for (int i = 0; i < chrs.length; i++) {
      char chr = chrs[i];
      // [0-9|a-z|\\-]
      if ((chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9') || chr == '-') {
        en = true;
        enStr += chr;
        // 为了避免最后一个英文被遗漏
        if (i == (chrs.length - 1) && StringUtils.isNotBlank(enStr)) {
          wordList.add(enStr.trim());
        }
      } else {
        // 前面为英文，到当前字符，截止了
        if (en && StringUtils.isNotBlank(enStr)) {
          wordList.add(enStr.trim());
        }
        // 重置英文状态
        en = false;
        enStr = "";
        // 转成字符串
        String chrStr = String.valueOf(chr);
        if (StringUtils.isNotBlank(chrStr)) {
          wordList.add(chrStr);
        }
      }
    }
    // 所有词取出来之后，按照两两组合的方式搭配
    if (wordList.size() == 2) {
      wordsList.add(wordList.get(0) + wordList.get(1));
    } else {
      // 两两组合
      for (int i = 0, j = i + 1; j < wordList.size(); i++, j++) {
        wordsList.add(wordList.get(i) + wordList.get(j));
      }
    }
    return wordsList;
  }

  /**
   * 获取关键词长度.
   * 
   * @param keywords
   * @return
   * @throws ServiceException
   */
  public static int getKwWordLength(String keywords) {

    if (StringUtils.isBlank(keywords)) {
      return 0;
    }
    if (StringUtils.isAsciiPrintable(keywords)) {
      return getEnKwWordLength(keywords);
    }
    return getZhKwWordLength(keywords);
  }

  /**
   * 获取中文关键词或者中英混合关键词长度.
   * 
   * @param zhKw
   * @return
   */
  private static int getZhKwWordLength(String zhKw) {

    String lzhKw = zhKw.toLowerCase();
    // 中文关键词里面可能会有英文关键词，先剔除出来
    Pattern p = Pattern.compile("[0-9a-z\\-\\.\\_]{2,}");
    Matcher m = p.matcher(lzhKw);
    int num = 0;
    while (m.find()) {
      num++;
    }
    lzhKw = lzhKw.replaceAll("[0-9a-z\\-\\.\\_]{2,}", "");

    // 非中文字符，不算长度
    String[] strs = lzhKw.split("");
    for (String str : strs) {
      if (StringUtils.isNotBlank(str) && !StringUtils.isAsciiPrintable(str)) {
        num++;
      }
    }
    return num;
  }

  /**
   * 获取英文关键词长度.
   * 
   * @param enKw
   * @return
   * @throws ServiceException
   */
  private static int getEnKwWordLength(String enKw) {

    // 直接用非a-z 0-9 - .的字符拆分
    String[] strs = enKw.toLowerCase().split("[^a-z0-9\\-\\.\\_]");
    int num = 0;
    for (String str : strs) {
      if (StringUtils.isNotBlank(str)) {
        num++;
      }
    }
    return num;
  }

  /**
   * 拆分关键词.
   * 
   * @param keywords
   * @return
   */
  public static List<String> splitKeywordsList(String keywords) {
    if (StringUtils.isBlank(keywords)) {
      return null;
    }

    keywords = keywords.replace("，", ";");
    keywords = keywords.replace("；", ";");
    keywords = keywords.replace("：", ";");
    keywords = keywords.replace("。", ";");
    keywords = keywords.replaceAll("\\s+", " ");
    String[] kws = keywords.split(";");

    List<String> kwList = new ArrayList<String>();
    outer_loop: for (String kw : kws) {
      if (StringUtils.isBlank(kw)) {
        continue;
      }
      kw = kw.trim();
      for (String dupkw : kwList) {
        if (dupkw.equalsIgnoreCase(kw)) {
          continue outer_loop;
        }
      }
      kwList.add(kw);
    }
    return kwList;
  }

  /**
   * 拆分内容，如果是英文单词，先拆分出来，其他按照两个两个连接的方式单独拆分出来，计算每个组合的hashcode.
   * 
   * @param content
   * @return
   */
  public static Set<Long> splitStr2WordHash(String content) {
    if (StringUtils.isBlank(content)) {
      return null;
    }
    content = content.toLowerCase();
    Set<Long> wordHashSet = new HashSet<Long>();
    // 英文，先单独拆分出来.
    if (content.matches(".*[0-9|a-z|\\-]{2,}.*")) {
      Pattern p = Pattern.compile("[0-9|a-z|\\-]{2,}");
      Matcher m = p.matcher(content);
      while (m.find()) {
        String word = m.group();
        wordHashSet.add(HashUtils.getStrHashCode(word));
      }
    }
    // 所有词两两组合
    List<String> split2Words = KeywordsDicUtils.splitStrJoin2Words(content);
    for (String str : split2Words) {
      if (StringUtils.isNotBlank(str)) {
        wordHashSet.add(HashUtils.getStrHashCode(str));
      }
    }
    return wordHashSet;
  }

  public static void main(String[] args) {

    String a = "孙子剩余定理";
    int n = getKwWordLength(a);
    System.out.println(n);

  }
}
